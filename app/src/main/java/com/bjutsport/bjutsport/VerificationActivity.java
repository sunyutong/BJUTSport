package com.bjutsport.bjutsport;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

import com.bjutsport.aes.AESUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class VerificationActivity extends BaseActivity implements OnClickListener {
    /**
     * 静态常量
     */
    //AES密钥
    private static final String AES_KEY = "BJUTSport1234567";
<<<<<<< HEAD
    private static final String WEBSERVICE_WSDL_URL = "http://192.168.1.101:8080/BJUTSport/services/RegisterImplPort?wsdl";
=======
    //注册界面URL?
    private static final String WEBSERVICE_WSDL_URL = "http://192.168.1.101:8080/BJUTSport/services/RegisterImplPort?wsdl";
    //注册界面NameSpace?
>>>>>>> refs/remotes/origin/Lichee's-branch
    private static final String WEBSERVICE_NAMESPACE = "http://register.bjutsport.com/";
    //验证验证码界面方法名称:validateUsername
    private static final String METHOD_NAME = "validateUsername";

    //显示手机号已经注册
    private static final int SHOW_PHONE_NUMBER_ALREADY_EXIST = 0x0000;
    //显示手机号不存在
    private static final int SHOW_PHONE_NUMBER_DO_NOT_EXIST = 0x0001;
    //显示连接超时
    private static final int SHOW_SOCKET_TIMEOUT = 0x0002;

    //验证成功
    private static final int VALIDATE_SUCCESS = 1;
    //验证失败
    private static final int VALIDATE_FAILED = 0;

    //时间限制?
    private static final int TIME_LIMIT = 30;
    //剩余时间?
    private static int leftTime = TIME_LIMIT;

    //字符串state?
    private static String state;



    // 手机号输入框
    private EditText inputPhoneEt;
    // 验证码输入框
    private EditText inputCodeEt;
    // 获取验证码按钮
    private Button requestCodeBtn;
    // 注册按钮
    private Button commitBtn;

    /**
     * UI线程 (主线程)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verification);

        /**
         * 控件绑定、Handler、Bundle定义
         * */
        //返回按钮
        Button button_back = (Button) findViewById(R.id.Button_VerificationActivity_to_MainActivity);
        //菜单栏标题
        TextView textView_VerificationTitle = (TextView)findViewById(R.id.TextView_Verification_Title);
        //注册/提交 按钮 (依据state而定)
        Button button_Verification = (Button)findViewById(R.id.Button_Verification_Verification);
        //从MainActivity或LoginActivity传过来的Bundle，其中包含的state字符串决定了activity_verification的UI
        Bundle thisBundle = this.getIntent().getExtras();
        //获取Bundle中的state字符串
        state = thisBundle.getString("state");

        /**
         * UI设定
         * */
        //设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //根据state传进来的信息修改UI设计
        if (state.equals("register")) {
            textView_VerificationTitle.setText("注册");
            button_Verification.setText("注册");
        } else if(state.equals("forgetPassword")) {
            textView_VerificationTitle.setText("验证验证码");
            button_Verification.setText("提交");
        }

        /**
         * 点击事件
         * */
        //点击返回按钮->回到上一级菜单（MainActivity或LoginActivity）
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //启动短信验证功能
        init();
    }

    //短信验证功能入口函数
    private void init() {
        //获取手机号输入框
        inputPhoneEt = (EditText) findViewById(R.id.EditText_Verification_userName);
        //获取验证码输入框
        inputCodeEt = (EditText) findViewById(R.id.EditText_Verfication_verificationCode);
        //获取获取验证码按钮
        requestCodeBtn = (Button) findViewById(R.id.Button_Verification_getVerificationCode);
        //获取验证按钮
        commitBtn = (Button) findViewById(R.id.Button_Verification_Verification);
        //设置按钮的ClickListener
        requestCodeBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

        // 启动短信验证sdk
        SMSSDK.initSDK(this, "1078349557343", "d3f4fb16542a1df1aa3f26ad1708db8b");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        String phoneNums = inputPhoneEt.getText().toString();
        switch (v.getId()) {
            case R.id.Button_Verification_getVerificationCode:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String phoneNums = inputPhoneEt.getText().toString();
                        try {
                            //加密用户输入的用户名
                            String encryptedUserName = AESUtil.encrypt(AES_KEY, phoneNums);

                            //创建一个SoapObject的对象,并指定WebService的命名空间和调用的方法名
                            SoapObject ruquest = new SoapObject(WEBSERVICE_NAMESPACE, METHOD_NAME);

                            //设置调用方法的参数值,添加加密后的用户名与密码
                            ruquest.addProperty("encryptedUserName", encryptedUserName);

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
                                case VALIDATE_SUCCESS:
                                    if (state.equals("register")) {
                                        if (!judgePhoneNums(phoneNums)) {
                                            return;
                                        }
                                        // 2. 通过sdk发送短信验证
                                        SMSSDK.getVerificationCode("86", phoneNums);

                                        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                                        requestCodeBtn.setClickable(false);
                                        handler.sendEmptyMessage(-9);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (leftTime = TIME_LIMIT; leftTime > 0; leftTime--) {
                                                    handler.sendEmptyMessage(-9);
                                                    if (leftTime <= 0) {
                                                        break;
                                                    }
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                handler.sendEmptyMessage(-8);
                                            }
                                        }).start();
                                    } else if (state.equals("forgetPassword")) {
                                        if (!judgePhoneNums(phoneNums)) {
                                            return;
                                        }
                                        //显示用户名不存在
                                        verificationHandler.sendEmptyMessage(SHOW_PHONE_NUMBER_DO_NOT_EXIST);
                                    }
                                    break;
                                case VALIDATE_FAILED:
                                    if (state.equals("register")) {
                                        //显示用户名已存在
                                        verificationHandler.sendEmptyMessage(SHOW_PHONE_NUMBER_ALREADY_EXIST);
                                    } else if (state.equals("forgetPassword")) {
                                        if (!judgePhoneNums(phoneNums)) {
                                            return;
                                        }
                                        // 2. 通过sdk发送短信验证
                                        SMSSDK.getVerificationCode("86", phoneNums);

                                        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                                        requestCodeBtn.setClickable(false);
                                        handler.sendEmptyMessage(-9);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (leftTime = TIME_LIMIT; leftTime > 0; leftTime--) {
                                                    handler.sendEmptyMessage(-9);
                                                    if (leftTime <= 0) {
                                                        break;
                                                    }
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                handler.sendEmptyMessage(-8);
                                            }
                                        }).start();
                                    }
                                    break;
                            }
                        } catch (SocketTimeoutException ste) {
                            //抛出异常以显示连接超时
                            verificationHandler.sendEmptyMessage(SHOW_SOCKET_TIMEOUT);
                            ste.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            case R.id.Button_Verification_Verification:
                SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt.getText().toString());
                createProgressBar();

                break;

        }
    }


    Handler verificationHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_SOCKET_TIMEOUT:
                    //显示连接超时
                    Toast.makeText(getApplicationContext(), "连接超时,请检查网络连接", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_PHONE_NUMBER_ALREADY_EXIST:
                    //显示该手机号已注册
                    Toast.makeText(getApplicationContext(), "该手机号已注册", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_PHONE_NUMBER_DO_NOT_EXIST:
                    //显示该手机号未注册
                    Toast.makeText(getApplicationContext(), "该手机号未注册", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                requestCodeBtn.setText("重新发送(" + leftTime + ")");
            } else if (msg.what == -8) {
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setClickable(true);
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        if (state.equals("register")) {
                            intent = new Intent(VerificationActivity.this, RegisterActivity.class);
                        } else if (state.equals("forgetPassword")) {
                            intent = new Intent(VerificationActivity.this, ChangePasswordActivity.class);
                        }
                        final Bundle nextBundle = new Bundle();
                        String phoneNums = inputPhoneEt.getText().toString();
                        //传送手机号码到RegisterActivity
                        nextBundle.putString("phoneNums", phoneNums);
                        intent.putExtras(nextBundle);
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };


    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * progressbar
     */
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
