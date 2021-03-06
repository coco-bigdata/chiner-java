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

import cn.com.chiner.java.command.Command;
import cn.com.chiner.java.command.impl.*;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/11
 * @desc : siner后端Java处理的入口主程序
 */
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    private static Map<String, Class<?>> commandRegister = new HashMap<String, Class<?>>() {{
        put("PingLoadDriverClass", PingLoadDriverClassImpl.class);              //加载驱动测试
        put("DBReverseGetAllTablesList", DBReverseGetAllTablesListImpl.class);  //逆向解析，获取数据表清单
        put("DBReverseGetTableDDL", DBReverseGetTableDDLImpl.class);            //逆向解析，获取指定数据表DDL
        put("ParsePDMFile", ParsePDMFileImpl.class);                            //逆向解析，获取指定数据表DDL
        put("GenDocx", GenDocxImpl.class);                                      //生成WORD文档
    }};

    /**
     * 参数转换<br/>
     * 例如：
     * driver_class_name=com.mysql.jdbc.Driver
     * url=jdbc:mysql://127.0.0.1:3306/vbcms?useUnicode=true&characterEncoding=UTF-8&useSSL=false
     * 转换为KV的Map结构中去
     * @param argList
     * @return
     */
    public static Map<String, String> parseArgs(List<String> argList) {
        Map argsMap = new HashMap();
        argList.forEach(x -> {
            x = x.replace("\"","").replace("\'","");
            int idx = x.indexOf("=");
            if (idx > 0) {
                String key = x.substring(0, idx);
                String value = x.substring(idx + 1);
                argsMap.put(key, value);
            }
        });

        return argsMap;
    }

    /**
     * 如果日志文件目录不存在，创建该目录
     * @throws IOException
     */
    public static void touchLogHomeDirectory() throws IOException {
        String userHome = System.getProperties().getProperty("user.home");
        List<String> fileParts = new ArrayList<String>(){{
           add(userHome);
           add(File.separator);
           add("logs");
           add(File.separator);
           add("siner");
           add(File.separator);
           add("touch-test-");
           add(System.currentTimeMillis()+"");
           add(".txt");
        }};
        String filePath = StringKit.join(fileParts);
        File file = new File(filePath);
        if(!file.exists()) {
            file.createNewFile();
        }
        System.setErr(new PrintStream(new FileOutputStream(file)));
    }

    /**
     * 把文本输出
     * @param outFileFullPath
     * @param text
     * @throws IOException
     */
    public static void sendTextOut(String outFileFullPath,String text) throws IOException {
        Date curDate = DateKit.now();
        File parentDirectory = new File(outFileFullPath).getParentFile();
        if(parentDirectory.isDirectory()){
            if(parentDirectory.exists()){
                File[] files = parentDirectory.listFiles();
                for(File file : files){
                    Date lastModified = new Date(file.lastModified());
                    int seconds = DateKit.getRangeSeconds(lastModified,curDate);
                    //一个小时之前的文件，则删除他
//                    if(seconds>3600){
//                        if(file.isDirectory()){
//                            FileKit.deleteDirectory(file);
//                        }else{
//                            FileKit.deleteFile(file);
//                        }
//                    }
                }
            }
        }

        File outFile = FileKit.touchFile(new File(outFileFullPath),true);
        FileKit.write(outFile,text,"UTF-8");
    }

    /**
     * 命令执行入口
     * @param args
     */
    public static void main(String[] args) {


        //1.检查参数的合法性
        if (args == null || args.length == 0) {
            String msg = "No Command Error !";
            logger.error(msg);
            throw new RuntimeException(msg);
        }

        //2. 解析命令及参数
        String cmdText = StringKit.nvl(args[0], "");
        List<String> argList = new ArrayList<String>();
        for (int i = 1; i < args.length; i++) {
            argList.add(args[i]);
        }
        Map<String, String> argsMap = parseArgs(argList);

        //3. 检查命令的合法性
        Class<?> cmdClass = commandRegister.get(cmdText);
        if (cmdClass == null) {
            String msg = "Command [" + cmdText + "] Not Supported.";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        String outFileFullPath = argsMap.get("out");
        if(StringKit.isBlank(outFileFullPath)){
            String msg = "Parameter [out] for the command ["+cmdText+"] does not exist.";
            logger.error(msg);
            throw new RuntimeException(msg);
        }

        //4. 输出执行的命令信息
        logger.info("------------------------------------>>>[执行命令]<<<-------------------------------------");
        logger.info(cmdText + " " + StringKit.join(argList, " "));
        logger.info("----------------------------------------------------------------------------------------");

        //5. 执行命令
        try {
            Command<?> cmd = (Command<?>) cmdClass.newInstance();
            Object ret = cmd.exec(argsMap);
            String retText = "";

            if (ret instanceof String) {
                retText = (String) ret;
            } else if (ret instanceof Number) {
                retText = ((Number) ret).toString();
            } else {
                retText = JSONKit.toJsonString(ret,true);
            }
            logger.info(retText);

            sendTextOut(outFileFullPath,retText);
        } catch (InstantiationException e) {
            logger.error("执行命令异常",e);
        } catch (IllegalAccessException e) {
            logger.error("执行命令异常",e);
        } catch (Exception e) {
            logger.error("未知的错误",e);
        }
    }

}
