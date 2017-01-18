package com.androdev.companion;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinate);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = HomeFragment.newInstance();
        //First Run Check
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            fragment = EntryFragment.newInstance();
            //Starting the Home Page
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frag, fragment)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frag, fragment)
                    .addToBackStack(null)
                    .commit();
            Toast.makeText(getBaseContext(),"Press back when done entering details",Toast.LENGTH_LONG).show();
        } else {
            //Starting the Home Page
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frag, fragment)
                    .commit();

        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

        //Setting Timer
        final TimeHandler timeHandler = new TimeHandler();
        final TextView timeViewer = (TextView) findViewById(R.id.time_text);
        timeViewer.setText(timeHandler.timeupdate());

        Thread th = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeViewer.setText(timeHandler.timeupdate());
                        }
                    });
                }
            }
        };
        th.start();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.entry) {
            Fragment fragment = EntryFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.main_frag, fragment)
                    .addToBackStack("null")
                    .commit();

        } else if (id == R.id.weebly) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Semester")
                    .setItems(R.array.choice_weebly, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                builder.setShowTitle(true);
                                builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(MainActivity.this,
                                        Uri.parse("http://srmnotes.weebly.com/odd-semester-2015-16.html"));
                            } else if (which == 1) {
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                builder.setShowTitle(true);
                                builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(MainActivity.this,
                                        Uri.parse("http://srmnotes.weebly.com/even-semester-2015-16.html"));
                            } else if (which == 2) {
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                builder.setShowTitle(true);
                                builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(MainActivity.this,
                                        Uri.parse("http://srmnotes.weebly.com/odd-semester-2016-17.html"));
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.shuchita) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(MainActivity.this, Uri.parse(getString(R.string.ishucita)));

        } else if (id == R.id.academia) {
            Fragment fragment = AcademiaFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.main_frag, fragment)
                    .addToBackStack("null")
                    .commit();

        } else if (id == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.help_amp_support).setItems(R.array.help_support, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        Uri uri = Uri.parse(getString(R.string.app_link));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else if (i == 1) {
                        Toast.makeText(getBaseContext(), R.string.help1,
                                Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Toast.makeText(getBaseContext(), R.string.help2,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.support) {
            Uri uri = Uri.parse(getString(R.string.techcheckone));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void card1(View view) {
        Fragment fragment = DayFragment1.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void card2(View view) {
        Fragment fragment = DayFragment2.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void card3(View view) {
        Fragment fragment = DayFragment3.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();

    }

    public void card4(View view) {
        Fragment fragment = DayFragment4.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void card5(View view) {
        Fragment fragment = DayFragment5.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void news(View view) {
        Fragment fragment = NewsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void event(View view) {
        Fragment fragment = EventsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }
}