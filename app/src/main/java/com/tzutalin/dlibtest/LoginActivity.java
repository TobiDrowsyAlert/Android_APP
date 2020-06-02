package com.tzutalin.dlibtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tzutalin.dlibtest.user.domain.RequestLoginDTO;
import com.tzutalin.dlibtest.user.model.User;
import com.tzutalin.dlibtest.user.model.UserDTO;

public class LoginActivity extends AppCompatActivity {

    static Context context;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = User.getInstance();
        context = this;

        Button btn_login;
        Button btn_signup;
        EditText editText_login, editText_password;

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signup =(Button)findViewById(R.id.btn_signup);
        editText_login = (EditText)findViewById(R.id.edit_login);
        editText_password = (EditText)findViewById(R.id.edit_password);


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

                RequestLoginDTO requestLoginDTO = new RequestLoginDTO();
                requestLoginDTO.setUserId(userId);
                requestLoginDTO.setUserPassword(userPassword);

                user.setUserId(userId);

                if(!retrofitConnection.requestLogin(requestLoginDTO)){
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, menuEvent.class);
                startActivity(intent);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, signupEvnet.class);
                startActivity(intent);
            }
        });

    }

    static public Context getContext(){
        return context;
    }
}
