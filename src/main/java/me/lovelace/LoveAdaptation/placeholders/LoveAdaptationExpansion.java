package me.lovelace.LoveAdaptation.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.models.AdaptationData;
import me.lovelace.LoveAdaptation.models.AdaptationType;
import me.lovelace.LoveAdaptation.models.PlayerData;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LoveAdaptationExpansion extends PlaceholderExpansion {

    private final LoveAdaptation plugin;

    public LoveAdaptationExpansion(LoveAdaptation plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "loveadaptation";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().isEmpty() ? "Lovelace" : plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(player.getUniqueId());
        if (data == null) return "";

        params = params.toLowerCase();

        if (params.equals("current_name")) {
            return PluginManager.getInstance().getAdaptationManager().getAdaptationDisplayName(data.getCurrentAdaptation());
        }

        if (params.equals("current_color")) {
            return PluginManager.getInstance().getAdaptationManager().getAdaptationColor(data.getCurrentAdaptation());
        }

        if (params.equals("current_progress") || params.equals("progress_percent")) {
            AdaptationData adaptData = data.getAdaptationData(data.getCurrentAdaptation());
            double percent = adaptData != null ? adaptData.getProgressPercent() : 0.0;
            return String.format("%.1f%%", percent) + (percent >= 90.0 ? " ⭐" : "");
        }

        if (params.equals("blocked_count")) {
            int blocked = 0;
            if (data.getCurrentAdaptation() != AdaptationType.BASE) {
                AdaptationData activeData = data.getAdaptationData(data.getCurrentAdaptation());
                if (activeData != null && activeData.getProgressPercent() >= 90.0) {
                    blocked = 6;
                }
            }
            return blocked + "/7 заблокировано";
        }

        if (params.equals("potion_cooldown")) {
            if (data.isPotionActive()) {
                long remainingSec = (data.getPotionExpiresAt() - System.currentTimeMillis()) / 1000L;
                return "⏱️ " + remainingSec + "s (активно)";
            }
            return "✓ Доступно";
        }

        if (params.equals("progress_next_adaptation")) {
            AdaptationType bestType = null;
            double highest = -1;
            for (Map.Entry<AdaptationType, AdaptationData> entry : data.getAdaptationProgressMap().entrySet()) {
                if (entry.getKey() == data.getCurrentAdaptation()) continue;
                if (entry.getValue().getProgressPercent() > highest && entry.getValue().getProgressPercent() < 90.0) {
                    highest = entry.getValue().getProgressPercent();
                    bestType = entry.getKey();
                }
            }
            if (bestType != null) {
                String name = PluginManager.getInstance().getAdaptationManager().getAdaptationDisplayName(bestType);
                return name + " (" + String.format("%.1f%%", highest) + ")";
            }
            return "Нет доступных";
        }

        // Patterns like water_progress, nether_status, etc.
        for (AdaptationType type : AdaptationType.values()) {
            if (type == AdaptationType.BASE) continue;
            String prefix = type.getConfigKey() + "_";
            if (params.startsWith(prefix)) {
                String sub = params.substring(prefix.length());
                AdaptationData adaptData = data.getAdaptationData(type);
                double percent = adaptData != null ? adaptData.getProgressPercent() : 0.0;
                boolean isCurrent = data.getCurrentAdaptation() == type;
                boolean isBlocked = data.getCurrentAdaptation() != AdaptationType.BASE && !isCurrent && data.getAdaptationData(data.getCurrentAdaptation()).getProgressPercent() >= 90.0;

                if (sub.equals("progress")) {
                    if (isBlocked) return "🔒";
                    return String.format("%.1f%%", percent) + (percent >= 90.0 ? " ⭐" : "");
                }
                if (sub.equals("status")) {
                    if (isCurrent && percent >= 90.0) return "✓ Активна (⭐ Мастерство)";
                    if (isCurrent) return "✓ Активна";
                    if (isBlocked) return "🔒 Заблокирована";
                    if (percent > 0) return "⏳ Прокачка (" + String.format("%.1f%%", percent) + ")";
                    return "🔐 Не разблокирована";
                }
                if (sub.equals("indicator")) {
                    if (isCurrent && percent >= 90.0) return "⭐";
                    if (isCurrent) return "✓";
                    if (isBlocked) return "🔒";
                    if (percent > 0) return "⏳";
                    return "⬜";
                }
                if (sub.equals("bar")) {
                    return me.lovelace.LoveAdaptation.utils.Utils.buildProgressBar(percent, 10);
                }
                if (sub.equals("next_bonus")) {
                    return "Достигни 90% для бонусов мастерства";
                }
            }
        }

        return null;
    }
}
