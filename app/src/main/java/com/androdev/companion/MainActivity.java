package com.androdev.companion;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androdev.companion.days.DayFragment1;
import com.androdev.companion.days.DayFragment2;
import com.androdev.companion.days.DayFragment3;
import com.androdev.companion.days.DayFragment4;
import com.androdev.companion.days.DayFragment5;
import com.androdev.companion.views.AcademiaFragment;
import com.androdev.companion.views.EntryFragment;
import com.androdev.companion.views.EventsFragment;
import com.androdev.companion.views.HomeOthers;
import com.androdev.companion.views.HomeWhatsNew;
import com.androdev.companion.views.HomeYourTimeTableFragment;
import com.androdev.companion.views.NewsFragment;

public class MainActivity extends AppCompatActivity {

    TextView actionBarTitle;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        actionBarTitle.setText(R.string.home);
        showBottomBar();
    }

    public void hideBottomBar() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomBar() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_details :
                hideBottomBar();
                Fragment fragment = EntryFragment.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_frag, fragment)
                        .addToBackStack("null")
                        .commit();
                break;
            case R.id.help :
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
                break;
            case R.id.support :
                Uri uri = Uri.parse(getString(R.string.techcheckone));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.about :
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(R.string.app_name);
                builder1.setMessage(R.string.version);
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText(R.string.home);

        final Fragment fragment = HomeYourTimeTableFragment.newInstance();
        //First Run Check
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //Starting the Home Page
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frag, fragment)
                    .commit();
            Fragment fragment1 = EntryFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frag, fragment1)
                    .addToBackStack(null)
                    .commit();
            Toast.makeText(getBaseContext(), "Press back when done entering details", Toast.LENGTH_LONG).show();
            hideBottomBar();

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
        timeViewer.setText(timeHandler.timeUpdate());

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
                            timeViewer.setText(timeHandler.timeUpdate());
                        }
                    });
                }
            }
        };
        th.start();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Fragment frag;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_time_table:
                                frag = HomeYourTimeTableFragment.newInstance();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_frag, frag)
                                        .commit();
                                break;
                            case R.id.action_whats_new:
                                frag = HomeWhatsNew.newInstance();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_frag, frag)
                                        .commit();
                                break;
                            case R.id.action_estudy:
                                frag = HomeOthers.newInstance();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_frag, frag)
                                        .commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    public void card1(View view) {
        actionBarTitle.setText(R.string.dayorder1);
        Fragment fragment = DayFragment1.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void card2(View view) {
        actionBarTitle.setText(R.string.dayorder2);
        Fragment fragment = DayFragment2.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void card3(View view) {
        actionBarTitle.setText(R.string.dayorder3);
        Fragment fragment = DayFragment3.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void card4(View view) {
        actionBarTitle.setText(R.string.dayorder4);
        Fragment fragment = DayFragment4.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void card5(View view) {
        actionBarTitle.setText(R.string.dayorder5);
        Fragment fragment = DayFragment5.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void news(View view) {
        actionBarTitle.setText(R.string.announcement);
        Fragment fragment = NewsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void event(View view) {
        actionBarTitle.setText(R.string.events);
        Fragment fragment = EventsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void academia(View view) {
        hideBottomBar();
        Fragment fragment = AcademiaFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
    }

    public void suchita(View view) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(MainActivity.this, Uri.parse(getString(R.string.ishucita)));
    }

    public void weebly(View view) {
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
    }
}