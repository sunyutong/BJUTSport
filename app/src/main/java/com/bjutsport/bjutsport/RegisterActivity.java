package com.bjutsport.bjutsport;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    private static final String AES_KEY = "BJUTSports123456";
    private static final String URL = "http://192.168.1.101:8080/BJUTSports/services/LoginImplPort";
    private static final String NAMESPACE = "http://login.bjutsports.com/";
    private static final String METHOD_NAME = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Button button_back = (Button) findViewById(R.id.Button_RegisterActivity_to_MainActivity);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_User = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent_User);
                finish();
            }
        });

        final TextView registerResult;
        registerResult = (TextView) findViewById(R.id.TextView_Register_Result);
        registerResult.setTextColor(Color.argb(255, 127, 127, 127));

        final Handler registerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x1111:
                        registerResult.setText("Passwords are not consistent！");
                        break;
                    case 0xffff:
                        registerResult.setText("Register success！");
                        break;
                    case 0x0000:
                        registerResult.setText("Register failed, please try again！");
                        break;
                    default:
                        break;
                }
            }
        };

        Button button_register = (Button) findViewById(R.id.Button_Register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        EditText ediUserName, ediUserPassword, ediUserPasswordCheck;
                        ediUserName = (EditText) findViewById(R.id.EditText_Register_userName);
                        ediUserPassword = (EditText) findViewById(R.id.EditText_Register_userPassword);
                        ediUserPasswordCheck = (EditText) findViewById(R.id.EditText_Register_userPassword_Check);

                        String strUserName = ediUserName.getText().toString();
                        String strUserPassword = ediUserPassword.getText().toString();
                        String strUserPasswordCheck = ediUserPasswordCheck.getText().toString();

                        if (!strUserPassword.equals(strUserPasswordCheck)) {
                            registerHandler.sendEmptyMessage(0x1111);
                        } else {

                            try {
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
                                    registerHandler.sendEmptyMessage(0xffff);
                                } else {
                                    registerHandler.sendEmptyMessage(0x0000);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
    }
}
