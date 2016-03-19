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
import java.net.SocketTimeoutException;

public class LoginActivity extends BaseActivity {


    /**
     * 常量定义
     * */

    //AES密钥
    private static final String AES_KEY = "BJUTSport1234567";
    //登录界面URL
    private static final String WEBSERVICE_WSDL_URL = "http://192.168.1.101:8080/BJUTSport/services/LoginImplPort?wsdl";
    //登录界面域名
    private static final String WEBSERVICE_NAMESPACE = "http://login.bjutsport.com/";
    //登录界面方法名称:login
    private static final String METHOD_NAME = "login";

    //在TextView中显示登录成功
    private static final int SHOW_LOGIN_SUCCESS_IN_TEXTVIEW = 0x0000;
    //在TextView中显示登录失败
    private static final int SHOW_LOGIN_FAILED_IN_TEXTVIEW = 0x0001;
    //套接字连接超时
    private static final int SHOW_SOCKET_TIMEOUT = 0x0002;
    //跳转到UserActivity
    private static final int JUMP_TO_USER_ACTIVITY = 0x0003;

    //登录成功
    private static final int LOGIN_SUCCESS = 1;
    //登录失败
    private static final int LOGIN_FAILED = 0;


    /**
     * 控件声明
     * */

    //后退按钮
    Button button_back;
    //忘记密码按钮
    Button button_forget_password;
    //登录按钮
    Button button_login;
    //用户名输入栏
    private EditText ediUserName;
    //密码输入栏
    private EditText ediUserPassword;

    /**
     * UI线程 (主线程)
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        /**
         * 控件绑定
         * Handler定义
         * */

        //后退按钮
        button_back = (Button) findViewById(R.id.Button_LoginActivity_Back);
        //忘记密码按钮
        button_forget_password = (Button) findViewById(R.id.Button_LoginActivity_Forget_Password);
        //登录按钮
        button_login = (Button) findViewById(R.id.Button_LoginActivity_Login);
        //Login的Handler
        final Handler loginHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_LOGIN_SUCCESS_IN_TEXTVIEW:
                        //显示登录成功
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                        break;
                    case SHOW_LOGIN_FAILED_IN_TEXTVIEW:
                        //显示登录失败
                        Toast.makeText(getApplicationContext(), "登录失败,用户名或密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case SHOW_SOCKET_TIMEOUT:
                        //显示连接超时
                        Toast.makeText(getApplicationContext(), "连接超时,请检查网络连接", Toast.LENGTH_SHORT).show();
                        break;
                    case JUMP_TO_USER_ACTIVITY:
                        Intent intent_User = new Intent(LoginActivity.this, UserActivity.class);
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
         * UI设定
         * */

        //设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        /**
         * 点击事件
         * */

        //点击返回按钮->回到MainActivity
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击忘记密码按钮->进入VerificationActivity（forgetPassword版）
        button_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_verification = new Intent(LoginActivity.this, VerificationActivity.class);
                Bundle bundle = new Bundle();
                String state = "forgetPassword";
                //传送核实状态到VerificationActivity
                bundle.putString("state", state);
                intent_verification.putExtras(bundle);
                startActivity(intent_verification);
            }
        });
        //点击登录按钮，创建新的线程以进行网络访问
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * 子线程:网络访问
                 * */

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        
                        
                        /**
                         * 子线程控件绑定
                         * */

                        //用户名输入栏
                        ediUserName = (EditText) findViewById(R.id.EditText_LoginActivity_UserName);
                        //密码输入栏
                        ediUserPassword = (EditText) findViewById(R.id.EditText_LoginActivity_UserPassword);
                        
                        
                        /**
                         * 提取用户输入的用户名和密码
                         * */
                        
                        String strUserName = ediUserName.getText().toString();
                        String strUserPassword = ediUserPassword.getText().toString();

                        
                        /** 
                         * 对用户输入的用户名和密码进行AES加密
                         * */

                        try {
                            //加密用户输入的用户名和密码
                            String encryptedUserName = AESUtil.encrypt(AES_KEY, strUserName);
                            String encryptedUserPassword = AESUtil.encrypt(AES_KEY, strUserPassword);

                            
                            /** 
                             * 向服务器发出数据
                             * */
                        
                            //创建一个SoapObject的对象,并指定WebService的命名空间和调用的方法名
                            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, METHOD_NAME);

                            //设置调用方法的参数值,添加加密后的用户名与密码
                            request.addProperty("encryptedUserName", encryptedUserName);
                            request.addProperty("encryptedUserPassword", encryptedUserPassword);

                            //创建HttpTransportSE对象,并通过HttpTransportSE类的构造方法指定Webservice的WSDL文档的URL
                            HttpTransportSE ht = new HttpTransportSE(WEBSERVICE_WSDL_URL, 1000);

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
                                case LOGIN_SUCCESS:
                                    loginHandler.sendEmptyMessage(SHOW_LOGIN_SUCCESS_IN_TEXTVIEW);
                                    loginHandler.sendEmptyMessage(JUMP_TO_USER_ACTIVITY);
                                    break;
                                case LOGIN_FAILED:
                                    //如果服务器返回值为false,则发送消息以显示登陆失败
                                    loginHandler.sendEmptyMessage(SHOW_LOGIN_FAILED_IN_TEXTVIEW);
                                    break;
                                default:
                                    break;
                            }

                        } catch (SocketTimeoutException ste) {
                            //抛出异常以显示连接超时
                            loginHandler.sendEmptyMessage(SHOW_SOCKET_TIMEOUT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

}
