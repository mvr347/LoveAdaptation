package me.lovelace.LoveAdaptation.utils;

import dev.lovelace.lovecore.api.LoveCore;
import dev.lovelace.lovecore.api.notify.LoveNotify;
import dev.lovelace.lovecore.api.notify.LoveNotify.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Thin wrapper over LoveCore's {@code LoveNotify}. Event-style notices go through it so a player's
 * {@code /lovenotify} choice is respected and LoveCore can fall back to chat for long or important text.
 * Without LoveCore the message is sent directly, exactly as before the wrapper existed.
 *
 * <p>Command replies, help, lists and the combat HP read-out stay on direct sends.</p>
 */
public final class Notify {

    /** What kind of notice is being shown; decides which channels are requested. */
    public enum Kind { ACTION_BAR, TITLE, EVENT, IMPORTANT }

    private Notify() {
    }

    /** Short status line, action bar preferred. */
    public static void actionBar(Player player, Component message) {
        send(player, message, Component.empty(), Kind.ACTION_BAR);
    }

    /**
     * Big on-screen notice. LoveNotify carries a single component, so title and subtitle are joined
     * for it; without LoveCore the classic title + subtitle pair is shown.
     */
    public static void title(Player player, Component title, Component subtitle) {
        send(player, title, subtitle, Kind.TITLE);
    }

    /** Short event message that used to be a plain chat line. */
    public static void event(Player player, Component message) {
        send(player, message, Component.empty(), Kind.EVENT);
    }

    /** Critical notice, always duplicated into chat by LoveCore. */
    public static void important(Player player, Component message) {
        send(player, message, Component.empty(), Kind.IMPORTANT);
    }

    private static void send(Player player, Component message, Component subtitle, Kind kind) {
        if (player == null || !player.isOnline() || message == null) return;
        try {
            // LoveCore.service is looked up on every call: a neighbour may register it later.
            if (Gateway.notify(player, message, subtitle, kind)) return;
        } catch (NoClassDefFoundError missingLoveCore) {
            // LoveCore is only a soft dependency: fall through to the direct send
        }
        switch (kind) {
            case ACTION_BAR -> player.sendActionBar(message);
            case TITLE -> player.showTitle(Title.title(message, subtitle));
            case EVENT, IMPORTANT -> player.sendMessage(message);
        }
    }

    /** Isolated so LoveNotify is only resolved inside the try/catch in {@link #send}. */
    static final class Gateway {
        private Gateway() {
        }

        static boolean notify(Player player, Component message, Component subtitle, Kind kind) {
            var service = LoveCore.service(LoveNotify.class);
            if (service.isEmpty()) return false;
            Component full = subtitle == null || Component.empty().equals(subtitle)
                    ? message : message.append(Component.space()).append(subtitle);
            service.get().notify(player, full, channelsFor(kind), kind == Kind.IMPORTANT);
            return true;
        }

        /** The title falls back to the action bar (not chat); LoveCore adds chat itself when needed. */
        static Set<Channel> channelsFor(Kind kind) {
            return switch (kind) {
                case ACTION_BAR -> Set.of(Channel.ACTION_BAR);
                case TITLE -> Set.of(Channel.TITLE, Channel.ACTION_BAR);
                case EVENT, IMPORTANT -> Set.of(Channel.ACTION_BAR, Channel.CHAT);
            };
        }
    }
}
