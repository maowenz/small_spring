package com.mwz;

import starter.SmallApplication;

/**
 * @author wzm
 * @date 2019年08月30日 16:39
 */
public class Application {

    public static void main(String[] args) {
        System.out.println("hello,this is test small_spring!");
        System.out.println(Application.class.getPackage());
        SmallApplication.run(com.mwz.Application.class,args);
    }

}
