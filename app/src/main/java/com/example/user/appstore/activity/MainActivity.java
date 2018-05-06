package com.example.user.appstore.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.user.appstore.R;
import com.example.user.appstore.fragment.ListAppFragment;
import com.example.user.appstore.fragment.HomeFragment;
import com.example.user.appstore.fragment.OurAppFragment;
import com.example.user.appstore.fragment.UserManagerFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("General Application");
//        }



        BottomNavigationView memu = (BottomNavigationView) findViewById(R.id.menu);
        memu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeFragment(new HomeFragment());

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.b1home:
                    setTitle("EggInHomeApp");
                    changeFragment(new HomeFragment());
                    return true;
                case R.id.b2data:
                    setTitle("List Applications");
                    changeFragment(new ListAppFragment());
                    return true;
                case R.id.b3app:
                    setTitle("Our Application");
                    changeFragment(new OurAppFragment());
                    return true;
                case R.id.b4user:
                    setTitle("User Manager");
                    changeFragment(new UserManagerFragment());
                    //TODO ต้องแก้
                    return true;
            }
            return false;
        }
    };


    private void changeFragment(Fragment fm){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_min, fm);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
