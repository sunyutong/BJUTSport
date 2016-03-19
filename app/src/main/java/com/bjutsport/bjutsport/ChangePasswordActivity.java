package com.bjutsport.bjutsport;


import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bjutsport.aes.AESUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class ChangePasswordActivity extends BaseActivity {

    private static final String AES_KEY = "BJUTSport1234567";
    private static final String WEBSERVICE_WSDL_URL = "http://192.168.1.101:8080/BJUTSport/services/RegisterImplPort?wsdl";
    private static final String WEBSERVICE_NAMESPACE = "http://register.bjutsport.com/";
    private static final String METHOD_NAME = "changePassword";

    private static final int SHOW_PASSWORD_UNCONSISTENT = 0x0000;
    private static final int SHOW_CHANGE_SUCCESS = 0x0001;
    private static final int SHOW_CHANGE_FAILED = 0x0002;
    private static final int SHOW_SOCKETTIMEOUT = 0x0003;
    private static final int SHOW_PASSWORD_TOO_SHORT = 0x0004;
    private static final int JUMP_TO_LOGINACTIVITY = 0x0005;

    private static final int passwordLength = 8;

    private static final int CHANGE_SUCCESS = 1;
    private static final int CHANGE_FAILED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置标题为空
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_change_password);

        //设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //获取返回按钮
        Button button_back = (Button) findViewById(R.id.Button_ChangePasswordActivity_to_MainActivity);
        //点击返回核实界面
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Verification = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(intent_Verification);
                finish();
            }
        });

        final Bundle bundle = this.getIntent().getExtras();

        //Register的Handler
        final Handler registerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_PASSWORD_TOO_SHORT:
                        //显示密码太短
                        Toast.makeText(getApplicationContext(), "密码长度应至少大于8", Toast.LENGTH_SHORT).show();
                        break;
                    case SHOW_PASSWORD_UNCONSISTENT:
                        //显示密码前后不一致
                        Toast.makeText(getApplicationContext(), "密码前后不一致", Toast.LENGTH_SHORT).show();
                        break;
                    case SHOW_CHANGE_SUCCESS:
                        //显示修改成功
                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        break;
                    case SHOW_CHANGE_FAILED:
                        //显示注册失败
                        Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                    case SHOW_SOCKETTIMEOUT:
                        //显示连接超时
                        Toast.makeText(getApplicationContext(), "连接超时,请检查网络连接", Toast.LENGTH_SHORT).show();
                        break;
                    case JUMP_TO_LOGINACTIVITY:
                        //跳转到用户界面
                        Intent intent_User = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        startActivity(intent_User);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };

        //获取注册按钮
        Button button_register = (Button) findViewById(R.id.Button_ChangePassword_Verification);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建新的进程以进行网络访问
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获得密码,二次输入密码EditText
                        EditText ediUserPassword, ediUserPasswordCheck;
                        ediUserPassword = (EditText) findViewById(R.id.EditText_ChangePassword_userPassword);
                        ediUserPasswordCheck = (EditText) findViewById(R.id.EditText_ChangePassword_userPassword_Check);

                        //提取用户输入的用户名,密码和二次密码
                        String strUserName = bundle.getString("phoneNums");
                        //String strUserName = "15911135092";
                        String strUserPassword = ediUserPassword.getText().toString();
                        String strUserPasswordCheck = ediUserPasswordCheck.getText().toString();
                        if (!checkPasswordLength(strUserPassword)) {
                            //如果用户输入的密码长度小于8,发送消息显示
                            registerHandler.sendEmptyMessage(SHOW_PASSWORD_TOO_SHORT);
                        } else if (!strUserPassword.equals(strUserPasswordCheck)) {
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
                                HttpTransportSE ht = new HttpTransportSE(WEBSERVICE_WSDL_URL, 1000);

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
                                    case CHANGE_SUCCESS:
                                        //如果服务器返回值为true,则发送消息以显示更改成功
                                        registerHandler.sendEmptyMessage(SHOW_CHANGE_SUCCESS);
                                        Thread.sleep(300);
                                        registerHandler.sendEmptyMessage(JUMP_TO_LOGINACTIVITY);
                                        break;
                                    case CHANGE_FAILED:
                                        //如果服务器返回值为flase,则发送消息以显示更改失败
                                        registerHandler.sendEmptyMessage(SHOW_CHANGE_FAILED);
                                        break;
                                    default:
                                        break;
                                }

                            } catch (SocketTimeoutException ste) {
                                //抛出异常以显示连接超时
                                registerHandler.sendEmptyMessage(SHOW_SOCKETTIMEOUT);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    private boolean checkPasswordLength(String password) {
        return password.length() >= passwordLength;
    }

}
