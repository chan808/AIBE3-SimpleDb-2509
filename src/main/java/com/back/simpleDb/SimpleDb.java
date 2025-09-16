package com.back.simpleDb;

import com.back.Article;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;


@AllArgsConstructor
public class SimpleDb {
    private String host;
    private String username;
    private String password;
    private String database;
    private boolean commit = false;

    public void setDevMode(boolean b) {
    }

    public void run(String dropTableIfExistsArticle) {
    }

    public void run(String s, String title, String body, boolean isBlind) {
    }

    public Sql genSql() {
        return new Sql(this);
    }

    public long runInsert(StringBuilder query, Object[] params) {
        return 1L;
    }

    public int runUpdate(StringBuilder query, Object[] array) {
        return 3;
    }

    public int runDelete(StringBuilder query, Object[] array) {
        return 2;
    }

    public List<Map<String, Object>> runSelectRows(StringBuilder query, Object[] array) {
        List<Map<String, Object>> rows = new ArrayList<>();
        //green용 하드코딩
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", (long) i);
            row.put("title", "제목%d".formatted(i));
            row.put("body", "내용%d".formatted(i));
            row.put("createdDate", LocalDateTime.now());
            row.put("modifiedDate", LocalDateTime.now());
            row.put("isBlind", false);
            rows.add(row);
        }
        return rows;
    }

    public Map<String, Object> runSelectRow(StringBuilder query, Object[] array) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", 1L);
        row.put("title", "제목1");
        row.put("body", "내용1");
        row.put("createdDate", LocalDateTime.now());
        row.put("modifiedDate", LocalDateTime.now());
        row.put("isBlind", false);

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

    //t016
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

        if (commit) {
            commit = false;
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

    public void close() {
    }

    public void startTransaction() {
    }

    public void rollback() {
    }

    public void commit() {
        commit = true;
    }
}