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
import java.util.Collections;
import java.util.List;

/**
 * Единая административная команда плагина: {@code /loveadaptationadmin <subcommand>}.
 * <p>
 * Раньше admin-подкоманды (reload, reset, check, givepotion) были зарыты внутри
 * {@code /loveadaptation}, что не соответствует принятому в экосистеме Love* стилю единой
 * родительской admin-команды с подкомандами. Старые пути из {@link LoveAdaptationCommand}
 * теперь просто перенаправляют сюда короткой подсказкой, а не тихо пропадают.
 */
public class LoveAdaptationAdminCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("reload", "reset", "check", "givepotion", "givebestiary", "help");

    private final LoveAdaptation plugin;

    public LoveAdaptationAdminCommand(LoveAdaptation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");

        if (!sender.hasPermission("loveadaptation.admin")) {
            sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> handleReload(sender);
            case "reset" -> handleReset(sender, args);
            case "check" -> handleCheck(sender, args);
            case "givepotion" -> handleGivePotion(sender, args);
            case "givebestiary" -> handleGiveBestiary(sender, args);
            default -> sendHelp(sender);
        }
        return true;
    }

    private void handleReload(CommandSender sender) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");
        plugin.reloadConfig();
        PluginManager.getInstance().getAdaptationManager().saveAllPlayers();
        sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.reloaded", "&aКонфигурация и база данных успешно перезагружены!")));
    }

    private void handleReset(CommandSender sender, String[] args) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");

        if (args.length < 2) {
            sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptationadmin reset <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.player_not_found", "&cИгрок не найден.")));
            return;
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
    }

    private void handleCheck(CommandSender sender, String[] args) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");

        if (args.length < 2) {
            sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptationadmin check <adaptation_name> [player]"));
            return;
        }

        AdaptationType type = AdaptationType.fromString(args[1]);
        if (type == AdaptationType.BASE) {
            sender.sendMessage(Utils.color(prefix + "&cАдаптация '" + args[1] + "' не найдена."));
            return;
        }

        Player target = (args.length >= 3) ? Bukkit.getPlayer(args[2]) : (sender instanceof Player ? (Player) sender : null);
        if (target == null) {
            sender.sendMessage(Utils.color(prefix + "&cИгрок не найден или не указан."));
            return;
        }

        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(target.getUniqueId());
        if (data == null) {
            sender.sendMessage(Utils.color(prefix + "&cДанные игрока не загружены."));
            return;
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
    }

    private void handleGivePotion(CommandSender sender, String[] args) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");

        if (args.length < 3) {
            sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptationadmin givepotion <player> <1|2>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.player_not_found", "&cИгрок не найден.")));
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Utils.color(prefix + "&cУровень должен быть числом 1 или 2."));
            return;
        }
        if (level != 1 && level != 2) {
            sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptationadmin givepotion <player> <1|2>"));
            return;
        }

        ItemStack potion = PluginManager.getInstance().getPotionManager().createAdaptationPotion(level);
        target.getInventory().addItem(potion);

        sender.sendMessage(Utils.color(prefix + "&aВыдали зелье адаптации уровня " + level + " игроку " + target.getName()));
    }

    private void handleGiveBestiary(CommandSender sender, String[] args) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");

        if (args.length < 2) {
            if (sender instanceof Player player) {
                ItemStack book = PluginManager.getInstance().getBestiaryManager().createBestiaryBook(player);
                player.getInventory().addItem(book);
                sender.sendMessage(Utils.color(prefix + "&aВам выдан Полевой Бестиарий!"));
                return;
            }
            sender.sendMessage(Utils.color(prefix + "&cИспользование: /loveadaptationadmin givebestiary <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.player_not_found", "&cИгрок не найден.")));
            return;
        }

        ItemStack book = PluginManager.getInstance().getBestiaryManager().createBestiaryBook(target);
        target.getInventory().addItem(book);
        target.sendMessage(Utils.color("&aВам выдан Полевой Бестиарий!"));
        sender.sendMessage(Utils.color(prefix + "&aВыдали Полевой Бестиарий игроку " + target.getName()));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.admin_help_header", "&8========== &6LoveAdaptation Admin &8==========")));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.admin_help_reload", "&6/loveadaptationadmin reload &7- Перезагрузить конфигурацию")));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.admin_help_reset", "&6/loveadaptationadmin reset <игрок> &7- Сбросить прогресс адаптаций игрока")));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.admin_help_check", "&6/loveadaptationadmin check <адаптация> [игрок] &7- Показать детальный прогресс адаптации")));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.admin_help_givepotion", "&6/loveadaptationadmin givepotion <игрок> <1|2> &7- Выдать зелье адаптации")));
        sender.sendMessage(Utils.color("&6/loveadaptationadmin givebestiary <игрок> &7- Выдать книгу Полевого Бестиария"));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("lang.admin_help_footer", "&8==========================================")));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("loveadaptation.admin")) return Collections.emptyList();

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (String s : SUBCOMMANDS) {
                if (s.startsWith(args[0].toLowerCase())) completions.add(s);
            }
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
            for (AdaptationType type : AdaptationType.values()) {
                if (type == AdaptationType.BASE) continue;
                if (type.getConfigKey().startsWith(args[1].toLowerCase())) {
                    completions.add(type.getConfigKey());
                }
            }
            return completions;
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("givepotion") || args[0].equalsIgnoreCase("givebestiary"))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) completions.add(p.getName());
            }
            return completions;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("check")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) completions.add(p.getName());
            }
            return completions;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("givepotion")) {
            completions.addAll(Arrays.asList("1", "2"));
            return completions;
        }

        return completions;
    }
}
