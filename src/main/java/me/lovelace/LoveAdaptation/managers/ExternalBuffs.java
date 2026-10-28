package me.lovelace.LoveAdaptation.managers;

import dev.lovelace.lovecore.api.buff.PassiveBuffSource;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Applies the passive bonuses that other plugins publish through {@link PassiveBuffSource}.
 * Potion effects are refreshed with the same rule as adaptation effects; attribute bonuses are
 * kept as modifiers keyed {@code loveadaptation:buff_<source>_<attribute>} so they can be
 * recognised and removed when the source stops granting them.
 */
public final class ExternalBuffs {
    private static final String PREFIX = "buff_";

    private final Plugin plugin;
    private final int refreshThreshold;

    public ExternalBuffs(Plugin plugin, int refreshThreshold) {
        this.plugin = plugin;
        this.refreshThreshold = refreshThreshold;
    }

    public void apply(Player player) {
        Map<String, Double> desired = new HashMap<>();
        Map<String, Attribute> attributesByKey = new HashMap<>();
        for (var registration : Bukkit.getServicesManager().getRegistrations(PassiveBuffSource.class)) {
            PassiveBuffSource source = registration.getProvider();
            try {
                for (PotionEffect effect : source.effects(player)) applyEffect(player, effect);
                for (var bonus : source.attributes(player).entrySet()) {
                    String key = key(source.id(), bonus.getKey());
                    desired.merge(key, bonus.getValue(), Double::sum);
                    attributesByKey.put(key, bonus.getKey());
                }
            } catch (RuntimeException e) {
                plugin.getLogger().warning("Buff source " + source.id() + " failed: " + e.getMessage());
            }
        }
        applyAttributes(player, desired, attributesByKey);
    }

    private void applyEffect(Player player, PotionEffect wanted) {
        PotionEffect current = player.getPotionEffect(wanted.getType());
        if (AdaptationRules.needsEffectRefresh(current != null,
                current != null ? current.getDuration() : 0,
                current != null ? current.getAmplifier() : 0,
                wanted.getAmplifier(), refreshThreshold)) {
            int duration = Math.max(wanted.getDuration(), refreshThreshold + 20);
            player.addPotionEffect(new PotionEffect(wanted.getType(), duration, wanted.getAmplifier(), true, false, true));
        }
    }

    private void applyAttributes(Player player, Map<String, Double> desired, Map<String, Attribute> byKey) {
        for (Attribute attribute : Registry.ATTRIBUTE) {
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance == null) continue;
            Map<String, Double> present = new HashMap<>();
            Map<String, AttributeModifier> presentModifiers = new HashMap<>();
            for (AttributeModifier modifier : instance.getModifiers()) {
                NamespacedKey id = modifier.getKey();
                if (id.getNamespace().equals(plugin.getName().toLowerCase(Locale.ROOT))
                        && id.getKey().startsWith(PREFIX)) {
                    present.put(id.getKey(), modifier.getAmount());
                    presentModifiers.put(id.getKey(), modifier);
                }
            }
            Map<String, Double> wanted = new HashMap<>();
            for (var entry : desired.entrySet())
                if (byKey.get(entry.getKey()) == attribute) wanted.put(entry.getKey(), entry.getValue());
            BuffPlan plan = BuffPlan.diff(wanted, present);
            if (plan.isEmpty()) continue;
            for (String key : plan.toRemove()) instance.removeModifier(presentModifiers.get(key));
            for (var add : plan.toAdd().entrySet())
                instance.addModifier(new AttributeModifier(
                        new NamespacedKey(plugin, add.getKey()), add.getValue(), AttributeModifier.Operation.ADD_NUMBER));
        }
    }

    private static String key(String sourceId, Attribute attribute) {
        return PREFIX + sourceId.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_")
                + "_" + attribute.getKey().getKey();
    }
}
