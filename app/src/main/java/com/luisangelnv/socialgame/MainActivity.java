package com.luisangelnv.socialgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    //Declarar tipo varibales para asignar despues
    TextView mtextViewPageRegister;
    TextInputEditText mTextInputEmail, mTextInputPassword;
    Button mBtnLogin;
    FirebaseAuth mAut;
    SignInButton mBtnLoginGoogle;
    //Google SignIn
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mandar a llamar las variables declaradas por ID
        mtextViewPageRegister = findViewById(R.id.textViewPageRegister);
        mTextInputEmail = findViewById(R.id.TxtInputEmail);
        mTextInputPassword = findViewById(R.id.TxtInputPassword);
        mBtnLogin = findViewById(R.id.BtnLogin);
        mAut = FirebaseAuth.getInstance();
        mBtnLoginGoogle = findViewById(R.id.BtnLoginGoogle);

        GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);
        builder.requestIdToken(getString(R.string.default_web_client_id));
        builder.requestEmail();
        GoogleSignInOptions gso = builder
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mBtnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAut.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent  = new Intent(MainActivity.this, PageHome.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void login(){
        String email = mTextInputEmail.getText().toString();
        String Password = mTextInputPassword.getText().toString();
        mAut.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(MainActivity.this, PageHome.class);
                    startActivity(intent);
                }else{
                Toast.makeText(MainActivity.this, "Oh no!!!\n\rAlgún campo de usuario o contraseña es incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d( "Campo", "Email "+ email);
        Log.d(  "Campo", "Password "+ Password);
    }

}