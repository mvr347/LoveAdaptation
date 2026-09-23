package me.lovelace.LoveAdaptation.gui;

import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Resolves a configurable GUI item (material, custom head texture, or fallback icon) from
 * gui.yml-supplied fields. Added 2026-09-23 alongside {@code items.custom_buttons} in gui.yml —
 * mirrors the same architecture/precedence as LoveBehaivor's {@code GuiItemBuilder#resolveIcon}/
 * {@code #resolveMaterialOrHead}, adapted to LoveAdaptation's legacy {@code '&'}-color item
 * metadata (this plugin doesn't use Adventure Components for GUI text) instead of MiniMessage.
 */
public final class GuiItemBuilder {

    private GuiItemBuilder() {
        // Утилитарный класс, инстанцирование не предполагается
    }

    /**
     * Precedence, same as LoveBehaivor: explicit {@code texture} (raw base64/URL) wins if set;
     * otherwise {@code material} is used — either a plain {@link Material} name, or the same
     * {@code basehead-<base64>} syntax gui_gen v2.1 uses for content-zone heads; otherwise
     * {@code defaultTexture} (the plugin's built-in fallback icon for this button); otherwise a
     * plain STONE so a broken/empty config never renders an invisible or null item.
     */
    public static ItemStack resolveIcon(String texture, String material, String defaultTexture,
                                         String name, List<String> lore) {
        if (texture != null && !texture.isBlank()) {
            return Utils.createCustomHead(texture, name, lore);
        }
        if (material != null && !material.isBlank()) {
            return fromMaterialOrBasehead(material, name, lore);
        }
        if (defaultTexture != null && !defaultTexture.isBlank()) {
            return Utils.createCustomHead(defaultTexture, name, lore);
        }
        return fromMaterialOrBasehead("STONE", name, lore);
    }

    private static ItemStack fromMaterialOrBasehead(String materialOrBasehead, String name, List<String> lore) {
        if (materialOrBasehead.regionMatches(true, 0, "basehead-", 0, "basehead-".length())) {
            return Utils.createCustomHead(materialOrBasehead, name, lore);
        }
        Material resolved = Material.matchMaterial(materialOrBasehead.toUpperCase());
        ItemStack item = new ItemStack(resolved != null ? resolved : Material.STONE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (name != null) {
                meta.setDisplayName(Utils.color(name));
            }
            if (lore != null) {
                meta.setLore(Utils.color(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
