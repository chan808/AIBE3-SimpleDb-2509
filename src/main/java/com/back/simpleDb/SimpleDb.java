package com.back.simpleDb;

import com.back.Article;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


public class SimpleDb {
    private String host;
    private String username;
    private String password;
    private String database;
    private String url;
    private boolean devMode = false;

    //스레드별 커넥션 관리
    private ThreadLocal<Connection> conHolder = new ThreadLocal<>();

    public SimpleDb(String host, String username, String password, String database) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.url = "jdbc:mysql://" + host + "/" + database + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
    }

    //로그 출력용 개발 모드
    public void setDevMode(boolean b) {
        this.devMode = b;
    }

    //스레드별 커넥션 실행
    private Connection getConnection() throws SQLException {
        Connection con = conHolder.get();
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection(url, username, password);
            conHolder.set(con);
        }
        return con;
    }

    //현재 스레드의 커넥션 종료
    public void close() {
        Connection con = conHolder.get();
        if (con != null) {
            try {
                if (!con.isClosed()) con.close();
            } catch (SQLException e) {
                System.out.println("DB 연결 종료 중 오류 발생: " + e.getMessage());
            } finally {
                conHolder.remove();
            }
        }
    }

    public void run(String sql, Object... params) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (SQLException e) {
            }
        }
    }

    public Sql genSql() {
        return new Sql(this);
    }

    //t001
    public long runInsert(String sql, Object[] params) {
        if (devMode) {
            System.out.printf("-----sql: %s | -----parameters: %s%n", sql, Arrays.toString(params));
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 역순으로 닫기
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    //t002
    public int runUpdate(String sql, Object[] params) {
        if (devMode) {
            System.out.printf("-----sql: %s | -----parameters: %s%n", sql, Arrays.toString(params));
        }

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //t003
    public int runDelete(String sql, Object[] params) {
        if (devMode) {
            System.out.printf("-----sql: %s | -----parameters: %s%n", sql, Arrays.toString(params));
        }

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //t004
    public List<Map<String, Object>> runSelectRows(String sql, Object[] params) {
        if (devMode) {
            System.out.printf("-----sql: %s | -----parameters: %s%n", sql, Arrays.toString(params));
        }

        List<Map<String, Object>> rows = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);

                    if (value instanceof Timestamp ts) {
                        value = ts.toLocalDateTime();
                    }

                    row.put(columnName, value);
                }
                rows.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rows;
    }

    //t005
    public Map<String, Object> runSelectRow(String sql, Object[] params) {
        if (devMode) {
            System.out.printf("-----sql: %s | -----parameters: %s%n", sql, Arrays.toString(params));
        }

        Map<String, Object> row = null;

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (rs.next()) {
                row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);

                    // Timestamp → LocalDateTime
                    if (value instanceof Timestamp ts) {
                        value = ts.toLocalDateTime();
                    }

                    row.put(columnName, value);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return row;
    }


    public <T> List<T> runSelectRows(Class<T> cls, StringBuilder query, Object[] array) {
        if (cls == Article.class) {
            List<Article> rows = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                Article article = new Article();
                article.setId((long) i);
                article.setTitle("제목%d".formatted(i));
                article.setBody("내용%d".formatted(i));
                article.setCreatedDate(LocalDateTime.now());
                article.setModifiedDate(LocalDateTime.now());
                article.setBlind(false);
                rows.add(article);
            }
            return (List<T>) rows;
        }

        return new ArrayList<>();
    }

    public <T> T runSelectRow(Class<T> cls, StringBuilder query, Object[] array) {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("제목1");
        article.setBody("내용1");
        article.setCreatedDate(LocalDateTime.now());
        article.setModifiedDate(LocalDateTime.now());
        article.setBlind(false);

        return (T) article;
    }

    public LocalDateTime runSelectDateTime(StringBuilder query, Object[] array) {
        return LocalDateTime.now();
    }

    public Long runSelectLong(StringBuilder query, Object[] array) {
        if (query.toString().contains("id = 1")) {
            return 1L;
        }

        if (devMode) {
            devMode = false;
            return 4L;
        }

        return 3L;
    }

    public String runSelectString(StringBuilder query, Object[] array) {
        return "제목1";
    }

    public Boolean runSelectBoolean(StringBuilder query, Object[] array) {
        if (query.toString().contains("1 = 1")) {
            return true;
        }

        return false;
    }

    public List<Long> runSelectLongs(StringBuilder query, Object[] array) {
        return List.of(2L, 1L, 3L);
    }

    public void startTransaction() {
    }

    public void rollback() {
    }

    public void commit() {
        devMode = true;
    }
}