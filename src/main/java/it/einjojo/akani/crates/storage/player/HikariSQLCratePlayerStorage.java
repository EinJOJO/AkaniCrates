package it.einjojo.akani.crates.storage.player;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariSQLCratePlayerStorage extends AbstractSQLCratePlayerStorage {
    private final HikariDataSource hikariDataSource;

    public HikariSQLCratePlayerStorage(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    Connection connection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
