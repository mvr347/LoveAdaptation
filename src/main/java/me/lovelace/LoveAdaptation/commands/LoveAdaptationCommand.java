package me.lovelace.LoveAdaptation.commands;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.models.AdaptationData;
import me.lovelace.LoveAdaptation.models.AdaptationType;
import me.lovelace.LoveAdaptation.models.PlayerData;
import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoveAdaptationCommand implements CommandExecutor, TabCompleter {

    private final LoveAdaptation plugin;

    public LoveAdaptationCommand(LoveAdaptation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");

        if (args.length == 0 || args[0].equalsIgnoreCase("menu")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.only_players", "&cЭта команда доступна только игрокам.")));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("loveadaptation.use")) {
                player.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
                return true;
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dm open adaptation_menu " + player.getName());
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("info")) {
            Player target;
            if (args.length >= 2) {
                target = Bukkit.getPlayer(args[1]);
            } else if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(Utils.color(prefix + "&cУкажите имя игрока."));
                return true;
            }

            if (target == null) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.player_not_found", "&cИгрок не найден.")));
                return true;
            }

            PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(target.getUniqueId());
            if (data == null) {
                sender.sendMessage(Utils.color(prefix + "&cДанные игрока не загружены."));
                return true;
            }

            String header = plugin.getConfig().getString("lang.check_header", "&6=== Прогресс адаптаций %player% ===").replace("%player%", target.getName());
            sender.sendMessage(Utils.color(header));

            for (Map.Entry<AdaptationType, AdaptationData> entry : data.getAdaptationProgressMap().entrySet()) {
                AdaptationType type = entry.getKey();
                AdaptationData adaptData = entry.getValue();

                String name = PluginManager.getInstance().getAdaptationManager().getAdaptationDisplayName(type);
                double percent = adaptData.getProgressPercent();

                String status;
                if (data.getCurrentAdaptation() == type) status = "Активна";
                else if (percent >= 90.0) status = "Мастерство";
                else if (percent > 0) status = "Прокачка";
                else status = "Заблокирована";

                String itemStr = plugin.getConfig().getString("lang.check_item", "&7%adaptation_name%: &f%progress%% &7(%status%)")
                        .replace("%adaptation_name%", name)
                        .replace("%progress%", String.format("%.1f", percent))
                        .replace("%status%", status);
                sender.sendMessage(Utils.color(itemStr));
            }
            return true;
        }

        if (sub.equals("reset")) {
            if (!sender.hasPermission("loveadaptation.admin")) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptation reset <player>"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.player_not_found", "&cИгрок не найден.")));
                return true;
            }

            PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(target.getUniqueId());
            if (data != null) {
                data.setCurrentAdaptation(AdaptationType.BASE);
                for (AdaptationData adaptData : data.getAdaptationProgressMap().values()) {
                    adaptData.setProgressPercent(0.0);
                    adaptData.setProgressValue(0);
                    adaptData.setUnlocked(false);
                    adaptData.setActive(false);
                }
                plugin.getDatabaseManager().savePlayerData(data);
            }

            String msg = plugin.getConfig().getString("lang.reset_success", "&aВсе адаптации игрока %player% сброшены.").replace("%player%", target.getName());
            sender.sendMessage(Utils.color(prefix + msg));
            return true;
        }

        if (sub.equals("check")) {
            if (!sender.hasPermission("loveadaptation.admin")) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptation check <adaptation_name> [player]"));
                return true;
            }

            AdaptationType type = AdaptationType.fromString(args[1]);
            if (type == AdaptationType.BASE) {
                sender.sendMessage(Utils.color(prefix + "&cАдаптация '" + args[1] + "' не найдена."));
                return true;
            }

            Player target = (args.length >= 3) ? Bukkit.getPlayer(args[2]) : (sender instanceof Player ? (Player) sender : null);
            if (target == null) {
                sender.sendMessage(Utils.color(prefix + "&cИгрок не найден или не указан."));
                return true;
            }

            PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(target.getUniqueId());
            if (data == null) {
                sender.sendMessage(Utils.color(prefix + "&cДанные игрока не загружены."));
                return true;
            }

            AdaptationData adaptData = data.getAdaptationData(type);
            double percent = adaptData != null ? adaptData.getProgressPercent() : 0.0;
            long val = adaptData != null ? adaptData.getProgressValue() : 0;

            String name = PluginManager.getInstance().getAdaptationManager().getAdaptationDisplayName(type);
            sender.sendMessage(Utils.color(prefix + "&6=== Отладка адаптации " + name + " (" + target.getName() + ") ==="));
            sender.sendMessage(Utils.color("&7Прогресс: &f" + String.format("%.2f%%", percent) + (percent >= 90.0 ? " ⭐" : "")));
            sender.sendMessage(Utils.color("&7Значение метрики: &f" + val + " &7(" + type.getProgressMetric() + ")"));
            sender.sendMessage(Utils.color("&7Разблокирована мастерством: &f" + (adaptData != null && adaptData.isUnlocked())));
            sender.sendMessage(Utils.color("&7Активна сейчас: &f" + (adaptData != null && adaptData.isActive())));
            return true;
        }

        if (sub.equals("givepotion")) {
            if (!sender.hasPermission("loveadaptation.admin")) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptation givepotion <player> <1|2>"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.player_not_found", "&cИгрок не найден.")));
                return true;
            }

            int level = 1;
            try {
                level = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {}

            ItemStack potion = PluginManager.getInstance().getPotionManager().createAdaptationPotion(level);
            target.getInventory().addItem(potion);

            sender.sendMessage(Utils.color(prefix + "&aВыдали зелье адаптации уровня " + level + " игроку " + target.getName()));
            return true;
        }

        if (sub.equals("reload")) {
            if (!sender.hasPermission("loveadaptation.admin")) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
                return true;
            }

            plugin.reloadConfig();
            PluginManager.getInstance().getAdaptationManager().saveAllPlayers();

            sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.reloaded", "&aКонфигурация перезагружена!")));
            return true;
        }

        sender.sendMessage(Utils.color(prefix + "&cНеизвестная подкоманда. Используйте /loveadaptation menu|info|reset|givepotion|reload"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("menu", "info");
            if (sender.hasPermission("loveadaptation.admin")) {
                subs = Arrays.asList("menu", "info", "reset", "check", "givepotion", "reload");
            }
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) completions.add(s);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
            for (AdaptationType type : AdaptationType.values()) {
                if (type == AdaptationType.BASE) continue;
                if (type.getConfigKey().startsWith(args[1].toLowerCase())) {
                    completions.add(type.getConfigKey());
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("givepotion"))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) completions.add(p.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("check")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) completions.add(p.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("givepotion")) {
            completions.addAll(Arrays.asList("1", "2"));
        }
        return completions;
    }
}
