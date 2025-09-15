package com.back.simpleDb;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Sql {
    private final SimpleDb simpleDb;
    private final StringBuilder query = new StringBuilder();
    private final List<Object> params = new ArrayList<>();

    public Sql append(String sqlPart) {
        query.append(" ").append(sqlPart);
        return this;
    }

    public Sql append(String sqlPart, Object... params) {
        query.append(" ").append(sqlPart);
        for (Object param : params) {
            this.params.add(param);
        }
        return this;
    }

    public long insert() {
        return simpleDb.runInsert(query, params.toArray());
    }

    public int update() {
        return simpleDb.runUpdate(query, params.toArray());
    }

    public int delete() { return simpleDb.runDelete(query, params.toArray()); }

    public List<Map<String, Object>> selectRows() { return simpleDb.runSelectRows(query, params.toArray());
    }

    public Map<String, Object> selectRow() { return simpleDb.runSelectRow(query, params.toArray());
    }

    public LocalDateTime selectDatetime() { return simpleDb.runSelectDateTime(query, params.toArray());
    }

    public Long selectLong() { return simpleDb.runselectLong(query, params.toArray());
    }

    public String selectString() { return simpleDb.runselectString(query, params.toArray());
    }

    public Boolean selectBoolean() { return simpleDb.runselectBoolean(query, params.toArray());
    }
}
