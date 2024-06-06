package it.einjojo.akani.crates.storage.player;

import it.einjojo.akani.crates.player.CratePlayer;
import it.einjojo.akani.crates.player.CratePlayerImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractSQLCratePlayerStorage implements CratePlayerStorage {

    private static final Logger log = LoggerFactory.getLogger(AbstractSQLCratePlayerStorage.class);

    abstract Connection connection() throws SQLException;

    @Override
    public void init() {
        try (Connection connection = connection(); Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE crates_players (
                        player_uuid VARCHAR(36) NOT NULL,
                        crate_id INT NOT NULL,
                        amount INT NOT NULL DEFAULT 0,
                        PRIMARY KEY (player_uuid, crate_id)
                    );
                    """);
            log.info("Created table crates_players");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull CratePlayerImpl loadPlayer(@NotNull UUID uuid) {
        String sql = "SELECT * FROM crates_players WHERE player_uuid = ?";
        try (Connection connection = connection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Integer> crates = new HashMap<>();
                while (rs.next()) {
                    crates.put(rs.getString("crate_id"), rs.getInt("keys"));
                }
                log.debug("Loaded player {} with {} crates-types", uuid, crates.size());
                return new CratePlayerImpl(uuid, crates);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePlayer(@NotNull CratePlayer player) {
        String sql = """
                INSERT INTO crates_players (player_uuid, crate_id, amount)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE amount = ?
                """;
        if (!(player instanceof CratePlayerImpl impl)) {
            throw new IllegalArgumentException("Player must be an instance of CratePlayerImpl");
        }
        if (!impl.hasChanged()) {
            log.debug("Player {} has no changes to save", player.playerUuid());
            return;
        };
        try (Connection connection = connection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            for (String crateId : impl.changedSet()) {
                int amount = impl.keys(crateId);
                ps.setString(1, player.playerUuid().toString());
                ps.setString(2, crateId);
                ps.setInt(3, amount);
                ps.setInt(4, amount);
                ps.addBatch();
                log.debug("Saving player {} crate {} with amount {}", player.playerUuid(), crateId, amount);
                impl.changedSet().remove(crateId);
            }
            ps.executeBatch();
            log.info("Saved player {}", player.playerUuid());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
