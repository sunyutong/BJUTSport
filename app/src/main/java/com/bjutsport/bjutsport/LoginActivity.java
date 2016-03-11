package com.bjutsport.bjutsport;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
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

import com.bjutspots.aes.AESUtil;

public class LoginActivity extends AppCompatActivity {

    private static final String AES_KEY = "BJUTSports123456";
    private static final String URL = "http://192.168.1.101:8080/BJUTSports/services/LoginImplPort";
    private static final String NAMESPACE = "http://login.bjutsports.com/";
    private static final String METHOD_NAME = "authentication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Button button_back = (Button) findViewById(R.id.Button_LoginActivity_to_MainActivity);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView loginResult;
        loginResult = (TextView) findViewById(R.id.TextView_Login_Result);

        final Handler loginHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0xffff:
                        loginResult.setTextColor(Color.argb(255, 127, 127, 127));
                        loginResult.setText("Register success！");
                        // 跳转
                        break;
                    case 0x0000:
                        loginResult.setText("Register failed, please try again！");
                        break;
                    default:
                        break;
                }
            }
        };

        final Handler loginFalseHandler = new Handler() {
            int i = 255;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x7777) {
                    loginResult.setTextColor(Color.argb(i--, 127, 127, 127));
                }
            }
        };

        Button button_login = (Button) findViewById(R.id.Button_Login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        EditText ediUserName, ediUserPassword;
                        ediUserName = (EditText) findViewById(R.id.EditText_Login_userName);
                        ediUserPassword = (EditText) findViewById(R.id.EditText_Login_userPassword);

                        String strUserName = ediUserName.getText().toString();
                        String strUserPassword = ediUserPassword.getText().toString();

                        try {
                            //加密用户输入的用户名和密码
                            String encryptedUserName = AESUtil.encrypt(AES_KEY, strUserName);
                            String encryptedUserPassword = AESUtil.encrypt(AES_KEY, strUserPassword);

                            HttpTransportSE ht = new HttpTransportSE(URL);
                            ht.debug = true;

                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                            SoapObject ruquest = new SoapObject(NAMESPACE, METHOD_NAME);

                            ruquest.addProperty("encryptedUserName", encryptedUserName);
                            ruquest.addProperty("encryptedUserPassword", encryptedUserPassword);

                            envelope.bodyOut = ruquest;
                            envelope.setOutputSoapObject(ruquest);

                            ht.call(null, envelope);

                            SoapObject returnedValue = (SoapObject) envelope.bodyIn;
                            String result = returnedValue.getPropertyAsString(0);

                            if (result.equals("true")) {
                                loginHandler.sendEmptyMessage(0xffff);
                            } else {
                                loginHandler.sendEmptyMessage(0x0000);
                                //发送Message以改变loginResult透明度
                                new Thread() {
                                    public void run() {
                                        for (int i = 0; i < 256; i++) {
                                            try {
                                                //显示1秒后开始渐变
                                                if (i == 0) {
                                                    Thread.sleep(1000);
                                                } else {
                                                    Thread.sleep(8);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            loginFalseHandler.sendEmptyMessage(0x7777);
                                        }
                                    }
                                }.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

}
