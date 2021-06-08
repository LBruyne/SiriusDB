package com.siriusdb.client;

import com.siriusdb.client.db.interpreter.Interpreter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 分布式数据库客户端入口
 * @author: liuxuanming
 * @date: 2021/04/29 3:43 下午
 */
@Slf4j
public class ClientRunner {

    public static void main( String[] args ) {
        Interpreter.initial();
    }
}
