package com.luisangelnv.socialgame.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luisangelnv.socialgame.R;
import com.luisangelnv.socialgame.activities.MainActivity;
import com.luisangelnv.socialgame.activities.Post;
import com.luisangelnv.socialgame.providers.AuthProviders;

import java.util.Objects;

public class Home extends Fragment {
    View mView;
    FloatingActionButton mFAB;
    Toolbar mToolbar;
    AuthProviders mAuthProvider;
        public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFAB = mView.findViewById(R.id.FAB);
        mToolbar = mView.findViewById(R.id.ToolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Publicaciones");
        setHasOptionsMenu(true);
        mAuthProvider = new AuthProviders();
        mFAB.setOnClickListener(view -> goToPost());
        return mView;
    }

    private void goToPost() {
            Intent intent = new Intent(getContext(), Post.class);
            startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.itemLogout){
                logOut();
            }
        return true;
    }

    private void logOut() {
            mAuthProvider.logOut();
            Intent intent = new Intent(getContext(), MainActivity.class);
            //Limpia el historial de navegación
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }
}