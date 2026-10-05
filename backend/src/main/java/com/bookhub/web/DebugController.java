package com.bookhub.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/connection")
    public Map<String, Object> connection() {
        Map<String, Object> result = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            result.put("url", conn.getMetaData().getURL());
            result.put("username", conn.getMetaData().getUserName());
            result.put("database", conn.getCatalog());
            result.put("product", conn.getMetaData().getDatabaseProductName());
            result.put("version", conn.getMetaData().getDatabaseProductVersion());
            result.put("driver", conn.getMetaData().getDriverName());

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM books")) {
                rs.next();
                result.put("booksCount", rs.getInt(1));
            }
        } catch (Exception e) {
            result.put("error", e.getClass().getName() + ": " + e.getMessage());
        }
        return result;
    }
}