package com.example.madara.parkino;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.madara.parkino.utils.Session;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GaragesFragment())
                .commit();
            navigationView.setCheckedItem(R.id.it_home);
        }
        View headerView = navigationView.getHeaderView(0);
        TextView userNameNav = (TextView) headerView.findViewById(R.id.user_name_nav);
        TextView userEmailNav = (TextView) headerView.findViewById(R.id.user_email_nav);
        userNameNav.setText(Session.getInstance().getUser().username);
        userEmailNav.setText(Session.getInstance().getUser().email);

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
                super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.it_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GaragesFragment())
                        .commit();
                break;
            case R.id.it_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserProfileFragment())
                        .commit();
                break;
            case R.id.it_cards:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserCardsFragment())
                        .commit();
                break;
            case R.id.it_garages:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserGaragesFragment())
                        .commit();
                break;
            case R.id.it_charge:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChargeFragment())
                        .commit();
                break;
            case R.id.it_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment())
                        .commit();
                break;
            case R.id.it_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HelpFragment())
                        .commit();
                break;
            case R.id.it_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FeedbackFragment())
                        .commit();
                break;
            case R.id.it_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutFragment())
                        .commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
