package com.bjutsport.netapi;

import com.bjutsport.aes.AESUtil;
import com.bjutsport.enums.Key;
import com.bjutsport.enums.WSInfo;
import com.bjutsport.enums.WSMethod;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by HUDIYU on 2016/7/19.
 */
public class UserAPI {

    public int login(String username, String password) throws Exception {

        //加密用户输入的用户名和密码
        String encryptedUserName = AESUtil.encrypt(Key.AES.getKey(), username);
        String encryptedUserPassword = AESUtil.encrypt(Key.AES.getKey(), password);

        /**
         * 向服务器发出数据
         * */

        //创建一个SoapObject的对象,并指定WebService的命名空间和调用的方法名
        SoapObject request = new SoapObject(WSInfo.NAMESPACE.getAddress(), WSMethod.LOGIN.getName());

        //设置调用方法的参数值,添加加密后的用户名与密码
        request.addProperty("encryptedUserName", encryptedUserName);
        request.addProperty("encryptedUserPassword", encryptedUserPassword);

        //创建HttpTransportSE对象,并通过HttpTransportSE类的构造方法指定Webservice的WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(WSInfo.WSDL.getAddress(), 1000);

        //生成调用WebService方法的SOAP请求消息,该信息由SoapSerializationEnvelope描述
        //SOAP版本号为1.1
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //设置bodyOut属性为SoapObject对象request
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);

        //使用call方法调用WebService方法
        ht.call(null, envelope);

        /**
         * 从服务器收取数据
         * */

        //获取返回值
        SoapObject returnedValue = (SoapObject) envelope.bodyIn;

        //解析返回结果
        int ret = Integer.parseInt(returnedValue.getPropertyAsString(0));

        return ret;
    }
    public int validate(String phoneNums) throws Exception {
        //加密用户输入的用户名
        String encryptedUserName = AESUtil.encrypt(Key.AES.getKey(), phoneNums);

        //创建一个SoapObject的对象,并指定WebService的命名空间和调用的方法名
        SoapObject ruquest = new SoapObject(WSInfo.NAMESPACE.getAddress(), WSMethod.VALIDATE_USERNAME.getName());

        //设置调用方法的参数值,添加加密后的用户名与密码
        ruquest.addProperty("encryptedUserName", encryptedUserName);

        //创建HttpTransportSE对象,并通过HttpTransportSE类的构造方法指定Webservice的WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(WSInfo.WSDL.getAddress(), 1000);

        //生成调用WebService方法的SOAP请求消息,该信息由SoapSerializationEnvelope描述
        //SOAP版本号为1.1
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //设置bodyOut属性为SoapObject对象request
        envelope.bodyOut = ruquest;
        envelope.setOutputSoapObject(ruquest);

        //使用call方法调用WebService方法
        ht.call(null, envelope);

        //获取返回值
        SoapObject returnedValue = (SoapObject) envelope.bodyIn;

        //解析返回结果
        int ret = Integer.parseInt(returnedValue.getPropertyAsString(0));

        return ret;
    }
    public int register(String username, String password) throws Exception {

        int ret = -1;

        //加密用户输入的用户名和密码
        String encryptedUserName = AESUtil.encrypt(Key.AES.getKey(), username);
        String encryptedUserPassword = AESUtil.encrypt(Key.AES.getKey(), password);

        /**
         * 向服务器发送数据
         * */

        //创建一个SoapObject的对象,并指定WebService的命名空间和调用的方法名
        SoapObject request = new SoapObject(WSInfo.NAMESPACE.getAddress(), WSMethod.REGISTER.getName());

        //设置调用方法的参数值,添加加密后的用户名与密码
        request.addProperty("encryptedUserName", encryptedUserName);
        request.addProperty("encryptedUserPassword", encryptedUserPassword);

        //创建HttpTransportSE对象,并通过HttpTransportSE类的构造方法指定Webservice的WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(WSInfo.WSDL.getAddress(), 1000);

        //生成调用WebService方法的SOAP请求消息,该信息由SoapSerializationEnvelope描述
        //SOAP版本号为1.1
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //设置bodyOut属性为SoapObject对象request
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);

        //使用call方法调用WebService方法
        ht.call(null, envelope);

        //获取返回值
        SoapObject returnedValue = (SoapObject) envelope.bodyIn;
        //解析返回结果
        ret = Integer.parseInt(returnedValue.getPropertyAsString(0));

        return ret;
    }
    public int changePassword(String username, String password) throws Exception {

        int ret = -1;

        //加密用户输入的用户名和密码
        String encryptedUserName = AESUtil.encrypt(Key.AES.getKey(), username);
        String encryptedUserPassword = AESUtil.encrypt(Key.AES.getKey(), password);

        //创建一个SoapObject的对象,并指定WebService的命名空间和调用的方法名
        SoapObject request = new SoapObject(WSInfo.NAMESPACE.getAddress(), WSMethod.CHANGE_PASSWORD.getName());

        //设置调用方法的参数值,添加加密后的用户名与密码
        request.addProperty("encryptedUserName", encryptedUserName);
        request.addProperty("encryptedUserPassword", encryptedUserPassword);

        //创建HttpTransportSE对象,并通过HttpTransportSE类的构造方法指定Webservice的WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(WSInfo.WSDL.getAddress(), 1000);

        //生成调用WebService方法的SOAP请求消息,该信息由SoapSerializationEnvelope描述
        //SOAP版本号为1.1
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //设置bodyOut属性为SoapObject对象request
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);

        //使用call方法调用WebService方法
        ht.call(null, envelope);

        //获取返回值
        SoapObject returnedValue = (SoapObject) envelope.bodyIn;
        //解析返回结果
        ret = Integer.parseInt(returnedValue.getPropertyAsString(0));

        return ret;
    }
}
