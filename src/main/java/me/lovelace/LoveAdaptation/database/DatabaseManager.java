package me.lovelace.LoveAdaptation.database;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.models.AdaptationData;
import me.lovelace.LoveAdaptation.models.AdaptationType;
import me.lovelace.LoveAdaptation.models.PlayerData;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final LoveAdaptation plugin;
    private File dbFile;
    private String connectionUrl;

    public DatabaseManager(LoveAdaptation plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        String path = plugin.getConfig().getString("database.file", "data.db");
        if (path.startsWith("plugins/LoveAdaptation/") || path.startsWith("plugins\\LoveAdaptation\\")) {
            path = path.substring("plugins/LoveAdaptation/".length());
        }
        dbFile = new File(plugin.getDataFolder(), path);
        if (dbFile.getParentFile() != null && !dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }

        connectionUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Built-in JDBC driver in Paper/Spigot
        }

        createTables();
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(connectionUrl);
        // Multiple async tasks (periodic auto-save, per-player quit save, history logging) can
        // each open their own connection concurrently; SQLite only allows one writer at a time,
        // so without a busy timeout a second writer fails immediately with SQLITE_BUSY instead
        // of waiting briefly for the lock to clear.
        try (Statement pragmaStmt = conn.createStatement()) {
            pragmaStmt.execute("PRAGMA busy_timeout = 5000;");
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to set busy_timeout on database connection: " + e.getMessage());
        }
        return conn;
    }

    private void createTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS player_adaptations (" +
                    "uuid TEXT PRIMARY KEY," +
                    "current_adaptation VARCHAR(50)," +
                    "current_progress_percent REAL DEFAULT 0," +
                    "is_mastery_unlocked INTEGER DEFAULT 0," +
                    "mastery_unlock_time INTEGER," +
                    "last_condition_check INTEGER," +
                    "last_degradation_check INTEGER," +
                    "created_at INTEGER," +
                    "updated_at INTEGER" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS adaptation_progress (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid TEXT NOT NULL," +
                    "adaptation_name VARCHAR(50)," +
                    "progress_percent REAL DEFAULT 0," +
                    "progress_type VARCHAR(50)," +
                    "progress_value INTEGER DEFAULT 0," +
                    "is_unlocked INTEGER DEFAULT 0," +
                    "is_active INTEGER DEFAULT 0," +
                    "unlocked_at INTEGER," +
                    "activated_at INTEGER," +
                    "deactivated_at INTEGER," +
                    "mastery_benefits_applied INTEGER DEFAULT 0," +
                    "created_at INTEGER," +
                    "updated_at INTEGER," +
                    "UNIQUE(uuid, adaptation_name)" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS adaptation_history (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid TEXT NOT NULL," +
                    "adaptation_name VARCHAR(50)," +
                    "event_type VARCHAR(50)," +
                    "event_percent REAL," +
                    "event_timestamp INTEGER," +
                    "notes TEXT" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS potion_effects (" +
                    "uuid TEXT PRIMARY KEY," +
                    "potion_type VARCHAR(20)," +
                    "applied_at INTEGER," +
                    "expires_at INTEGER" +
                    ");");

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create database tables: " + e.getMessage());
        }
    }

    public CompletableFuture<PlayerData> loadPlayerData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerData data = new PlayerData(uuid);
            long now = System.currentTimeMillis();

            try (Connection conn = getConnection()) {
                // Load overall player adaptation
                String queryPlayer = "SELECT * FROM player_adaptations WHERE uuid = ?";
                try (PreparedStatement stmt = conn.prepareStatement(queryPlayer)) {
                    stmt.setString(1, uuid.toString());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String current = rs.getString("current_adaptation");
                        data.setCurrentAdaptation(AdaptationType.fromString(current));
                        data.setLastConditionCheck(rs.getLong("last_condition_check"));
                        data.setLastDegradationCheck(rs.getLong("last_degradation_check"));
                    } else {
                        // Insert new player
                        String insertPlayer = "INSERT INTO player_adaptations (uuid, current_adaptation, created_at, updated_at) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement inst = conn.prepareStatement(insertPlayer)) {
                            inst.setString(1, uuid.toString());
                            inst.setString(2, AdaptationType.BASE.name());
                            inst.setLong(3, now);
                            inst.setLong(4, now);
                            inst.executeUpdate();
                        }
                    }
                }

                // Load adaptation progress entries
                String queryProgress = "SELECT * FROM adaptation_progress WHERE uuid = ?";
                try (PreparedStatement stmt = conn.prepareStatement(queryProgress)) {
                    stmt.setString(1, uuid.toString());
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String adaptName = rs.getString("adaptation_name");
                        AdaptationType type = AdaptationType.fromString(adaptName);
                        if (type != AdaptationType.BASE) {
                            AdaptationData adaptData = data.getAdaptationData(type);
                            adaptData.setProgressPercent(rs.getDouble("progress_percent"));
                            adaptData.setProgressValue(rs.getLong("progress_value"));
                            adaptData.setUnlocked(rs.getInt("is_unlocked") == 1);
                            adaptData.setActive(rs.getInt("is_active") == 1);
                            adaptData.setUnlockedAt(rs.getLong("unlocked_at"));
                            adaptData.setActivatedAt(rs.getLong("activated_at"));
                            adaptData.setDeactivatedAt(rs.getLong("deactivated_at"));
                        }
                    }
                }

                // Load active potion effects
                String queryPotion = "SELECT * FROM potion_effects WHERE uuid = ?";
                try (PreparedStatement stmt = conn.prepareStatement(queryPotion)) {
                    stmt.setString(1, uuid.toString());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        long expires = rs.getLong("expires_at");
                        if (expires > now) {
                            data.setPotionExpiresAt(expires);
                            data.setActivePotionType(rs.getString("potion_type"));
                        }
                    }
                }

            } catch (SQLException e) {
                plugin.getLogger().severe("Error loading player data for " + uuid + ": " + e.getMessage());
            }

            return data;
        });
    }

    public void savePlayerData(PlayerData data) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> savePlayerDataSync(data));
    }

    public void savePlayerDataSync(PlayerData data) {
        long now = System.currentTimeMillis();
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Update player_adaptations
            String sqlPlayer = "INSERT INTO player_adaptations (uuid, current_adaptation, current_progress_percent, created_at, updated_at) "
                    +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT(uuid) DO UPDATE SET " +
                    "current_adaptation = excluded.current_adaptation, " +
                    "current_progress_percent = excluded.current_progress_percent, " +
                    "updated_at = excluded.updated_at;";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPlayer)) {
                stmt.setString(1, data.getUuid().toString());
                stmt.setString(2, data.getCurrentAdaptation().name());
                AdaptationData currentAdaptData = data.getAdaptationData(data.getCurrentAdaptation());
                stmt.setDouble(3, currentAdaptData != null ? currentAdaptData.getProgressPercent() : 0.0);
                stmt.setLong(4, now);
                stmt.setLong(5, now);
                stmt.executeUpdate();
            }

            // Update adaptation_progress for each type
            String sqlProgress = "INSERT INTO adaptation_progress " +
                    "(uuid, adaptation_name, progress_percent, progress_type, progress_value, is_unlocked, is_active, unlocked_at, activated_at, deactivated_at, created_at, updated_at) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT(uuid, adaptation_name) DO UPDATE SET " +
                    "progress_percent = excluded.progress_percent, " +
                    "progress_value = excluded.progress_value, " +
                    "is_unlocked = excluded.is_unlocked, " +
                    "is_active = excluded.is_active, " +
                    "unlocked_at = excluded.unlocked_at, " +
                    "activated_at = excluded.activated_at, " +
                    "deactivated_at = excluded.deactivated_at, " +
                    "updated_at = excluded.updated_at;";

            try (PreparedStatement stmt = conn.prepareStatement(sqlProgress)) {
                for (Map.Entry<AdaptationType, AdaptationData> entry : data.getAdaptationProgressMap().entrySet()) {
                    AdaptationType type = entry.getKey();
                    AdaptationData adaptData = entry.getValue();

                    stmt.setString(1, data.getUuid().toString());
                    stmt.setString(2, type.name());
                    stmt.setDouble(3, adaptData.getProgressPercent());
                    stmt.setString(4, type.getProgressMetric());
                    stmt.setLong(5, adaptData.getProgressValue());
                    stmt.setInt(6, adaptData.isUnlocked() ? 1 : 0);
                    stmt.setInt(7, adaptData.isActive() ? 1 : 0);
                    stmt.setLong(8, adaptData.getUnlockedAt());
                    stmt.setLong(9, adaptData.getActivatedAt());
                    stmt.setLong(10, adaptData.getDeactivatedAt());
                    stmt.setLong(11, now);
                    stmt.setLong(12, now);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            // Update potion effects
            if (data.isPotionActive()) {
                String sqlPotion = "INSERT INTO potion_effects (uuid, potion_type, applied_at, expires_at) VALUES (?, ?, ?, ?) "
                        +
                        "ON CONFLICT(uuid) DO UPDATE SET potion_type = excluded.potion_type, expires_at = excluded.expires_at;";
                try (PreparedStatement stmt = conn.prepareStatement(sqlPotion)) {
                    stmt.setString(1, data.getUuid().toString());
                    stmt.setString(2, data.getActivePotionType());
                    stmt.setLong(3, now);
                    stmt.setLong(4, data.getPotionExpiresAt());
                    stmt.executeUpdate();
                }
            } else {
                String sqlDeletePotion = "DELETE FROM potion_effects WHERE uuid = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlDeletePotion)) {
                    stmt.setString(1, data.getUuid().toString());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save player data for " + data.getUuid() + ": " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    plugin.getLogger().severe("Failed to roll back transaction for " + data.getUuid() + ": " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    plugin.getLogger().warning("Failed to close database connection for " + data.getUuid() + ": " + closeEx.getMessage());
                }
            }
        }
    }

    public void logHistory(UUID uuid, String adaptationName, String eventType, double percent, String notes) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "INSERT INTO adaptation_history (uuid, adaptation_name, event_type, event_percent, event_timestamp, notes) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, adaptationName);
                stmt.setString(3, eventType);
                stmt.setDouble(4, percent);
                stmt.setLong(5, System.currentTimeMillis());
                stmt.setString(6, notes);
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().warning("Failed to log adaptation history: " + e.getMessage());
            }
        });
    }
}
