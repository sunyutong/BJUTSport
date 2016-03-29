package com.bjutsport.bjutsport;

/**
 * Created by HUDIYU on 2016/3/28.
 */
class WebService {
    //AES密钥
    static final String AES_KEY = "BJUTSport1234567";
    //登录界面URL
    static final String WEBSERVICE_WSDL_URL = "http://192.168.1.101:8080/BJUTSport/services/BJUTSportWebServiceImplPort?wsdl";
    //登录界面域名
    static final String WEBSERVICE_NAMESPACE = "http://bjutsport.com/";
    //登录界面方法名称:login
    static final String METHOD_NAME_LOGIN = "login";
    //忘记密码界面方法名称：changePassword
    static final String METHOD_NAME_CHANGE_PASSWORD = "changePassword";
    //注册界面方法名称:register
    static final String METHOD_NAME_REGISTER = "register";
    //验证验证码界面方法名称:validateUsername
    static final String METHOD_NAME_VALIDATE_USERNAME = "validateUsername";
}
