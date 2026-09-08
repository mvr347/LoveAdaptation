package me.lovelace.LoveAdaptation.commands;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
 * Команда для управления Полевым Бестиарием: {@code /bestiary <give|open> [player]}.
 */
public class BestiaryCommand implements CommandExecutor, TabCompleter {

    private final LoveAdaptation plugin;

    public BestiaryCommand(LoveAdaptation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = plugin.getConfig().getString("lang.prefix", "&8[&6LoveAdaptation&8] ");

        if (args.length == 0) {
            if (sender instanceof Player player) {
                ItemStack book = PluginManager.getInstance().getBestiaryManager().getBestiaryFromInventory(player);
                if (book != null) {
                    PluginManager.getInstance().getBestiaryManager().openBestiary(player, book);
                    return true;
                } else {
                    player.sendMessage(Utils.color(prefix + "&cУ вас нет Полевого Бестиария в инвентаре."));
                    return true;
                }
            }
            sendHelp(sender, label);
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("give")) {
            if (!sender.hasPermission("loveadaptation.admin")) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.no_permission", "&cУ вас нет прав.")));
                return true;
            }

            Player target;
            if (args.length >= 2) {
                target = Bukkit.getPlayer(args[1]);
            } else if (sender instanceof Player p) {
                target = p;
            } else {
                sender.sendMessage(Utils.color(prefix + "&cУкажите имя игрока."));
                return true;
            }

            if (target == null) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.player_not_found", "&cИгрок не найден.")));
                return true;
            }

            ItemStack book = PluginManager.getInstance().getBestiaryManager().createBestiaryBook(target);
            target.getInventory().addItem(book);
            target.sendMessage(Utils.color("&aВам выдан Полевой Бестиарий!"));
            target.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);

            if (!target.equals(sender)) {
                sender.sendMessage(Utils.color(prefix + "&aВы выдали Полевой Бестиарий игроку &f" + target.getName()));
            }
            return true;
        }

        if (sub.equals("open")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Utils.color(prefix + plugin.getConfig().getString("lang.only_players", "&cЭта команда доступна только игрокам.")));
                return true;
            }

            ItemStack book = PluginManager.getInstance().getBestiaryManager().getBestiaryFromInventory(player);
            if (book != null) {
                PluginManager.getInstance().getBestiaryManager().openBestiary(player, book);
            } else {
                player.sendMessage(Utils.color(prefix + "&cУ вас нет Полевого Бестиария в инвентаре."));
            }
            return true;
        }

        sendHelp(sender, label);
        return true;
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(Utils.color("&8========== &6Полевой Бестиарий &8=========="));
        sender.sendMessage(Utils.color("&6/" + label + " open &7- Открыть бестиарий из инвентаря"));
        if (sender.hasPermission("loveadaptation.admin")) {
            sender.sendMessage(Utils.color("&6/" + label + " give [игрок] &7- Выдать книгу Полевого Бестиария"));
        }
        sender.sendMessage(Utils.color("&8=========================================="));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("open");
            if (sender.hasPermission("loveadaptation.admin")) {
                list.add("give");
            }
            return list.stream().filter(s -> s.startsWith(args[0].toLowerCase())).toList();
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give") && sender.hasPermission("loveadaptation.admin")) {
            List<String> list = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(p.getName());
                }
            }
            return list;
        }

        return Collections.emptyList();
    }
}
