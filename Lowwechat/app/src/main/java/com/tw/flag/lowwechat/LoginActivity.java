package com.tw.flag.lowwechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by CHENGXUYUAN on 2017/5/11.
 */

public class LoginActivity extends Activity {

    private EditText accountEdit;
    private EditText pswEdit;
    private TextView accountShow;
    private TextView pswShow;
    private Button loginBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        findviews();
    }

    private void findviews(){


        accountEdit = (EditText)findViewById(R.id.account_input);
        pswEdit = (EditText)findViewById(R.id.code_input);
        loginBtn = (Button)findViewById(R.id.login);


        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               login();
            }
        });
    }
   private void login(){


       LoginInfo info = new LoginInfo(accountEdit.getText().toString().toLowerCase(),
               pswEdit.getText().toString()); // config...
       RequestCallback<LoginInfo> callback =
               new RequestCallback<LoginInfo>() {
                   @Override
                   public void onSuccess(LoginInfo param) {
                       Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       String data = accountEdit.getText().toString().toLowerCase();
                       intent = new Intent(LoginActivity.this,MainActivity.class);
                       intent.putExtra("extra_data",data);
                       startActivity(intent);
                       finish();
                   }

                   @Override
                   public void onFailed(int code) {
                       Toast.makeText(LoginActivity.this,"用户名或密码不正确",
                               Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onException(Throwable exception) {

                   }
                   // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
               };
              NIMClient.getService(AuthService.class).login(info)
               .setCallback(callback);
   }

}
