package com.bjutsport.player;


import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class basketballPlayer implements KvmSerializable {

    //姓名
    private String name;
    //年龄
    private Integer age;
    //性别
    private Boolean sex;

    @Override
    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return name;
            case 1:
                return age;
            case 2:
                return sex;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
        switch (arg0) {
            case 0:
                name = arg1.toString();
                break;
            case 1:
                age = Integer.parseInt(arg1.toString());
                break;
            case 2:
                sex = Boolean.parseBoolean(arg1.toString());
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2){
        switch (arg0) {
            case 0:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "name";
                break;
            case 1:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "age";
                break;
            case 2:
                arg2.type = PropertyInfo.BOOLEAN_CLASS;
                arg2.name = "sex";
                break;
            default:
                break;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Boolean getSex() {
        return sex;
    }
}
