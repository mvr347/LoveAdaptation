package me.lovelace.LoveAdaptation.managers;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.models.PlayerData;
import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.util.List;

public class PotionManager {

    private final LoveAdaptation plugin;
    private final NamespacedKey potionKey;

    public PotionManager(LoveAdaptation plugin) {
        this.plugin = plugin;
        this.potionKey = new NamespacedKey(plugin, "adaptation_potion");
    }

    public ItemStack createAdaptationPotion(int level) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            String path = level == 2 ? "potions.adaptation_potion_ii" : "potions.adaptation_potion_i";
            String name = plugin.getConfig().getString(path + ".name", level == 2 ? "&eУсиленное зелье адаптации II" : "&eЗелье адаптации I");
            List<String> lore = plugin.getConfig().getStringList(path + ".lore");

            meta.setDisplayName(Utils.color(name));
            meta.setLore(Utils.color(lore));
            meta.setColor(Color.fromRGB(0, 200, 255)); // Cyan / adaptation glowing color
            meta.setBasePotionType(PotionType.WATER);
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

            meta.getPersistentDataContainer().set(potionKey, PersistentDataType.INTEGER, level);
            potion.setItemMeta(meta);
        }
        return potion;
    }

    public boolean isAdaptationPotion(ItemStack item) {
        if (item == null || item.getType() != Material.POTION || !item.hasItemMeta()) return false;
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(potionKey, PersistentDataType.INTEGER);
    }

    public int getPotionLevel(ItemStack item) {
        if (!isAdaptationPotion(item)) return 0;
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null) return 0;
        Integer level = meta.getPersistentDataContainer().get(potionKey, PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }

    public void applyAdaptationPotion(Player player, int level) {
        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(player.getUniqueId());
        if (data == null) return;

        int seconds = level == 2 ? 
                plugin.getConfig().getInt("potions.adaptation_potion_ii.duration_seconds", 120) :
                plugin.getConfig().getInt("potions.adaptation_potion_i.duration_seconds", 60);

        long expiresAt = System.currentTimeMillis() + (seconds * 1000L);
        data.setPotionExpiresAt(expiresAt);
        data.setActivePotionType(level == 2 ? "II" : "I");

        String potionName = level == 2 ? "Усиленное зелье адаптации II" : "Зелье адаптации I";
        String msg = plugin.getConfig().getString("lang.potion_applied", "&eВы выпили %potion_name%! Все адаптации активны на %duration% сек.")
                .replace("%potion_name%", potionName)
                .replace("%duration%", String.valueOf(seconds));

        Utils.sendMessage(player, plugin.getConfig().getString("lang.prefix", "") + msg);
        Utils.playSound(player, "BLOCK_BEACON_ACTIVATE", 1.0f, 1.2f);
    }
}
