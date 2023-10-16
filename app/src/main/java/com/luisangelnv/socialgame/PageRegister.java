package com.luisangelnv.socialgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class PageRegister extends AppCompatActivity {

    CircleImageView mcircleImageBack;
    TextInputEditText mTxtInpNameUser, mtxtInpEmailUser, mTxtInpPasswordBoxOne, mTxtInpPasswordBoxTwo;
    Button mBtnRegister;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_register);

        mcircleImageBack = findViewById(R.id.circleImageBack);
        mTxtInpNameUser = findViewById(R.id.TxtInpNameUser);
        mtxtInpEmailUser = findViewById(R.id.txtInpEmailUser);
        mTxtInpPasswordBoxOne = findViewById(R.id.TxtInpPasswordBoxOne);
        mTxtInpPasswordBoxTwo = findViewById(R.id.TxtInpPasswordBoxTwo);
        mBtnRegister = findViewById(R.id.BtnRegister);

        mcircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register(){
        String userName = mTxtInpNameUser.getText().toString();
        String userEmail = mtxtInpEmailUser.getText().toString();
        String boxPasswordOne = mTxtInpPasswordBoxOne.getText().toString();
        String boxPasswordTwo = mTxtInpPasswordBoxTwo.getText().toString();

        if (!boxPasswordOne.toString().equals(boxPasswordTwo.toString())) {
            Toast.makeText(this, "Los elementos de contraseña no coinciden", Toast.LENGTH_LONG).show();
        }else{
            if (!userName.isEmpty() && !userEmail.isEmpty() && !boxPasswordOne.isEmpty() && !boxPasswordTwo.isEmpty()){
                if(isEmailValid(userEmail)){

                }else{
                    Toast.makeText(this, "El correo no es valido", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Registro con exito", Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(this, "Oh no... \nAlgo te ha falto llenar algunos campos", Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}