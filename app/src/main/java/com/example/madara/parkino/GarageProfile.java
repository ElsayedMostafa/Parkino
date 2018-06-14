package com.example.madara.parkino;

import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.example.madara.parkino.adapters.ProfilePageAdapter;
import com.example.madara.parkino.adapters.ViewPagerAdapter;

public class GarageProfile extends AppCompatActivity {
    private static final String TAG = "GarageProfile";
    ViewPager imageViewPager, profileViewPager;
    TabLayout tabLayout;
    ProfilePageAdapter profilePageAdapter;
    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_profile);
        imageViewPager = (ViewPager) findViewById(R.id.viewpager_image_garage);
        profileViewPager = (ViewPager) findViewById(R.id.profile_viewpager_garage);
        tabLayout = (TabLayout) findViewById(R.id.garage_profile_tab_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        profilePageAdapter = new ProfilePageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        profilePageAdapter.addFragment(new DetailsFragment(), "Details");
        profilePageAdapter.addFragment( new ReserveFragment(), "Reserve");
        profilePageAdapter.addFragment(new MapFragment(), "Map");
        profileViewPager.setAdapter(profilePageAdapter);
        tabLayout.setupWithViewPager(profileViewPager);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("garageimgae") && getIntent().hasExtra("garagename")) {
            String url = getIntent().getStringExtra("garageimgae");
            String name = getIntent().getStringExtra("garagename");
            setContent(url, name);
        }
    }

    private void setContent(String url, String name) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageUrls, width, 350);
        imageViewPager.setAdapter(viewPagerAdapter);

//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentimage == imageUrls.length-1) {
//                    currentimage = 0;
//                }
//                _viewpage_garage.setCurrentItem(currentimage++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer .schedule(new TimerTask() { // task to be scheduled
//
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);

        getSupportActionBar().setTitle(name);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
