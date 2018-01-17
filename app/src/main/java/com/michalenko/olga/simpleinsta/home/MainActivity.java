package com.michalenko.olga.simpleinsta.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michalenko.olga.simpleinsta.R;
import com.michalenko.olga.simpleinsta.auth.LoginActivity;
import com.michalenko.olga.simpleinsta.home.fragments.ProfileFragment;
import com.michalenko.olga.simpleinsta.home.fragments.TimelineFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private ProfileFragment fragmentProfile;
    private TimelineFragment fragmentTimeline;
    FragmentTransaction ft;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_timeline:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.containerFragment,fragmentTimeline);
                    ft.commit();
                    return true;
                case R.id.navigation_foto:{
                    Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                    startActivity(intent);
                }
                case R.id.navigation_profile:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.containerFragment, fragmentProfile);
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentProfile = new ProfileFragment();
        fragmentTimeline = new TimelineFragment();

         ft = getSupportFragmentManager().beginTransaction();
         ft.replace(R.id.containerFragment, fragmentProfile);
         ft.commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth = FirebaseAuth.getInstance();
               mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
