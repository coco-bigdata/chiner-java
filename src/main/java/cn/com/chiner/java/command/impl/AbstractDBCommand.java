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
package cn.com.chiner.java.command.impl;

import cn.com.chiner.java.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/12
 * @desc : 数据库相关操作命令的抽像
 */
public abstract class AbstractDBCommand<T> implements Command<T> {
    public static final String KEY_DRIVER_CLASS_NAME = "driver_class_name";
    public static final String KEY_URL = "url";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String driverClassName;
    protected String url;
    protected String username;
    protected String password;

    public void init(Map<String,String> params){
        driverClassName = params.get(KEY_DRIVER_CLASS_NAME);
        url = params.get(KEY_URL);
        username = params.get(KEY_USERNAME);
        password = params.get(KEY_PASSWORD);
    }
}
