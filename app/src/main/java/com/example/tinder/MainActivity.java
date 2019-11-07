package com.example.tinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());


        mViewPager =  findViewById(R.id.container_page);
        setupViewPager(mViewPager);



        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorHeight(0);

        tabLayout.getTabAt(0).setIcon(R.drawable.profileicon);
        tabLayout.getTabAt(1).setIcon(R.drawable.tinderlogo);
        tabLayout.getTabAt(2).setIcon(R.drawable.messageicon);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:tab.setIcon(R.drawable.userselectedicon);
                        break;
                    case 1:tab.setIcon(R.drawable.tinderlogo);
                        break;
                    case 2:tab.setIcon(R.drawable.messageiconselected);
                        break;
                }



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:tab.setIcon(R.drawable.profileicon);
                        break;
                    case 1:tab.setIcon(R.drawable.tinderlogogselected);
                        break;
                    case 2:tab.setIcon(R.drawable.messageicon);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });


    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment());
        adapter.addFragment(new Tab2Fragment());
        adapter.addFragment(new Tab3Fragment());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    public void clearBackStackInclusive() {
        finish();
    }




}
