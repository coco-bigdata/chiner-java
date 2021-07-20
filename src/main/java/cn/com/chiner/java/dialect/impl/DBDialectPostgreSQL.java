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

import cn.com.chiner.java.dialect.DBDialect;
import cn.com.chiner.java.model.TableEntity;
import cn.fisok.raw.kit.JdbcKit;

import java.sql.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/7/20
 * @desc : PostgreSQL方言
 */
public class DBDialectPostgreSQL extends DBDialect {
    @Override
    public TableEntity createTableEntity(Connection conn, DatabaseMetaData meta, String tableName) throws SQLException {
        ResultSet rs = null;
        Statement stmt = null;
        try{
            rs = meta.getTables(null, getSchemaPattern(conn), tableName.toLowerCase(), new String[]{"TABLE"});
            stmt = rs.getStatement();
            if(rs.next()) {
                TableEntity tableEntity = createTableEntity(conn, rs);
                fillTableEntity(tableEntity,conn);
                return tableEntity;
            }
        }catch (SQLException e){
            throw e;
        }finally {
            JdbcKit.close(stmt);
            JdbcKit.close(rs);
        }
        return null;
    }
}