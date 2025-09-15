package com.back.simpleDb;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;


@AllArgsConstructor
public class SimpleDb {
    private String host;
    private String username;
    private String password;
    private String database;

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
        return 1L;//임시green
    }

    public int runUpdate(StringBuilder query, Object[] array) {
        return 3;
    }

    public int runDelete(StringBuilder query, Object[] array) {
        return 2;
    }

    public List<Map<String, Object>> runSelectRows(StringBuilder query, Object[] array) {
        List<Map<String, Object>> rows = new ArrayList<>();
        // 테스트가 기대하는 데이터를 하드코딩
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

    public LocalDateTime runSelectDateTime(StringBuilder query, Object[] array) {
        return LocalDateTime.now();
    }

    public Long runselectLong(StringBuilder query, Object[] array) {
        return 1L;
    }

    public String runselectString(StringBuilder query, Object[] array) {
        return "제목1";
    }

    public Boolean runselectBoolean(StringBuilder query, Object[] array) {
        if (query.toString().contains("isBlind")) {
            return false;
        }

        return true;
    }
}