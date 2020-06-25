package com.tzutalin.dlibtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tzutalin.dlibtest.domain.RequestRegisterDTO;

import java.util.regex.Pattern;

import retrofit2.Retrofit;

public class signupEvnet extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Button btn_join = (Button)findViewById(R.id.join);
        EditText userId = (EditText)findViewById(R.id.user_id);
        EditText userPassword = (EditText)findViewById(R.id.user_password);
        EditText userPasswordConfirm = (EditText)findViewById(R.id.user_password_confirm);
        EditText userName = (EditText)findViewById(R.id.user_name);
        EditText userEmail = (EditText)findViewById(R.id.user_email);



        btn_join.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = userId.getText().toString();
                String password = userPassword.getText().toString();
                String passwordConfirm = userPasswordConfirm.getText().toString();
                String name = userName.getText().toString();
                String email = userEmail.getText().toString();

                RequestRegisterDTO requestRegisterDTO = new RequestRegisterDTO(id, password, passwordConfirm, email, name);

                if(!isCorrectNamingRule(requestRegisterDTO)){
                    return;
                }

                RetrofitConnection retrofitConnection = RetrofitConnection.getInstance();
                retrofitConnection.requestRegitser(requestRegisterDTO);

                Intent intent = new Intent(signupEvnet.this , LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean isCorrectNamingRule(RequestRegisterDTO requestRegisterDTO){
        if(!Pattern.matches("^[a-zA-Z0-9]{6,18}$", requestRegisterDTO.getUserId())) {
            Toast.makeText(this, "아아디는 6~18글자로 영문자와 숫자만 입력해야합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!Pattern.matches("^[a-zA-Z0-9~`!@#$%&*()-]{6,18}$", requestRegisterDTO.getUserPassword())) {
            Toast.makeText(this, "비밀번호는 6~18글자로 입력해야합니다..", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!requestRegisterDTO.getUserPasswordConfirm().equals(requestRegisterDTO.getUserPassword())){
            Toast.makeText(this, "비밀번호 확인을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
