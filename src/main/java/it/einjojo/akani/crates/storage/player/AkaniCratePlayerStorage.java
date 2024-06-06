package it.einjojo.akani.crates.storage.player;

import it.einjojo.akani.core.api.AkaniCore;

import java.sql.Connection;
import java.sql.SQLException;

public class AkaniCratePlayerStorage extends AbstractSQLCratePlayerStorage {
    private final AkaniCore akaniCore;

    public AkaniCratePlayerStorage(AkaniCore akaniCore) {
        this.akaniCore = akaniCore;
    }

    @Override
    Connection connection() throws SQLException {
        return akaniCore.dataSourceProxy().getConnection();
    }
}
