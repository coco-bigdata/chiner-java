/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.chiner.java.dialect;

import cn.fisok.raw.kit.JdbcKit;
import cn.fisok.raw.kit.StringKit;
import cn.com.chiner.java.command.kit.ConnParseKit;
import cn.com.chiner.java.model.ColumnField;
import cn.com.chiner.java.model.TableEntity;
import cn.com.chiner.java.model.TableIndex;
import cn.com.chiner.java.model.TableIndexColumnField;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc : 数据库方言抽象类
 */
public class DBDialect {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取数据库SchemaPattern
     * @param conn
     * @return
     * @throws SQLException
     */
    public String getSchemaPattern(Connection conn) throws SQLException{
        return null;
    };

    /**
     * 获取数据库TableNamePattern
     * @param conn
     * @return
     * @throws SQLException
     */
    public String getTableNamePattern(Connection conn) throws SQLException{
        return null;
    }


    /**
     * 将resultset,主键的resultset装到一个二元组中，并返回
     * @param conn
     * @param tableName
     * @return
     * @throws SQLException
     */
    public Pair getColumnAndPrimaryKeyResultSetPair(Connection conn,String tableName) throws SQLException {
        DatabaseMetaData connMetaData = conn.getMetaData();
        String schema = getSchemaPattern(conn);

        ResultSet rs = connMetaData.getColumns(conn.getCatalog(), schema,tableName, "%");
        ResultSet pkRs = connMetaData.getPrimaryKeys(conn.getCatalog(),schema,tableName);

        Pair<ResultSet,ResultSet> pair = Pair.of(rs,pkRs);

        return pair;
    }

    /**
     * 根据结果集，创建表实体对象，仅填充表名，中文名，注释信息
     * @param connection
     * @param rs
     * @return
     */
    public TableEntity createTableEntity(Connection connection,ResultSet rs) throws SQLException {
        TableEntity entity = new TableEntity();
        fillTableEntityNoColumn(entity, connection,rs);
        if (StringKit.isNotBlank(entity.getDefKey())) {
            return entity;
        } else {
            return null;
        }
    }

    /**
     * 传入一个空对象，填充表名，中文名，注释信息
     * @param tableEntity
     * @param rs
     * @throws SQLException
     */
    public void fillTableEntityNoColumn(TableEntity tableEntity, Connection connection,ResultSet rs) throws SQLException {
        String tableName = rs.getString("TABLE_NAME");
        String remarks = StringKit.trim(rs.getString("REMARKS"));
        String defKey = tableName;
        String defName = remarks;
        String comment = "";

        //如果remark中有分号等分割符，则默认之后的就是注释说明文字
        if(StringKit.isNotBlank(remarks)){
            Pair<String, String> pair = ConnParseKit.parseNameAndComment(remarks);
            defName = pair.getLeft();
            comment = pair.getRight();
        }
        tableEntity.setDefKey(defKey);
        tableEntity.setDefName(defName);
        tableEntity.setComment(comment);
    }

    /**
     * 传入表名，中文名，注释信息，获取字段明细，索引信息
     * @param tableEntity
     * @param conn
     */
    public void fillTableEntity(TableEntity tableEntity, Connection conn) throws SQLException {
        String tableName = tableEntity.getDefKey();

        ResultSet rs = null;
        Statement stmt = null;
        ResultSet pkRs = null;
        Statement pkStmt = null;

        try {
            Pair<ResultSet,ResultSet> pair = getColumnAndPrimaryKeyResultSetPair(conn,tableName);
            rs = pair.getLeft();
            pkRs = pair.getRight();
            stmt = rs.getStatement();
            pkStmt = pkRs.getStatement();
            Set<String> pkSet = new HashSet<String>();
            while(pkRs.next()){
                String columnName = pkRs.getString("COLUMN_NAME");
                pkSet.add(columnName);
            }

            while(rs.next()){
                ColumnField field = new ColumnField();
                fillColumnField(field,conn,rs,pkSet);
                tableEntity.getFields().add(field);
            }
            fillTableIndexes(tableEntity,conn);
        } catch (SQLException e) {
            logger.error("读取数据表"+tableName+"的字段明细出错",e);
            throw new RuntimeException("读取数据表"+tableName+"的字段明细出错",e);
        } finally {
            JdbcKit.close(stmt);
            JdbcKit.close(rs);
            JdbcKit.close(pkStmt);
            JdbcKit.close(pkRs);
        }
    }

    /**
     * 填充列数据
     * @param field
     * @param rs
     * @throws SQLException
     */
    public void fillColumnField(ColumnField field,Connection conn, ResultSet rs, Set<String> pkSet) throws SQLException {

        String colName = rs.getString("COLUMN_NAME");
        String remarks = StringKit.trim(rs.getString("REMARKS"));
        String typeName = rs.getString("TYPE_NAME");
        int dataType = rs.getInt("DATA_TYPE");
        int columnSize = rs.getInt("COLUMN_SIZE");
        Integer decimalDigits = rs.getInt("DECIMAL_DIGITS");
        String isNullable = rs.getString("IS_NULLABLE");
        String isAutoincrement = rs.getString("IS_AUTOINCREMENT");
        String defaultValue = "";

        String label = remarks;
        String comment = null;
        if(StringKit.isNotBlank(remarks)){
            Pair<String,String> columnRemarks = ConnParseKit.parseNameAndComment(remarks);
            label = columnRemarks.getLeft();
            comment = columnRemarks.getRight();
        }

        field.setDefKey(colName);
        field.setDefName(label);
        field.setComment(comment);
        field.setType(typeName);
        field.setLen(columnSize);
        if(decimalDigits<=0){
            field.setScale(null);
        }else{
            field.setScale(decimalDigits);
        }

        field.setPrimaryKey(pkSet.contains(colName));
        field.setNotNull(!"YES".equalsIgnoreCase(isNullable));
        field.setAutoIncrement(!"NO".equalsIgnoreCase(isAutoincrement));
        field.setDefaultValue(defaultValue);
    }

    /**
     * 填充数据表的索引
     * @param tableEntity
     * @param conn
     * @throws SQLException
     */
    public void fillTableIndexes(TableEntity tableEntity, Connection conn) throws SQLException {
        String table = tableEntity.getDefKey();
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet rs = dbMeta.getIndexInfo(null,null,table,false,false);

        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            String indexName = rs.getString("INDEX_NAME");
            String columnName = rs.getString("COLUMN_NAME");
            String nonUnique = rs.getString("NON_UNIQUE");
            String ascOrDesc = rs.getString("ASC_OR_DESC");

            if(!table.equalsIgnoreCase(tableName)){
                continue;
            }
            if(StringKit.isBlank(indexName)){
                continue;
            }
            if(StringKit.isBlank(columnName)){
                continue;
            }

            TableIndex index = tableEntity.lookupIndex(indexName);
            if(index == null){
                index = new TableIndex();
                index.setDefKey(indexName);
                index.setUnique(!"1".equalsIgnoreCase(nonUnique));
                tableEntity.getIndexes().add(index);
            }

            TableIndexColumnField ticf = index.lookupField(columnName);
            if(ticf != null){
                continue;
            }
            ticf = new TableIndexColumnField();
            ticf.setFieldDefKey(columnName);
            ticf.setAscOrDesc(ascOrDesc);
            index.getFields().add(ticf);

        }

        JdbcKit.close(rs.getStatement());
        JdbcKit.close(rs);
    }

    /**
     * 取所有的数据表清单
     * @param conn
     * @return
     */
    public List<TableEntity> getAllTables(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();

        String schemaPattern = getSchemaPattern(conn);
        String tableNamePattern = getTableNamePattern(conn);
        String catalog = conn.getCatalog();

        ResultSet rs = meta.getTables(catalog, schemaPattern, tableNamePattern, new String[]{"TABLE"});
        List<TableEntity> tableEntities = new ArrayList<TableEntity>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            /**
             *  SQL Server系统保留表
             *  trace_xe_action_map,trace_xe_event_map
             */
            if (!tableName.equalsIgnoreCase("PDMAN_DB_VERSION")
                    && !tableName.equalsIgnoreCase("trace_xe_action_map")
                    && !tableName.equalsIgnoreCase("trace_xe_event_map")){
                TableEntity entity = createTableEntity(conn,rs);
                if(entity != null){
                    tableEntities.add(entity);
                }
            }else{
                continue;
            }
        }
        return tableEntities;
    }
}
