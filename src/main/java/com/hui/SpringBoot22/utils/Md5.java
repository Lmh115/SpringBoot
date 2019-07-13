package com.hui.SpringBoot22.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class Md5 {

    private Md5() {}

    public static String md5(String pwd){
        Md5Hash md5Hash = new Md5Hash(pwd);
        return md5Hash.toString();
    }
}
