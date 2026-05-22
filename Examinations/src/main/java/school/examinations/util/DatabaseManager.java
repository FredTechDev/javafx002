package school.examinations.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/examinations");
            config.setUsername("root");
            config.setPassword("Root@2026");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000); // 30 minutes
            config.setConnectionTimeout(30000);
            
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize HikariCP connection pool.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
