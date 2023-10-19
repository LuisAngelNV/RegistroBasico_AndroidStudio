package com.luisangelnv.socialgame.activities;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.luisangelnv.socialgame.R;
import com.luisangelnv.socialgame.databinding.ActivityPageHomeBinding;
import com.luisangelnv.socialgame.fragments.Chats;
import com.luisangelnv.socialgame.fragments.Filters;
import com.luisangelnv.socialgame.fragments.Home;
import com.luisangelnv.socialgame.fragments.Profile;

public class PageHome extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_home);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new Home());
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        ((FragmentTransaction) transaction).addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.ItemHome){
                        openFragment(new Home());
                    } else if (item.getItemId() == R.id.ItemChat) {
                        openFragment(new Chats());
                    } else if (item.getItemId()== R.id.ItemFilters) {
                        openFragment(new Filters());
                    } else if (item.getItemId() == R.id.ItemProfile)  {
                        openFragment(new Profile());
                    }
                return true;
           }
    };
}