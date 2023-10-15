package com.luisangelnv.socialgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    //Declarar tipo varibales para asignar despues
    TextView mtextViewPageRegister;
    TextInputEditText mTextInputEmail, mTextInputPassword;
    Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mandar a llamar las variables declaradas por ID
        mtextViewPageRegister = findViewById(R.id.textViewPageRegister);
        mTextInputEmail = findViewById(R.id.TxtInputEmail);
        mTextInputPassword = findViewById(R.id.TxtInputPassword);
        mBtnLogin = findViewById(R.id.BtnLogin);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mtextViewPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PageRegister.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        String email = mTextInputEmail.getText().toString();
        String Password = mTextInputPassword.getText().toString();
        Log.d( "Campo", "Email "+ email);
        Log.d(  "Campo", "Password "+ Password);
    }

}