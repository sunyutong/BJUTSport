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

public class LoginActivity extends Activity {

    private static final String AES_KEY = "BJUTSport1234567";
    private static final String WEBSERVICE_WSDL_URL = "http://192.168.1.102:8080/BJUTSport/services/LoginImplPort?wsdl";
    private static final String WEBSERVICE_NAMESPACE = "http://login.bjutsport.com/";
    private static final String METHOD_NAME = "login";

    private static final int SHOW_LOGIN_SUCCESS_IN_TEXTVIEW = 0x0000;
    private static final int SHOW_LOGIN_FAILED_IN_TEXTVIEW = 0x0001;
    private static final int JUMP_TO_USERACTIVITY = 0x0002;
    private static final int CHANGE_TRANSPARENCY = 0x0003;

    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAILED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //获取返回按钮
        Button button_back = (Button) findViewById(R.id.Button_LoginActivity_Back);
        //点击返回主界面
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_User = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent_User);
                finish();
            }
        });
        //获取TextView_Login_Result
        final TextView loginResult;
        loginResult = (TextView) findViewById(R.id.TextView_LoginActivity_Result);

        //显示TextView_Login_Result中文本的Handler
        final Handler showLoginResultHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loginResult.setTextColor(Color.argb(255, 127, 127, 127));
                switch (msg.what) {
                    case SHOW_LOGIN_SUCCESS_IN_TEXTVIEW:
                        //显示登录成功
                        loginResult.setText("Login success！");
                        break;
                    case SHOW_LOGIN_FAILED_IN_TEXTVIEW:
                        //显示登录失败
                        loginResult.setText("Login failed, please try again！");
                        break;
                    default:
                        break;
                }
            }
        };

        //获取登录成功消息的Handler
        final Handler loginSuccessHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == JUMP_TO_USERACTIVITY) {
                    //跳转到用户界面
                    Intent intent_User = new Intent(LoginActivity.this, UserActivity.class);
                    startActivity(intent_User);
                    finish();
                }
            }
        };

        //设置TextView_Login_Result中文本的透明度的Handler
        final Handler textViewChangeHandler = new Handler() {
            //透明度初值
            int i = 255;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == CHANGE_TRANSPARENCY) {
                    //每收到一次消息透明度减1
                    loginResult.setTextColor(Color.argb(i--, 127, 127, 127));
                }
            }
        };

        //获取登录按按钮
        Button button_login = (Button) findViewById(R.id.Button_LoginActivity_Login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建新的进程以进行网络访问
                new Thread() {
                    public void run() {
                        //获得用户名与密码EditText
                        EditText ediUserName = (EditText) findViewById(R.id.EditText_LoginActivity_UserName);
                        EditText ediUserPassword = (EditText) findViewById(R.id.EditText_LoginActivity_UserPassword);

                        //提取用户输入的用户名和密码
                        String strUserName = ediUserName.getText().toString();
                        String strUserPassword = ediUserPassword.getText().toString();

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
                            int result = Integer.parseInt(returnedValue.getPropertyAsString(0));

                            switch (result) {
                                case LOGIN_SUCCESS:
                                    //如果服务器返回值为LOGIN_SUCCESS,则发送消息以显示登陆成功
                                    showLoginResultHandler.sendEmptyMessage(SHOW_LOGIN_SUCCESS_IN_TEXTVIEW);
                                    //300毫秒后发送消息以从登录界面跳转至用户界面
                                    Thread.sleep(300);
                                    loginSuccessHandler.sendEmptyMessage(JUMP_TO_USERACTIVITY);
                                    break;
                                case LOGIN_FAILED:
                                    //如果服务器返回值为LOGIN_FAILED,则发送消息以显示登陆失败
                                    showLoginResultHandler.sendEmptyMessage(SHOW_LOGIN_FAILED_IN_TEXTVIEW);
                                    //发送消息以改变TextView中文本的透明度
                                    new Thread() {
                                        public void run() {
                                            for (int i = 0; i < 256; i++) {
                                                try {
                                                    if (i == 0) {
                                                        //非透明显示1秒后开始渐变
                                                        Thread.sleep(1000);
                                                    } else {
                                                        //每8毫秒发送发送一次消息
                                                        Thread.sleep(8);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                //发送消息
                                                textViewChangeHandler.sendEmptyMessage(CHANGE_TRANSPARENCY);
                                            }
                                        }
                                    }.start();
                                    break;
                                default:
                                    break;
                            }

                        } catch (Exception e) {
                            //抛出异常则发送消息以显示登陆失败
                            showLoginResultHandler.sendEmptyMessage(SHOW_LOGIN_FAILED_IN_TEXTVIEW);
                            new Thread() {
                                public void run() {
                                    for (int i = 0; i < 256; i++) {
                                        try {
                                            if (i == 0) {
                                                //非透明显示1秒后开始渐变
                                                Thread.sleep(1000);
                                            } else {
                                                //每8毫秒发送发送一次消息
                                                Thread.sleep(8);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //发送消息
                                        textViewChangeHandler.sendEmptyMessage(CHANGE_TRANSPARENCY);
                                    }
                                }
                            }.start();
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

}
