package com.imooc.res.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imooc.res.R;
import com.imooc.res.UserInfoHolder;
import com.imooc.res.bean.User;
import com.imooc.res.biz.UserBiz;
import com.imooc.res.net.CommonCallback;
import com.imooc.res.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

public class LoginActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mBtnRegister;

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";

    private UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtUsername = (EditText) findViewById(R.id.id_et_username);
        mEtPassword = (EditText) findViewById(R.id.id_et_password);
        mBtnLogin = (Button) findViewById(R.id.id_btn_login);
        mBtnRegister = (TextView) findViewById(R.id.id_btn_register);

        initIntent();


        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast("账号或者密码不能为空");
                    return;
                }
                startLoadingProgress();
                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User user) {
                        stopLoadingProgress();
                        T.showToast("登录成功");
                        UserInfoHolder.getInstance().setUser(user);
                        toOrderActivity();
                        finish();
                    }
                });
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterActivity();
            }
        });

    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra(KEY_USERNAME);
            if (username != null) {
                mEtUsername.setText(username);
            }
            String password = intent.getStringExtra(KEY_PASSWORD);
            if (password != null) {
                mEtPassword.setText(password);
            }
        }
    }

    private void toOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }
}
