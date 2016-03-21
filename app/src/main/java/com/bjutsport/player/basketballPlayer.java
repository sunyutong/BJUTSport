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
    private String sex;
    //身高
    private Integer tall;
    //体重
    private Integer weight;
    //位置
    private String position;
    //队伍
    private String team;
    //号码
    private Integer number;
    //球员特征
    private String characteristic;

    @Override
    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return name;
            case 1:
                return age;
            case 2:
                return sex;
            case 3:
                return tall;
            case 4:
                return weight;
            case 5:
                return position;
            case 6:
                return team;
            case 7:
                return number;
            case 8:
                return characteristic;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 8;
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
                sex = arg1.toString();
                break;
            case 3:
                tall = Integer.parseInt(arg1.toString());
                break;
            case 4:
                weight = Integer.parseInt(arg1.toString());
                break;
            case 5:
                position = arg1.toString();
                break;
            case 6:
                team = arg1.toString();
                break;
            case 7:
                number = Integer.parseInt(arg1.toString());
                break;
            case 8:
                characteristic = arg1.toString();
                break;
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
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "sex";
                break;
            case 3:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "tall";
                break;
            case 4:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "weight";
                break;
            case 5:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "position";
                break;
            case 6:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "team";
                break;
            case 7:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "number";
                break;
            case 8:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "characteristic";
                break;
            default:
                break;
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Integer getTall() {
        return tall;
    }
    public void setTall(Integer tall) {
        this.tall = tall;
    }
    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public String getCharacteristic() {
        return characteristic;
    }
    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    @Override
    public String toString() {
        return "basketballPlayer [name=" + name + ", sex=" + sex + ", age=" + age + ", tall=" + tall
                + ", weight=" + weight + ", position=" + position + ", team=" + team + ", number=" + number
                + ", characteristic=" + characteristic + "]";
    }

}
