package com.onedongua.abutton;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.textfield.TextInputLayout;
import com.onedongua.abutton.databinding.AcitvityLoginBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.LoginData;
import com.onedongua.abutton.model.UserInfo;
import com.onedongua.abutton.util.JsonUtils;
import com.onedongua.abutton.util.MD5Utils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private AcitvityLoginBinding binding;
    private TextInputLayout loginEmail;
    private TextInputLayout loginPassword;
    private TextView loginButtonText;
    private TextView loginForgot;
    private TextView loginRegister;
    private ServerManager serverManager;

    // 正则表达式用于验证邮箱
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AcitvityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        serverManager = ServerManager.getInstance();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.login);
            actionBar.setDisplayHomeAsUpEnabled(true); // 启用返回按钮
        }

        loginEmail = binding.loginEmail;
        loginPassword = binding.loginPassword;
        loginButtonText = binding.loginButtonText;

        loginButtonText.setOnClickListener(this::onLoginButtonTextClick);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void onLoginButtonTextClick(View v) {
        String email = loginEmail.getEditText().getText().toString();
        String password = loginPassword.getEditText().getText().toString();

        boolean isValid = true;

        // 检查邮箱是否合规
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            loginEmail.setErrorEnabled(true);
            loginEmail.setError(getText(R.string.email_invalid));
            isValid = false;
        }

        // 检查密码是否大于6位
        if (password.length() < 6) {
            loginPassword.setErrorEnabled(true);
            loginPassword.setError(getText(R.string.password_invalid));
            isValid = false;
        }

        new Handler().postDelayed(() -> {
            loginEmail.setErrorEnabled(false);
            loginPassword.setErrorEnabled(false);
        }, 2000);

        if (isValid) {
            login(email, password);
        }
    }

    private void login(String email, String password) {
        String md5Password = MD5Utils.md5(password + ":" + email);

        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json;
        try {
            json = JsonUtils.toJson(new LoginData(email, md5Password));
        } catch (JsonProcessingException e) {
            Log.e(TAG, "login: ", e);
            print(R.string.unknown_error);
            return;
        }

        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(serverManager.getServer() + "api/login")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
                runOnUiThread(() -> print(R.string.login_failed));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            UserInfo userInfo = JsonUtils.fromJson(response.body().string(), UserInfo.class);
                            runOnUiThread(() -> loginSuccess(userInfo));
                        }
                    } else {
                        if (response.code() == 401) {
                            runOnUiThread(() -> print(R.string.login_incorrect));
                        } else {
                            if (response.body() != null) {
                                print(response.body().string());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: ", e);
                    runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });
    }

    private void loginSuccess(UserInfo userInfo) {
        try {
            JsonUtils.toJsonFile(userInfo, new File(getFilesDir(), "user.json"));
            print(R.string.login_success);
            finish();
        } catch (IOException e) {
            print(R.string.unknown_error);
            Log.e(TAG, "loginSuccess: ", e);
        }
    }
}
