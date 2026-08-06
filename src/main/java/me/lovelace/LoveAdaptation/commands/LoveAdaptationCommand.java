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
            if (Bukkit.getPluginManager().getPlugin("DeluxeMenus") == null) {
                player.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_deluxemenus",
                        "&cМеню недоступно: не установлен DeluxeMenus.")));
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

        // Админ-подкоманды (reset, check, givepotion, reload) переехали под единую
        // /loveadaptationadmin — здесь остаётся только понятная подсказка, чтобы команда не
        // «молчала» для тех, кто по привычке набирает /loveadaptation reload и т.п.
        if (sub.equals("reset") || sub.equals("check") || sub.equals("givepotion") || sub.equals("reload")) {
            if (!sender.hasPermission("loveadaptation.admin")) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
                return true;
            }
            sendAdminMoved(sender, "/loveadaptationadmin " + sub);
            return true;
        }

        if (sub.equals("help")) {
            sendHelp(sender);
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendAdminMoved(CommandSender sender, String newCommand) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");
        String msg = plugin.getConfig().getString("lang.admin_moved", "&eЭта команда перемещена. Используйте: &f%command%").replace("%command%", newCommand);
        sender.sendMessage(Utils.color(prefix + msg));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.help_header", "&8========== &6LoveAdaptation Помощь &8==========")));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.help_menu", "&6/adaptation menu &7- Открыть меню адаптаций и посмотреть прогресс")));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.help_info", "&6/adaptation info [игрок] &7- Показать прогресс адаптаций")));
        if (sender.hasPermission("loveadaptation.admin")) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.help_admin", "&6/loveadaptationadmin &7- Административные команды LoveAdaptation")));
        }
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.help_footer", "&8==========================================")));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String s : Arrays.asList("menu", "info", "help")) {
                if (s.startsWith(args[0].toLowerCase())) completions.add(s);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) completions.add(p.getName());
            }
        }
        return completions;
    }
}
