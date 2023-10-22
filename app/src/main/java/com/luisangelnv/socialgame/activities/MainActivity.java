package com.luisangelnv.socialgame.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.luisangelnv.socialgame.R;
import com.luisangelnv.socialgame.fragments.Home;
import com.luisangelnv.socialgame.providers.AuthProviders;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    TextView mtextViewPageRegister;
    TextInputEditText mTextInputEmail, mTextInputPassword;
    Button mBtnLogin;
    AuthProviders mAuthProvider;
    SignInButton mBtnLoginGoogle;
    AlertDialog mDialog;
    //Google SignIn
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtextViewPageRegister = findViewById(R.id.textViewPageRegister);
        mTextInputEmail = findViewById(R.id.TxtInputEmail);
        mTextInputPassword = findViewById(R.id.TxtInputPassword);
        mBtnLogin = findViewById(R.id.BtnLogin);

        mAuthProvider = new AuthProviders();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento, por favor...")
                .setCancelable(false).build();

        mBtnLogin.setOnClickListener(view -> login());

        mtextViewPageRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PageRegister.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuthProvider.getUserSession() != null){
            Intent intent = new Intent(MainActivity.this, PageHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void login(){
        String email = Objects.requireNonNull(mTextInputEmail.getText()).toString();
        String Password = Objects.requireNonNull(mTextInputPassword.getText()).toString();
        mDialog.show();
        try {
            mAuthProvider.login(email, Password).addOnCompleteListener(task -> {
                mDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(MainActivity.this, PageHome.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Oh no!!!\n\rAlgún campo de usuario o contraseña es incorrecto", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d( "Campo", "Email "+ email);
            Log.d(  "Campo", "Password "+ Password);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}