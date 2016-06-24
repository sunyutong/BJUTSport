package com.bjutsport.bjutsport;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.bjutsport.aes.AESUtil;
import com.bjutsport.enums.Key;
import com.bjutsport.enums.WSInfo;
import com.bjutsport.enums.WSMethod;

import java.net.SocketTimeoutException;

public class RegisterActivity extends BaseActivity {


    /**
     * 常量定义
     * */

    //显示密码前后不一致
    private static final int SHOW_PASSWORD_INCONSISTENT = 0x0000;
    //显示注册成功
    private static final int SHOW_REGISTER_SUCCESS = 0x0001;
    //显示注册失败
    private static final int SHOW_REGISTER_FAILED = 0x0002;
    //显示连接超时
    private static final int SHOW_SOCKET_TIMEOUT = 0x0003;
    //显示密码长度过短
    private static final int SHOW_PASSWORD_TOO_SHORT = 0x0004;
    //跳转到LoginActivity
    private static final int JUMP_TO_USER_ACTIVITY = 0x0005;

    //密码长度:8位
    private static final int passwordLength = 8;

    //注册成功
    private static final int REGISTER_SUCCESS = 1;
    //注册失败
    private static final int REGISTER_FAILED = 0;


    /**
     * 控件声明
     * */

    //后退按钮
    Button button_back;
    //注册按钮
    Button button_register;
    //密码输入栏
    private EditText ediUserPassword;
    //密码输入确认栏
    private EditText ediUserPasswordCheck;
    
    
    /**
     * UI线程
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);


        /**
         * 控件绑定
         * Handler定义
         * Bundle定义
         * */

        //后退按钮
        button_back = (Button) findViewById(R.id.Button_RegisterActivity_to_MainActivity);
        //注册按钮
        button_register = (Button) findViewById(R.id.Button_Register_Verification);

        //从VerificationActivity传过来的Bundle，其中包含了用户名(手机号)信息
        final Bundle bundle = this.getIntent().getExtras();


        /**
         * UI设定
         * */

        //设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        /**
         * 点击事件
         * */

        //点击返回按钮->回到VerificationActivity
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击注册按钮，创建新的线程以进行网络访问
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * 子线程：网络访问
                 * */

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        /**
                         * 子线程控件绑定
                         * */

                        //密码输入栏
                        ediUserPassword = (EditText) findViewById(R.id.EditText_Register_userPassword);
                        //密码输入确认栏
                        ediUserPasswordCheck = (EditText) findViewById(R.id.EditText_Register_userPassword_Check);

    
                        /** 
                         * 提取用户输入的用户名(from Bundle send by VerificationActivity)和密码(from EditText)
                         * */
                        
                        //提取用户输入的用户名
                        String strUserName = bundle.getString("phoneNums");
                        //提取用户输入的密码
                        String strUserPassword = ediUserPassword.getText().toString();
                        //提取用户输入的和二次密码
                        String strUserPasswordCheck = ediUserPasswordCheck.getText().toString();

                        
                        /** 
                         * 对用户输入的密码进行错误检测与提醒
                         * */
                        
                        if (!checkPasswordLength(strUserPassword)) {
                            //如果用户输入的密码长度小于8,发送消息显示
                            registerHandler.sendEmptyMessage(SHOW_PASSWORD_TOO_SHORT);
                        } else if (!strUserPassword.equals(strUserPasswordCheck)) {
                            //如果用户输入的密码前后不一致,发送消息显示密码前后不一致
                            registerHandler.sendEmptyMessage(SHOW_PASSWORD_INCONSISTENT);
                        } else {
                            //否则进行注册
                            try {


                                /**
                                 * 对用户名和密码进行AES加密
                                 * */

                                //加密用户输入的用户名和密码
                                String encryptedUserName = AESUtil.encrypt(Key.AES.getKey(), strUserName);
                                String encryptedUserPassword = AESUtil.encrypt(Key.AES.getKey(), strUserPassword);


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


                                /**
                                 * 从服务器收取数据
                                 * */

                                //获取返回值
                                SoapObject returnedValue = (SoapObject) envelope.bodyIn;
                                //解析返回结果
                                int result = Integer.parseInt(returnedValue.getPropertyAsString(0));


                                /**
                                 * 对返回数据进行处理
                                 * */

                                switch (result) {
                                    case REGISTER_SUCCESS:
                                        //如果服务器返回值为true,则发送消息以显示注册成功
                                        registerHandler.sendEmptyMessage(SHOW_REGISTER_SUCCESS);
                                        registerHandler.sendEmptyMessage(JUMP_TO_USER_ACTIVITY);
                                        break;
                                    case REGISTER_FAILED:
                                        //如果服务器返回值为false,则发送消息以显示注册失败
                                        registerHandler.sendEmptyMessage(SHOW_REGISTER_FAILED);
                                        break;
                                    default:
                                        break;
                                }

                            } catch (SocketTimeoutException ste) {
                                //抛出异常以显示连接超时
                                registerHandler.sendEmptyMessage(SHOW_SOCKET_TIMEOUT);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    //Register的Handler
    final Handler registerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PASSWORD_TOO_SHORT:
                    //显示密码太短
                    Toast.makeText(getApplicationContext(), "密码长度应至少大于8", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_PASSWORD_INCONSISTENT:
                    //显示密码前后不一致
                    Toast.makeText(getApplicationContext(), "密码前后不一致", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_REGISTER_SUCCESS:
                    //显示注册成功
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_REGISTER_FAILED:
                    //显示注册失败
                    Toast.makeText(getApplicationContext(), "用户名已存在,注册失败", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_SOCKET_TIMEOUT:
                    //显示连接超时
                    Toast.makeText(getApplicationContext(), "连接超时,请检查网络连接", Toast.LENGTH_SHORT).show();
                    break;
                case JUMP_TO_USER_ACTIVITY:
                    //跳转到用户界面
                    Intent intent_User = new Intent(RegisterActivity.this, UserActivity.class);
                    startActivity(intent_User);
                    //结束全部活动除了用户界面
                    ActivityCollector.finishAll();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 检查密码长度函数
     * */

    private boolean checkPasswordLength(String password) {
        return password.length() >= passwordLength;
    }


}
