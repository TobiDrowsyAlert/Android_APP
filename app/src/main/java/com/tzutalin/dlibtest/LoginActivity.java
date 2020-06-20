package com.tzutalin.dlibtest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.tzutalin.dlibtest.user.domain.RequestLoginDTO;
import com.tzutalin.dlibtest.user.model.User;
import com.tzutalin.dlibtest.user.model.UserDTO;

public class LoginActivity extends AppCompatActivity {

    static Context context;
    private User user;
    private RequestLoginDTO login_infor;

    static private SharedPreferences LoginPreferences;
    RetrofitConnection retro;

    private final String defualt = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        RequestLoginDTO requestLoginDTO = new RequestLoginDTO();  // 선언 위치 변경함

        LoginPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);

        user = User.getInstance();
        context = this;


        Button btn_login;
        Button btn_signup;
        EditText editText_login, editText_password;

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signup =(Button)findViewById(R.id.btn_signup);
        editText_login = (EditText)findViewById(R.id.edit_login);
        editText_password = (EditText)findViewById(R.id.edit_password);

        String username = LoginPreferences.getString("id", defualt); // ""
        String userpasssword = LoginPreferences.getString("password", defualt);

        editText_login.setText(String.format("%s", username));
        editText_password.setText(String.format("%s",userpasssword));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = editText_login.getText().toString();
                String userPassword = editText_password.getText().toString();

                if(userId.equals("")) {
                    Toast.makeText(LoginActivity.this, "ID 입력 안함", Toast.LENGTH_SHORT).show();
                    userId = "default";
                    return;
                }

                user.setUserDTO(new UserDTO());
                user.getUserDTO().setUserId(userId);
                RetrofitConnection retrofitConnection = new RetrofitConnection();

                //RequestLoginDTO requestLoginDTO = new RequestLoginDTO();  //선언
                requestLoginDTO.setUserId(userId);            // 아이디 저장
                requestLoginDTO.setUserPassword(userPassword);      //비번 저장

                user.setUserId(userId);

                //retro.getUserId();
                if(!retrofitConnection.requestLogin(requestLoginDTO)){
                    return;
                }

            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, signupEvnet.class);
                startActivity(intent);
            }
        });

        editText_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editText_login.setHint("");
                editText_password.setHint("비밀번호");
            }
        });
        editText_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editText_login.setHint("아이디");
                editText_password.setHint("");
            }
        });


    }

    static public Context getContext(){
        return context;
    }


}
