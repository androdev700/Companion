package com.androdev.companion;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView time;
    String day;
    int temp,j;
    String t;
    SharedPreferences year;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeupdate();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        th.start();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        year = getSharedPreferences("year",MODE_PRIVATE);
        time = (TextView) findViewById(R.id.textView32);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            startActivity(new Intent(MainActivity.this, entery.class));
            Toast.makeText(MainActivity.this, "Thanks For Downloading!", Toast.LENGTH_LONG)
                    .show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HHmm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime = date.format(currentLocalTime);
        DateFormat df = new SimpleDateFormat("EEE");
        day = df.format(Calendar.getInstance().getTime());
        j = Integer.parseInt(localTime.replaceAll("[\\D]", ""));

        if (day.equals("Sat")||day.equals("Sun"))
            time.setText("Enjoy The Weekend");
        else
        if (j > 0 && j <= 500) {
            time.setText("Have a Good Night!");
        } else if (j > 500 && j <= 700) {
            time.setText("Good Morning");
        } else if (j > 700 && j < 800) {
            temp = 800 - j - 40;
            t = Integer.toString(temp);
            time.setText("College will start in " + t + " Mins.");
        } else if (j > 800 && j <= 850) {
            temp = 850 - j;
            t = Integer.toString(temp);
            time.setText("Hour 1 will finish in " + t + " Mins.");
        } else if (j > 850 && j <= 940) {
            if (j < 900) {
                temp = 940 - j - 40;
            } else {
                temp = 940 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 2 will finish in " + t + " Mins.");
        } else if (j > 940 && j <= 945) {
            temp = 945 - j;
            t = Integer.toString(temp);
            time.setText("Hour 3 will start in " + t + " Mins");
        } else if (j > 945 && j <= 1035) {
            if (j < 1000) {
                temp = 1035 - j - 40;
            } else {
                temp = 1035 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 3 will finish in " + t + " Mins.");
        } else if (j > 1035 && j <= 1040) {
            temp = 1040 - j;
            t = Integer.toString(temp);
            time.setText("Hour 4 will start in " + t + " Mins");
        } else if (j > 1040 && j <= 1130) {
            if (j < 1100) {
                temp = 1130 - j - 40;
            } else {
                temp = 1130 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 4 will finish in " + t + " Mins.");
        } else if (j > 1130 && j <= 1230) {
            time.setText("Go get some food!");
        } else if (j > 1230 && j <= 1320) {
            if (j < 1300) {
                temp = 1320 - j - 40;
            } else {
                temp = 1320 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 6 will finish in " + t + " Mins.");
        } else if (j > 1320 && j <= 1325) {
            temp = 1325 - j;
            t = Integer.toString(temp);
            time.setText("Hour 7 will start in " + t + " Mins");
        } else if (j > 1325 && j <= 1415) {
            if (j < 1400) {
                temp = 1415 - j - 40;
            } else {
                temp = 1415 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 7 will finish in " + t + " Mins.");
        } else if (j > 1415 && j <= 1420) {
            temp = 1420 - j;
            t = Integer.toString(temp);
            time.setText("Hour 8 will start in " + t + " Mins");
        } else if (j > 1420 && j <= 1510) {
            if (j < 1500) {
                temp = 1510 - j - 40;
            } else {
                temp = 1510 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 8 will finish in " + t + " Mins.");
        } else if (j > 1510 && j <= 1515) {
            temp = 1515 - j;
            t = Integer.toString(temp);
            time.setText("Hour 9 will start in " + t + " Mins");
        } else if (j > 1515 && j <= 1605) {
            if (j < 1600) {
                temp = 1605 - j - 40;
            } else {
                temp = 1605 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 9 will finish in " + t + " Mins.");
        } else if (j > 1605 && j <= 1655) {
            temp = 1655 - j;
            t = Integer.toString(temp);
            time.setText("Hour 10 will finish in " + t + " Mins.");
        } else if (j > 1700 && j < 2200) {
            time.setText("How was your day today?");
        } else {
            time.setText("Hi There!");
        }
    }

    private void timeupdate() {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HHmm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime = date.format(currentLocalTime);
        DateFormat df = new SimpleDateFormat("EEE");
        day = df.format(Calendar.getInstance().getTime());
        j = Integer.parseInt(localTime.replaceAll("[\\D]", ""));

        if (day.equals("Sat")||day.equals("Sun"))
            time.setText("Enjoy The Weekend");
        else
        if (j > 0 && j <= 500) {
            time.setText("Have a Good Night!");
        } else if (j > 500 && j <= 700) {
            time.setText("Good Morning");
        } else if (j > 700 && j < 800) {
            temp = 800 - j - 40;
            t = Integer.toString(temp);
            time.setText("College will start in " + t + " Mins.");
        } else if (j > 800 && j <= 850) {
            temp = 850 - j;
            t = Integer.toString(temp);
            time.setText("Hour 1 will finish in " + t + " Mins.");
        } else if (j > 850 && j <= 940) {
            if (j < 900) {
                temp = 940 - j - 40;
            } else {
                temp = 940 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 2 will finish in " + t + " Mins.");
        } else if (j > 940 && j <= 945) {
            temp = 945 - j;
            t = Integer.toString(temp);
            time.setText("Hour 3 will start in " + t + " Mins");
        } else if (j > 945 && j <= 1035) {
            if (j < 1000) {
                temp = 1035 - j - 40;
            } else {
                temp = 1035 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 3 will finish in " + t + " Mins.");
        } else if (j > 1035 && j <= 1040) {
            temp = 1040 - j;
            t = Integer.toString(temp);
            time.setText("Hour 4 will start in " + t + " Mins");
        } else if (j > 1040 && j <= 1130) {
            if (j < 1100) {
                temp = 1130 - j - 40;
            } else {
                temp = 1130 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 4 will finish in " + t + " Mins.");
        } else if (j > 1130 && j <= 1230) {
            time.setText("Go get some food!");
        } else if (j > 1230 && j <= 1320) {
            if (j < 1300) {
                temp = 1320 - j - 40;
            } else {
                temp = 1320 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 6 will finish in " + t + " Mins.");
        } else if (j > 1320 && j <= 1325) {
            temp = 1325 - j;
            t = Integer.toString(temp);
            time.setText("Hour 7 will start in " + t + " Mins");
        } else if (j > 1325 && j <= 1415) {
            if (j < 1400) {
                temp = 1415 - j - 40;
            } else {
                temp = 1415 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 7 will finish in " + t + " Mins.");
        } else if (j > 1415 && j <= 1420) {
            temp = 1420 - j;
            t = Integer.toString(temp);
            time.setText("Hour 8 will start in " + t + " Mins");
        } else if (j > 1420 && j <= 1510) {
            if (j < 1500) {
                temp = 1510 - j - 40;
            } else {
                temp = 1510 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 8 will finish in " + t + " Mins.");
        } else if (j > 1510 && j <= 1515) {
            temp = 1515 - j;
            t = Integer.toString(temp);
            time.setText("Hour 9 will start in " + t + " Mins");
        } else if (j > 1515 && j <= 1605) {
            if (j < 1600) {
                temp = 1605 - j - 40;
            } else {
                temp = 1605 - j;
            }
            t = Integer.toString(temp);
            time.setText("Hour 9 will finish in " + t + " Mins.");
        } else if (j > 1605 && j <= 1655) {
            temp = 1655 - j;
            t = Integer.toString(temp);
            time.setText("Hour 10 will finish in " + t + " Mins.");
        } else if (j > 1700 && j < 2200) {
            time.setText("How was your day today?");
        } else {
            time.setText("Hi There!");
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.entery) {
            startActivity(new Intent(this, entery.class));

        } else if (id == R.id.nav_do1) {
            startActivity(new Intent(this, dayorder1.class));

        } else if (id == R.id.nav_do2) {
            startActivity(new Intent(this, dayorder2.class));

        } else if (id == R.id.nav_do3) {
            startActivity(new Intent(this, dayorder3.class));

        } else if (id == R.id.nav_do4) {
            startActivity(new Intent(this, dayorder4.class));

        } else if (id == R.id.nav_do5) {
            startActivity(new Intent(this, dayorder5.class));

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
            customTabsIntent.launchUrl(MainActivity.this, Uri.parse("http://ishuchita.com/"));

        } else if (id == R.id.academia) {
            startActivity(new Intent(this, academia.class));

        } else if (id == R.id.nav_news) {
            startActivity(new Intent(this, news.class));

        } else if (id == R.id.nav_events) {
            startActivity(new Intent(this, events.class));

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Play Store");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.androdev.companion");
            startActivity(Intent.createChooser(shareIntent, "Share"));

        } else if (id == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.help_amp_support).setItems(R.array.help_support, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome&hl=en");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else if (i == 1) {
                        Toast.makeText(getBaseContext(),"Goto any day order and give it a try!",Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Toast.makeText(getBaseContext(),"Don't forget to press save after entering the data",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.support) {
            Uri uri = Uri.parse("http://www.facebook.com/techcheckone");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }  else if (id == R.id.nav_about) {
            startActivity(new Intent(this, about.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}