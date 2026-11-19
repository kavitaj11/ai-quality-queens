package com.k11.pwj.db;

import com.k11.pwj.config.Config;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Read-only access to the practice shop's database for verification. Every query uses a
 * PreparedStatement with parameters (never string concatenation), and money is converted
 * from integer cents to BigDecimal with an exact scale of 2.
 */
public class ShopDb {

    public record OrderRow(String orderId, String username, String status, BigDecimal total) {
    }

    public record OrderLineRow(int lineNo, int productId, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }

    private final String url;

    public ShopDb() {
        if (!Config.dbPath().toFile().exists()) {
            throw new IllegalStateException("Shop database not found at " + Config.dbPath()
                    + ". Start the practice shop first, or pass -DdbPath=<path to shop.db>.");
        }
        this.url = "jdbc:sqlite:" + Config.dbPath();
    }

    public Optional<OrderRow> findOrder(String orderId) {
        return queryOne("SELECT order_id, username, status, total_cents FROM orders WHERE order_id = ?",
                rs -> new OrderRow(rs.getString(1), rs.getString(2), rs.getString(3), money(rs.getLong(4))), orderId);
    }

    public List<OrderLineRow> orderLines(String orderId) {
        String sql = "SELECT line_no, product_id, quantity, unit_price_cents, line_total_cents "
                + "FROM order_lines WHERE order_id = ? ORDER BY line_no";
        List<OrderLineRow> rows = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new OrderLineRow(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                            money(rs.getLong(4)), money(rs.getLong(5))));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + sql, e);
        }
        return rows;
    }

    /** Number of audit entries for this order and action, e.g. ("ORD-...", "CREATED"). */
    public int auditCount(String orderId, String action) {
        return queryOne("SELECT COUNT(*) FROM audit_log WHERE entity_type = 'ORDER' AND entity_id = ? AND action = ?",
                rs -> rs.getInt(1), orderId, action).orElse(0);
    }

    /** Reconciliation: the order total must equal the sum of its lines. Returns the difference (0 when correct). */
    public BigDecimal totalMinusLines(String orderId) {
        String sql = "SELECT o.total_cents - COALESCE(SUM(l.line_total_cents), 0) "
                + "FROM orders o LEFT JOIN order_lines l ON l.order_id = o.order_id "
                + "WHERE o.order_id = ? GROUP BY o.order_id, o.total_cents";
        return queryOne(sql, rs -> money(rs.getLong(1)), orderId)
                .orElseThrow(() -> new AssertionError("Order " + orderId + " not found"));
    }

    public BigDecimal productPrice(int productId) {
        String sql = "SELECT price_cents FROM products WHERE product_id = ?";
        return queryOne(sql, rs -> money(rs.getLong(1)), productId)
                .orElseThrow(() -> new AssertionError("Product " + productId + " not found"));
    }

    private static BigDecimal money(long cents) {
        return BigDecimal.valueOf(cents, 2);
    }

    @FunctionalInterface
    private interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    private <T> Optional<T> queryOne(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.ofNullable(mapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + sql, e);
        }
    }
}
