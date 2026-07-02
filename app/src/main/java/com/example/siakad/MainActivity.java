package com.example.siakad;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.siakad.fragment.DashboardFragment;
import com.example.siakad.fragment.NotificationFragment;
import com.example.siakad.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;

    private DashboardFragment dashboardFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;

    private static final String TAG_DASHBOARD = "DASHBOARD";
    private static final String TAG_NOTIFICATION = "NOTIFICATION";
    private static final String TAG_PROFILE = "PROFILE";

    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String npm = getIntent().getStringExtra("KEY_NPM");
        String nama = getIntent().getStringExtra("KEY_NAMA");

        fragmentManager = getSupportFragmentManager();

        initViews();
        initFragments(npm, nama);
        setupBottomNavigation();
        setupBackHandler();

        if (savedInstanceState == null) {
            showFragment(dashboardFragment);
            bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
        }
    }

    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void initFragments(String npm, String nama) {
        dashboardFragment = DashboardFragment.newInstance(npm, nama);
        notificationFragment = new NotificationFragment();
        profileFragment = ProfileFragment.newInstance(npm, nama);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragmentContainer, dashboardFragment, TAG_DASHBOARD);
        transaction.add(R.id.fragmentContainer, notificationFragment, TAG_NOTIFICATION);
        transaction.add(R.id.fragmentContainer, profileFragment, TAG_PROFILE);
        transaction.hide(notificationFragment);
        transaction.hide(profileFragment);
        transaction.commit();

        activeFragment = dashboardFragment;
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            return navigateToBottomItem(itemId);
        });

        bottomNavigation.setOnItemReselectedListener(item -> {
            clearFeatureBackStack();

            if (item.getItemId() == R.id.nav_dashboard) {
                showFragment(dashboardFragment);
            }
        });
    }

    private boolean navigateToBottomItem(int itemId) {
        clearFeatureBackStack();

        if (itemId == R.id.nav_dashboard) {
            showFragment(dashboardFragment);
            return true;
        } else if (itemId == R.id.nav_notification) {
            showFragment(notificationFragment);
            return true;
        } else if (itemId == R.id.nav_profile) {
            showFragment(profileFragment);
            return true;
        }

        return false;
    }

    private void setupBackHandler() {
        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (fragmentManager.getBackStackEntryCount() > 0) {
                            clearFeatureBackStack();
                            showFragment(dashboardFragment);
                            bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
                        } else if (activeFragment != dashboardFragment) {
                            showFragment(dashboardFragment);
                            bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
                        } else {
                            setEnabled(false);
                            getOnBackPressedDispatcher().onBackPressed();
                        }
                    }
                }
        );
    }

    public void openFeatureFragment(Fragment fragment) {
        clearFeatureBackStack();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.slide_out_right
        );

        if (activeFragment != null && activeFragment.isAdded()) {
            transaction.hide(activeFragment);
        }

        transaction.add(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showFragment(Fragment targetFragment) {
        if (activeFragment == targetFragment) return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );

        if (activeFragment != null && activeFragment.isAdded()) {
            transaction.hide(activeFragment);
        }

        if (targetFragment.isAdded()) {
            transaction.show(targetFragment);
        } else {
            transaction.add(R.id.fragmentContainer, targetFragment);
        }

        transaction.commit();
        activeFragment = targetFragment;
    }

    private void clearFeatureBackStack() {
        while (fragmentManager.popBackStackImmediate()) {
        }
    }
}
