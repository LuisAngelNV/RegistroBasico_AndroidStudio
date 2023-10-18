package com.luisangelnv.socialgame.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProviders {
    private FirebaseAuth mAuth;
    public AuthProviders(){
        mAuth = FirebaseAuth.getInstance();
    }
    public Task<AuthResult> login(String email, String password){
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public String getUid(){
        if (mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser().getUid();
        }else{
            return null;
        }
    }

    public Task<AuthResult> Register(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

}
