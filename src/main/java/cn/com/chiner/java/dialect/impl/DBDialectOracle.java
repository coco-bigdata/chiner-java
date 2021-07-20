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
package cn.com.chiner.java.dialect.impl;

import cn.com.chiner.java.command.kit.ConnParseKit;
import cn.com.chiner.java.dialect.DBDialect;
import cn.com.chiner.java.model.ColumnField;
import cn.fisok.raw.kit.StringKit;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc : ORACLE数据库方言
 */
public class DBDialectOracle extends DBDialect {

    public String getSchemaPattern(Connection conn) throws SQLException {
        String schemaPattern = conn.getMetaData().getUserName().toUpperCase();
        if (StringKit.isNotBlank(schemaPattern)) {
            schemaPattern = schemaPattern.toUpperCase();
        }
        return schemaPattern;
    }

    public void fillColumnField(ColumnField field, Connection conn, ResultSet rs, Set<String> pkSet) throws SQLException {
        String colName = rs.getString("COLUMN_NAME");
        String remarks = StringKit.trim(rs.getString("REMARKS"));
        String typeName = rs.getString("TYPE_NAME");
        int dataType = rs.getInt("DATA_TYPE");
        int columnSize = rs.getInt("COLUMN_SIZE");
        Integer decimalDigits = rs.getInt("DECIMAL_DIGITS");
        String defaultValue = rs.getString("COLUMN_DEF");
        String isNullable = rs.getString("IS_NULLABLE");
        String isAutoincrement = "NO";
        defaultValue = parseDefaultValue(defaultValue);

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

}
