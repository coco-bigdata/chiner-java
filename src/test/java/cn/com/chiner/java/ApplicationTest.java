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
package cn.com.chiner.java;

import org.junit.Test;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/12
 * @desc :
 */
public class ApplicationTest {

    @Test
    public void pingDriverLoadTest(){
        String[] args =  new String[]{
                "PingLoadDriverClass",                      //执行什么命令
                "driver_class_name=com.mysql.cj.jdbc.Driver",
                "url=jdbc:mysql://127.0.0.1:3306/vbcms?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                "username=fisok",
                "password=fisok2020",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/pdc-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void pingDB2DriverLoadTest(){
        String[] args =  new String[]{
                "PingLoadDriverClass",                      //执行什么命令
                "driver_class_name=com.ibm.db2.jcc.DB2Driver",
                "url=jdbc:db2://47.107.253.194:50000/ams5:progressiveStreaming=2;",
                "username=db2inst1",
                "password=db2inst1",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/pdc-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void listTableTest(){
        String[] args =  new String[]{
                "DBReverseGetAllTablesList",            //执行什么命令
                "driver_class_name=com.mysql.cj.jdbc.Driver",
                "url=jdbc:mysql://g5.mtain.top:51236/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                "username=root",
                "password=passwd",
//                "driver_class_name=com.mysql.cj.jdbc.Driver",
//                "url=jdbc:mysql://127.0.0.1:3306/vbcms?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
//                "username=fisok",
//                "password=fisok2020",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/dbrgatl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void getTableDDLTest(){
        String[] args =  new String[]{
                "DBReverseGetTableDDL",            //执行什么命令
                "driver_class_name=com.mysql.cj.jdbc.Driver",
                "url=jdbc:mysql://g5.mtain.top:51236/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                "username=root",
                "password=passwd",
//                "driver_class_name=com.mysql.cj.jdbc.Driver",
//                "url=jdbc:mysql://127.0.0.1:3306/vbcms?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
//                "username=fisok",
//                "password=fisok2020",
//                "tables=cust_base,cust_ent,cust_ind,cust_owner,cust_fnastat",
//                "tables=cust_ent,cust_base",
                "tables=sys_user,user",
                "out=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/dbrgtddl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void getTableDDL4SQLServerTest(){
        String[] args =  new String[]{
                "DBReverseGetTableDDL",            //执行什么命令
//                "driver_class_name=com.mysql.cj.jdbc.Driver",
//                "url=jdbc:mysql://127.0.0.1:3306/vbcms?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
//                "username=fisok",
//                "password=fisok2020",
                "driver_class_name=com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "url=jdbc:sqlserver://123.207.182.229:1433;DatabaseName=pdman",
                "username=sa",
                "password=pdman2021#$%",
//                "tables=cust_base,cust_ent,cust_ind,cust_owner,cust_fnastat",
                "tables=cust_ent,cust_base",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/dbrgtddl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void getTableDDL4PostgreSQLTest(){
        String[] args =  new String[]{
                "DBReverseGetTableDDL",            //执行什么命令
//                "driver_class_name=com.mysql.cj.jdbc.Driver",
//                "url=jdbc:mysql://127.0.0.1:3306/vbcms?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
//                "username=fisok",
//                "password=fisok2020",
                "driver_class_name=org.postgresql.Driver",
                "url=jdbc:postgresql://148.70.37.64:5432/test",
                "username=test",
                "password=123456",
//                "tables=SIMS_CLASS,SIMS_STUDENT",
                "tables=SIMS_STUDENT",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/dbrgtddl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void listTable4PostgreSQLTest(){
        String[] args =  new String[]{
                "DBReverseGetAllTablesList",            //执行什么命令
                "driver_class_name=org.postgresql.Driver",
                "url=jdbc:postgresql://148.70.37.64:5432/test",
                "username=test",
                "password=123456",
//                "driver_class_name=org.postgresql.Driver",
//                "url=jdbc:postgresql://148.70.37.64:5432/test",
//                "username=test",
//                "password=123456",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/dbrgatl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void getTableDDL4OracleTest(){
        String[] args =  new String[]{
                "DBReverseGetTableDDL",            //执行什么命令
//                "driver_class_name=com.mysql.cj.jdbc.Driver",
//                "url=jdbc:mysql://127.0.0.1:3306/vbcms?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
//                "username=fisok",
//                "password=fisok2020",
                "driver_class_name=oracle.jdbc.driver.OracleDriver",
                "url=jdbc:oracle:thin:@g5.mtain.top:30003:ORCL",
                "username=UCREDIT",
                "password=pNhpE8hn",
//                "tables=SIMS_CLASS,SIMS_STUDENT",
                "tables=SIMS_STUDENT",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/dbrgtddl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void genDocxTest(){
        String[] args =  new String[]{
                "GenDocx",            //执行什么命令
//                "sinerFile=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/siner/业务配置及调查报告.sinoper.json",  //输入的PDMan文件
                "sinerFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/siner/营销战情中心.chnr.json",  //输入的PDMan文件
                "docxTpl=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/tpl/siner-docx-tpl.docx",      //文档模板文件
                "imgDir=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/images/smis",                         //图片文件存放目录
                "imgExt=.png",//图片文件后缀名
                "outFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/siner-"+System.nanoTime()+".docx",
                "out=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/gendocx-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void genDocxGroupsTest(){
        String[] args =  new String[]{
                "GenDocx",            //执行什么命令
                "sinerFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/siner/bbc系统.chnr.json",  //输入的PDMan文件
                "docxTpl=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/tpl/siner-docx-tpl.docx",      //文档模板文件
                "imgDir=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/images/shop",                         //图片文件存放目录
                "imgExt=.png",//图片文件后缀名
                "outFile=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/siner-"+System.nanoTime()+".docx",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/gendocx-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void parsePDMFileTest(){
        String[] args =  new String[]{
                "ParsePDMFile",            //执行什么命令
                "pdmFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/pdm/通用组件模型汇总(1).pdm",  //输入的PDMan文件
//                "pdmFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/pdm/JEKI-WIKI文章模块.pdm",  //输入的PDMan文件
//                "pdmFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/pdm/数据字典.PDM",  //输入的PDMan文件
                "out=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/import-pdm-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }
}
