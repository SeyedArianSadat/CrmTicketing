package com.company.crmticketing.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class DatabaseConnectionTestRunner implements CommandLineRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        log.info("========================================");
        log.info("🧪 Testing Database Connection");
        log.info("========================================");
        
        testConnection();
        testQuery();
        
        log.info("========================================");
        log.info("✅ Database connection test completed");
        log.info("========================================");
    }
    
    private void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("✅ Successfully connected to database!");
            log.info("   Database: {}", connection.getMetaData().getDatabaseProductName());
            log.info("   Version: {}", connection.getMetaData().getDatabaseProductVersion());
            log.info("   URL: {}", connection.getMetaData().getURL());
        } catch (SQLException e) {
            log.error("❌ Failed to connect to database: {}", e.getMessage(), e);
        }
    }
    
    private void testQuery() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            log.info("✅ Test query 'SELECT 1' returned: {}", result);
        } catch (Exception e) {
            log.error("❌ Test query failed: {}", e.getMessage());
        }
    }
}