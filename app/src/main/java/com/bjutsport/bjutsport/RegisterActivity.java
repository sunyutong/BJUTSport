package com.bjutsport.bjutsport;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.bjutsport.aes.AESUtil;

public class RegisterActivity extends Activity {

    private static final String AES_KEY = "BJUTSports123456";
    private static final String WEBSERVICE_WSDL_URL = "http://192.168.1.100:8080/BJUTSports/services/LoginImplPort";
    private static final String WEBSERVICE_NAMESPACE = "http://login.bjutsports.com/";
    private static final String METHOD_NAME = "register";

    private static final int SHOW_PASSWORD_UNCONSISTENT = 0x0000;
    private static final int SHOW_REGISTER_SUCCESS = 0x0001;
    private static final int SHOW_REGISTER_FAILED = 0x0002;
    private static final int JUMP_TO_LOGINACTIVITY = 0x0003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置标题为空
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //获取返回按钮
        Button button_back = (Button) findViewById(R.id.Button_RegisterActivity_to_MainActivity);
        //点击返回主界面
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_User = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent_User);
                finish();
            }
        });

        //获取TextView_Register_Result
        final TextView registerResult;
        registerResult = (TextView) findViewById(R.id.TextView_Register_Result);
        //设置初始颜色
        registerResult.setTextColor(Color.argb(255, 127, 127, 127));

        //显示TextView_Register_Result中文本的Handler
        final Handler registerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_PASSWORD_UNCONSISTENT:
                        //显示密码前后不一致
                        registerResult.setText("Passwords are unconsistent！");
                        break;
                    case SHOW_REGISTER_SUCCESS:
                        //显示注册成功
                        registerResult.setText("Register success！");
                        break;
                    case SHOW_REGISTER_FAILED:
                        //显示注册失败
                        registerResult.setText("Register failed, please try again！");
                        break;
                    default:
                        break;
                }
            }
        };

        //获取注册成功消息的Handler
        final Handler registerSuccessHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == JUMP_TO_LOGINACTIVITY) {
                    //跳转到用户界面
                    Intent intent_User = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent_User);
                    finish();
                }
            }
        };

        //获取注册按钮
        Button button_register = (Button) findViewById(R.id.Button_Register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建新的进程以进行网络访问
                new Thread() {
                    public void run() {
                        //获得用户名,密码,二次输入密码EditText
                        EditText ediUserName, ediUserPassword, ediUserPasswordCheck;
                        ediUserName = (EditText) findViewById(R.id.EditText_Register_userName);
                        ediUserPassword = (EditText) findViewById(R.id.EditText_Register_userPassword);
                        ediUserPasswordCheck = (EditText) findViewById(R.id.EditText_Register_userPassword_Check);

                        //提取用户输入的用户名,密码和二次密码
                        String strUserName = ediUserName.getText().toString();
                        String strUserPassword = ediUserPassword.getText().toString();
                        String strUserPasswordCheck = ediUserPasswordCheck.getText().toString();

                        if (!strUserPassword.equals(strUserPasswordCheck)) {
                            //如果用户输入的密码前后不一致,发送消息显示密码前后不一致
                            registerHandler.sendEmptyMessage(SHOW_PASSWORD_UNCONSISTENT);
                        } else {
                            //否则进行注册
                            try {
                                //加密用户输入的用户名和密码
                                String encryptedUserName = AESUtil.encrypt(AES_KEY, strUserName);
                                String encryptedUserPassword = AESUtil.encrypt(AES_KEY, strUserPassword);

                                //创建一个SoapObject的对象,并指定WebService的命名空间和调用的方法名
                                SoapObject ruquest = new SoapObject(WEBSERVICE_NAMESPACE, METHOD_NAME);

                                //设置调用方法的参数值,添加加密后的用户名与密码
                                ruquest.addProperty("encryptedUserName", encryptedUserName);
                                ruquest.addProperty("encryptedUserPassword", encryptedUserPassword);

                                //创建HttpTransportSE对象,并通过HttpTransportSE类的构造方法指定Webservice的WSDL文档的URL
                                HttpTransportSE ht = new HttpTransportSE(WEBSERVICE_WSDL_URL);

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
                                String result = returnedValue.getPropertyAsString(0);

                                if (result.equals("true")) {
                                    //如果服务器返回值为true,则发送消息以显示注册成功
                                    registerHandler.sendEmptyMessage(SHOW_REGISTER_SUCCESS);
                                    Thread.sleep(300);
                                    registerSuccessHandler.sendEmptyMessage(JUMP_TO_LOGINACTIVITY);
                                } else {
                                    //如果服务器返回值为flase,则发送消息以显示注册失败
                                    registerHandler.sendEmptyMessage(SHOW_REGISTER_FAILED);
                                }
                            } catch (Exception e) {
                                //抛出异常则发送消息以显示注册失败
                                registerHandler.sendEmptyMessage(SHOW_REGISTER_FAILED);
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
    }

}
