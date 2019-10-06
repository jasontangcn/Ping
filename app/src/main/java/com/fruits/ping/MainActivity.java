package com.fruits.ping;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private BottomNavigationView navigation;
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
          case R.id.navigation_messages:
            viewPager.setCurrentItem(0);
            return true;
          case R.id.navigation_contacts:
            viewPager.setCurrentItem(1);
            return true;
          case R.id.navigation_vlogs:
            viewPager.setCurrentItem(2);
            return true;
          case R.id.navigation_settings:
            viewPager.setCurrentItem(3);
            return true;
        }
        return false;
      }
    });

    viewPager = (ViewPager) findViewById(R.id.viewPager);
    final ArrayList<Fragment> fragments = new ArrayList<>(3);
    fragments.add(new MessagesFragment());
    fragments.add(new ContactsFragment());
    fragments.add(new VlogsFragment());
    fragments.add(new SettingsFragment());
    FragmentPagerAdapter viewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        return fragments.get(position);
      }

      @Override
      public int getCount() {
        return fragments.size();
      }
    };
    viewPager.setAdapter(viewPagerAdapter);
    viewPager.setOffscreenPageLimit(2); // preload next two pages

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        navigation.getMenu().getItem(position).setChecked(true);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }
}
