package dk.MyTunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

public class ConnectionManager {
    private final SQLServerDataSource ds;

    public ConnectionManager() {
        ds = new SQLServerDataSource();
        ds.setDatabaseName("GroupCMyTunes");
        ds.setUser("CSe2023b_e_13");
        ds.setPassword("CSe2023bE13#23");
        ds.setServerName("10.176.111.34");
        ds.setPortNumber(1433);
        ds.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException {
        return ds.getConnection();
    }
}