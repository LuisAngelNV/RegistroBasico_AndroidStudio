package com.luisangelnv.socialgame.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.luisangelnv.socialgame.R;
import com.luisangelnv.socialgame.models.User;
import com.luisangelnv.socialgame.providers.AuthProviders;
import com.luisangelnv.socialgame.providers.userProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PageRegister extends AppCompatActivity {
    CircleImageView mcircleImageBack;
    TextInputEditText mTxtInpNameUser, mtxtInpEmailUser, mTxtInpPasswordBoxOne, mTxtInpPasswordBoxTwo;
    Button mBtnRegister;
    AuthProviders mAuthProvider;
    userProvider mUsersProvider;
    AlertDialog mDialog;


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
        mAuthProvider = new AuthProviders();
        mUsersProvider = new userProvider();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento, por favor...")
                .setCancelable(false).build();

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

    private void register() {
        String userName = mTxtInpNameUser.getText().toString();
        String userEmail = mtxtInpEmailUser.getText().toString();
        String boxPasswordOne = mTxtInpPasswordBoxOne.getText().toString();
        String boxPasswordTwo = mTxtInpPasswordBoxTwo.getText().toString();

        if (!userName.isEmpty() && !userEmail.isEmpty() && !boxPasswordOne.isEmpty() && !boxPasswordTwo.isEmpty()) {
            if (isEmailValid(userEmail)) {
                if (boxPasswordOne.equals(boxPasswordTwo)) {
                    if (boxPasswordOne.length() >= 6) {
                        CreateUser(userEmail, boxPasswordOne, userName);
                    } else {
                        Toast.makeText(this, "La contraseña debe tener más de 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Los elementos de contraseña no coinciden", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Correo electrónico incorrecto.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Oh no... \nAlgo te ha falto llenar algunos campos", Toast.LENGTH_LONG).show();
        }

    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void CreateUser(String email, String password, String userName) {
        mDialog.show();
        mAuthProvider.Register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuthProvider.getUid();
                    User user = new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUserName(userName);
                    mUsersProvider.Create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(PageRegister.this, "El registro se ha completado", Toast.LENGTH_SHORT).show();
                                Intent intent  = new Intent(PageRegister.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Toast.makeText(PageRegister.this, "El registro para el usuario.\r\n No se ha podido llevar a cabo. \r\n Intentar más tarde por favor\n\r" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("Error", "Mensaje " + exception.getMessage());
                                }

                            }
                        }
                    });
                }else {
                    mDialog.dismiss();
                    Toast.makeText(PageRegister.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}