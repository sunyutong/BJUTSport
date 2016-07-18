package com.bjutsport.enums;

/**
 * Created by hudiyu on 16/6/24.
 */
public enum WSMethod {

    LOGIN("login"),
    REGISTER("register"),
    CHANGE_PASSWORD("changePassword"),
    VALIDATE_USERNAME("validateUsername");
    private final String name;

    WSMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
