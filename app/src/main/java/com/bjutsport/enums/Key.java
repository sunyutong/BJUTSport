package com.bjutsport.enums;

/**
 * Created by hudiyu on 16/6/24.
 */
public enum Key {

    //AES秘钥
    AES("BJUTSport1234567");

    private final String key;

    Key(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }


}
