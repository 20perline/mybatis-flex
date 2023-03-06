/**
 * Copyright (c) 2022-2023, Mybatis-Flex (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mybatisflex.core.querywrapper;

import com.mybatisflex.core.exception.FlexExceptions;
import com.mybatisflex.core.table.TableDef;
import com.mybatisflex.core.util.ArrayUtil;
import com.mybatisflex.core.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Map;

public class QueryWrapper extends BaseQueryWrapper<QueryWrapper> {


    public static QueryWrapper create() {
        return new QueryWrapper();
    }

    public QueryWrapper select(QueryColumn... queryColumns) {
        for (QueryColumn column : queryColumns) {
            if (column != null) {
                addSelectColumn(column);
            }
        }
        return this;
    }


    public QueryWrapper from(TableDef tableDef) {
        return from(tableDef.getTableName());
    }


    public QueryWrapper from(String table) {
        return from(new QueryTable(table));
    }


    public QueryWrapper from(QueryTable table) {
        if (CollectionUtil.isEmpty(queryTables)) {
            queryTables = new ArrayList<>();
            queryTables.add(table);
        } else {
            boolean contains = false;
            for (QueryTable queryTable : queryTables) {
                if (queryTable.isSameTable(table)) {
                    contains = true;
                }
            }
            if (!contains) {
                queryTables.add(table);
            }
        }
        return this;
    }


    public QueryWrapper from(QueryWrapper queryWrapper) {
        return from(new SelectQueryTable(queryWrapper));
    }

    public QueryWrapper as(String alias) {
        if (CollectionUtil.isEmpty(queryTables)) {
            throw new IllegalArgumentException("query table must not be empty.");
        }
        if (queryTables.size() > 1) {
            throw FlexExceptions.wrap("QueryWrapper.as(...) only support 1 table");
        }
        queryTables.get(0).alias = alias;
        return this;
    }


    public QueryWrapper where(QueryCondition queryCondition) {
        this.setWhereQueryCondition(queryCondition);
        return this;
    }

    public QueryWrapper where(String sql) {
        this.setWhereQueryCondition(new StringQueryCondition(sql));
        return this;
    }


    public QueryWrapper where(String sql, Object... params) {
        this.setWhereQueryCondition(new StringQueryCondition(sql, params));
        return this;
    }


    public QueryWrapper where(Map<String, Object> whereConditions) {
        if (whereConditions != null) {
            whereConditions.forEach((s, o) -> and(QueryCondition.create(new QueryColumn(s), o)));
        }
        return this;
    }


    public QueryWrapper and(QueryCondition queryCondition) {
        return addWhereQueryCondition(queryCondition, SqlConnector.AND);
    }

    public QueryWrapper and(String sql) {
        this.addWhereQueryCondition(new StringQueryCondition(sql), SqlConnector.AND);
        return this;
    }


    public QueryWrapper and(String sql, Object... params) {
        this.addWhereQueryCondition(new StringQueryCondition(sql, params), SqlConnector.AND);
        return this;
    }


    public QueryWrapper or(QueryCondition queryCondition) {
        return addWhereQueryCondition(queryCondition, SqlConnector.OR);
    }


    public Joiner<QueryWrapper> leftJoin(String table) {
        return joining(Join.TYPE_LEFT, table, true);
    }


    public Joiner<QueryWrapper> leftJoinIf(String table, boolean condition) {
        return joining(Join.TYPE_LEFT, table, condition);
    }

    public Joiner<QueryWrapper> leftJoin(TableDef table) {
        return joining(Join.TYPE_LEFT, table.getTableName(), true);
    }


    public Joiner<QueryWrapper> leftJoinIf(TableDef table, boolean condition) {
        return joining(Join.TYPE_LEFT, table.getTableName(), condition);
    }

    public Joiner<QueryWrapper> leftJoin(QueryWrapper table) {
        return joining(Join.TYPE_LEFT, table, true);
    }

    public Joiner<QueryWrapper> leftJoinIf(QueryWrapper table, boolean condition) {
        return joining(Join.TYPE_LEFT, table, condition);
    }

    public Joiner<QueryWrapper> rightJoin(String table) {
        return joining(Join.TYPE_RIGHT, table, true);
    }

    public Joiner<QueryWrapper> rightJoinIf(String table, boolean condition) {
        return joining(Join.TYPE_RIGHT, table, condition);
    }

    public Joiner<QueryWrapper> rightJoin(QueryWrapper table) {
        return joining(Join.TYPE_RIGHT, table, true);
    }

    public Joiner<QueryWrapper> rightJoinIf(QueryWrapper table, boolean condition) {
        return joining(Join.TYPE_RIGHT, table, condition);
    }

    public Joiner<QueryWrapper> innerJoin(String table) {
        return joining(Join.TYPE_INNER, table, true);
    }

    public Joiner<QueryWrapper> innerJoinIf(String table, boolean condition) {
        return joining(Join.TYPE_INNER, table, condition);
    }

    public Joiner<QueryWrapper> innerJoin(TableDef table) {
        return innerJoinIf(table, true);
    }

    public Joiner<QueryWrapper> innerJoinIf(TableDef table, boolean condition) {
        return joining(Join.TYPE_INNER, table.getTableName(), condition);
    }

    public Joiner<QueryWrapper> innerJoin(QueryWrapper table) {
        return joining(Join.TYPE_INNER, table, true);
    }

    public Joiner<QueryWrapper> innerJoinIf(QueryWrapper table, boolean condition) {
        return joining(Join.TYPE_INNER, table, condition);
    }

    public Joiner<QueryWrapper> fullJoin(String table) {
        return joining(Join.TYPE_FULL, table, true);
    }

    public Joiner<QueryWrapper> fullJoinIf(String table, boolean condition) {
        return joining(Join.TYPE_FULL, table, condition);
    }

    public Joiner<QueryWrapper> fullJoin(QueryWrapper table) {
        return joining(Join.TYPE_FULL, table, true);
    }

    public Joiner<QueryWrapper> fullJoinIf(QueryWrapper table, boolean condition) {
        return joining(Join.TYPE_FULL, table, condition);
    }
    public Joiner<QueryWrapper> crossJoin(String table) {
        return joining(Join.TYPE_CROSS, table, true);
    }

    public Joiner<QueryWrapper> crossJoinIf(String table, boolean condition) {
        return joining(Join.TYPE_CROSS, table, condition);
    }

    public Joiner<QueryWrapper> crossJoin(QueryWrapper table) {
        return joining(Join.TYPE_CROSS, table, true);
    }

    public Joiner<QueryWrapper> crossJoinIf(QueryWrapper table, boolean condition) {
        return joining(Join.TYPE_CROSS, table, condition);
    }



    protected Joiner<QueryWrapper> joining(String type, String table, boolean condition) {
        Join join = new Join(type, table, condition);
        addJoinTable(join.getQueryTable());
        return new Joiner<>(AddJoin(join), join);
    }

    protected Joiner<QueryWrapper> joining(String type, QueryWrapper queryWrapper, boolean condition) {
        Join join = new Join(type, queryWrapper, condition);
        addJoinTable(join.getQueryTable());
        return new Joiner<>(AddJoin(join), join);
    }


    public QueryWrapper groupBy(String name) {
        addGroupByColumns(new QueryColumn(name));
        return this;
    }

    public QueryWrapper groupBy(String... names) {
        for (String name : names) {
            groupBy(name);
        }
        return this;
    }

    public QueryWrapper groupBy(QueryColumn column) {
        addGroupByColumns(column);
        return this;
    }

    public QueryWrapper groupBy(QueryColumn... columns) {
        for (QueryColumn column : columns) {
            groupBy(column);
        }
        return this;
    }


    public QueryWrapper having(QueryCondition queryCondition) {
        addHavingQueryCondition(queryCondition, SqlConnector.AND);
        return this;
    }

    public QueryWrapper orderBy(QueryOrderBy... orderBys) {
        for (QueryOrderBy queryOrderBy : orderBys) {
            addOrderBy(queryOrderBy);
        }
        return this;
    }

    public QueryWrapper orderBy(String... orderBys) {
        for (String queryOrderBy : orderBys) {
            addOrderBy(new StringQueryOrderBy(queryOrderBy));
        }
        return this;
    }


    public QueryWrapper limit(Integer rows) {
        setLimitRows(rows);
        return this;
    }

    public QueryWrapper offset(Integer offset) {
        setLimitOffset(offset);
        return this;
    }

    public QueryWrapper limit(Integer offset, Integer rows) {
        setLimitOffset(offset);
        setLimitRows(rows);
        return this;
    }

    public QueryWrapper datasource(String datasource) {
        setDatasource(datasource);
        return this;
    }

    /**
     * 获取 queryWrapper 的参数
     * 在构建 sql 的时候，需要保证 where 在 having 的前面
     */
    Object[] getValueArray() {
        Object[] whereValues = WrapperUtil.getValues(whereQueryCondition);
        Object[] havingValues = WrapperUtil.getValues(havingQueryCondition);
        return ArrayUtil.concat(whereValues, havingValues);
    }


}
