package com.luisangelnv.socialgame.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthProviders {
    private final FirebaseAuth mAuth;
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

    public FirebaseUser getUserSession(){
        if (mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser();
        }else{
            return null;
        }
    }

    public Task<AuthResult> Register(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
    public void logOut(){
        if (mAuth != null){
            mAuth.signOut();
        }
    }
}
