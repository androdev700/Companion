package com.androdev.timetable.activities;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androdev.timetable.R;
import com.androdev.timetable.dayorder.DayOrder;
import com.androdev.timetable.days.DayFragment1;
import com.androdev.timetable.days.DayFragment2;
import com.androdev.timetable.days.DayFragment3;
import com.androdev.timetable.days.DayFragment4;
import com.androdev.timetable.days.DayFragment5;
import com.androdev.timetable.handlers.DayOrderHandler;
import com.androdev.timetable.handlers.TimeHandler;
import com.androdev.timetable.labslot.LabSlot;
import com.androdev.timetable.slot.Slot;
import com.androdev.timetable.viewFragments.EntryFragment;
import com.androdev.timetable.viewFragments.EventsFragment;
import com.androdev.timetable.viewFragments.HomeOthers;
import com.androdev.timetable.viewFragments.HomeWhatsNew;
import com.androdev.timetable.viewFragments.HomeYourTimeTableFragment;
import com.androdev.timetable.viewFragments.NewsFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private int count = 0;
    private static final String TAG = "MainActivity";
    private String dayOrder = "Today is ";
    private String mUsername;

    private TextView actionBarTitle;
    private Toolbar toolbar;
    private ImageView view;
    private BottomNavigationView bottomNavigationView;
    private TextView timeViewer;
    private ProgressBar progressBarDayOrder;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseReference;
    private static DatabaseReference userRef;

    final Animation animationFadeIn = new AlphaAnimation(0.0f, 1.0f);
    final Animation animationFadeOut = new AlphaAnimation(1.0f, 0.0f);

    private SharedPreferences pref0, pref1, pref2, pref3, pref4;
    private String[] hourName = new String[]{"hour1", "hour2", "hour3", "hour4", "hour5", "hour6",
            "hour7", "hour8", "hour9", "hour10"};

    private String[] courses = new String[]{"courseA", "courseB", "courseC", "courseD", "courseE", "courseF", "courseG"};
    private String[] labCourses = new String[]{"courseLab1", "courseLab2", "courseLab3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) throws IllegalThreadStateException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First Run Check
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Toast.makeText(getBaseContext(), "Thanks for downloading!", Toast.LENGTH_LONG).show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        pref0 = getSharedPreferences("day1", MODE_PRIVATE);
        pref1 = getSharedPreferences("day2", MODE_PRIVATE);
        pref2 = getSharedPreferences("day3", MODE_PRIVATE);
        pref3 = getSharedPreferences("day4", MODE_PRIVATE);
        pref4 = getSharedPreferences("day5", MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        view = (ImageView) findViewById(R.id.toolbar_image);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        timeViewer = (TextView) findViewById(R.id.time_text);
        progressBarDayOrder = (ProgressBar) findViewById(R.id.progressBarDayOrder);
        progressBarDayOrder.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);

        //Setting Home ActionBarText
        actionBarTitle.setText(R.string.home);
        view.setVisibility(View.GONE);

        //Customizing Animations
        animationFadeIn.setDuration(400);
        animationFadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        animationFadeOut.setDuration(400);
        animationFadeOut.setInterpolator(new AccelerateDecelerateInterpolator());

        Toast.makeText(getBaseContext(), R.string.fetching_day, Toast.LENGTH_SHORT).show();

        //Starting the Home Page
        Fragment fragment = HomeYourTimeTableFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frag, fragment)
                .commit();

        //Setting Timer Thread
        final TimeHandler timeHandler = new TimeHandler();
        final Thread timeThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(13000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeViewer.startAnimation(animationFadeIn);
                            timeViewer.setText(timeHandler.timeUpdate());
                        }
                    });
                }
            }
        };
        timeThread.start();

        //Setting Day Order
        Date date = new Date();
        String day = (String) DateFormat.format("dd", date);
        final String monthNumber = (String) DateFormat.format("MM", date);
        final String todayDate = day + " " + monthNumber;

        //Navigation Switcher
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Fragment frag;
                    FragmentTransaction transaction;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_time_table:
                                frag = HomeYourTimeTableFragment.newInstance();
                                transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_frag, frag).commit();
                                break;
                            case R.id.action_whats_new:
                                frag = HomeWhatsNew.newInstance();
                                transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_frag, frag).commit();
                                break;
                            case R.id.action_estudy:
                                frag = HomeOthers.newInstance();
                                transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_frag, frag).commit();
                                break;
                        }
                        return true;
                    }
                });

        //Auth State Listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUsername = user.getUid();
                    userRef = mDatabaseReference.child(mUsername);

                    DatabaseReference reference = userRef.child("batch");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String data = dataSnapshot.getValue(String.class);
                            if (data != null) {
                                getSharedPreferences("batch", MODE_PRIVATE).edit().putString("batch", data).apply();
                                if ((data.equals("1") || data.equals("2"))) {
                                    Log.d(TAG, "Fetched");
                                }
                            } else {
                                startActivity(new Intent(MainActivity.this, BatchSelector.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference slotRef = userRef.child("slot");
                    slotRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Slot slot = dataSnapshot.getValue(Slot.class);
                            ArrayList<String> course;
                            ArrayList<String> room;
                            if (slot != null) {
                                course = slot.getCourse();
                                room = slot.getRoom();
                                setSlot(course, room);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference slotLabRef = userRef.child("lab_slot");
                    slotLabRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            LabSlot labSlot = dataSnapshot.getValue(LabSlot.class);
                            ArrayList<String> course;
                            ArrayList<String> room;
                            ArrayList<String> time;
                            if (labSlot != null) {
                                course = labSlot.getCourse();
                                room = labSlot.getRoom();
                                time = labSlot.getTime();
                                setLabSlot(course, room, time);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference fetchDayOrder1 = userRef.child("day_order1");
                    fetchDayOrder1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(DayOrder.class) != null) {
                                DayOrder order = dataSnapshot.getValue(DayOrder.class);
                                ArrayList<String> course = null;
                                if (order != null) {
                                    course = order.getCourse();
                                }
                                setData(course, pref0);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference fetchDayOrder2 = userRef.child("day_order2");
                    fetchDayOrder2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(DayOrder.class) != null) {
                                DayOrder order = dataSnapshot.getValue(DayOrder.class);
                                ArrayList<String> course = null;
                                if (order != null) {
                                    course = order.getCourse();
                                }
                                setData(course, pref1);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference fetchDayOrder3 = userRef.child("day_order3");
                    fetchDayOrder3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(DayOrder.class) != null) {
                                DayOrder order = dataSnapshot.getValue(DayOrder.class);
                                ArrayList<String> course = null;
                                if (order != null) {
                                    course = order.getCourse();
                                }
                                setData(course, pref2);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference fetchDayOrder4 = userRef.child("day_order4");
                    fetchDayOrder4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(DayOrder.class) != null) {
                                DayOrder order = dataSnapshot.getValue(DayOrder.class);
                                ArrayList<String> course = null;
                                if (order != null) {
                                    course = order.getCourse();
                                }
                                setData(course, pref3);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference fetchDayOrder5 = userRef.child("day_order5");
                    fetchDayOrder5.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(DayOrder.class) != null) {
                                DayOrder order = dataSnapshot.getValue(DayOrder.class);
                                ArrayList<String> course = null;
                                if (order != null) {
                                    course = order.getCourse();
                                }
                                setData(course, pref4);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    /*
                    DatabaseReference checkReference = mDatabaseReference.child("CheckOrder");
                    int day = 1;
                    int month = 10;
                    String dayString;
                    int order = 5;
                    for (; day < 32; day++) {
                        if (day < 10) {
                            dayString = "0".concat(Integer.toString(day));
                        } else {
                            dayString = Integer.toString(day);
                        }
                        if (order > 5) {
                            order = 1;
                        }
                        if (day == 1 || day == 2 || day == 7 || day == 8 || day == 14 || day == 15 || day == 18 || day == 21 || day == 22 || day == 28 || day == 29) {
                            DatabaseReference hello = checkReference.child(dayString.concat(" ").concat(Integer.toString(month)));
                            hello.setValue("a holiday");
                        } else {
                            DatabaseReference hello = checkReference.child(dayString.concat(" ").concat(Integer.toString(month)));
                            hello.setValue(order++);
                        }
                    }
                    */

                    DayOrderHandler orderHandler = new DayOrderHandler();
                    DatabaseReference dayRef = orderHandler.initDatabase(todayDate);
                    dayRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot.getValue(Long.class) != null) {
                                    dayOrder = Long.toString(dataSnapshot.getValue(Long.class));
                                    dayOrder = String.format("%s %s", getString(R.string.today_is_day), dayOrder);
                                    timeViewer.startAnimation(animationFadeIn);
                                    timeViewer.setText(dayOrder);
                                }
                            } catch (DatabaseException e) {
                                if (dayOrder.equals("Today is ")) {
                                    dayOrder = dayOrder.concat(dataSnapshot.getValue(String.class));
                                    timeViewer.startAnimation(animationFadeIn);
                                    timeViewer.setText(dayOrder);
                                    Toast.makeText(getBaseContext(), dayOrder, Toast.LENGTH_LONG).show();
                                }
                            }
                            progressBarDayOrder.setAnimation(animationFadeOut);
                            progressBarDayOrder.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });

                } else {
                    // User is signed out
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .setTheme(R.style.LoginTheme)
                            .build(), RC_SIGN_IN);
                }
            }
        };

        //Multi Touch Magic :P
        timeViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count++ % 2 == 1) {
                    timeViewer.startAnimation(animationFadeIn);
                    timeViewer.setText(timeHandler.timeUpdate());
                } else {
                    if (dayOrder.equals("Today is ")) {
                        timeViewer.startAnimation(animationFadeIn);
                        timeViewer.setText(R.string.fetching_day);
                    } else {
                        timeViewer.startAnimation(animationFadeIn);
                        timeViewer.setText(dayOrder);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!, Fetching previous data if it exists!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_details:
                toolbar.getMenu().clear();
                hideBottomBar();
                revealBack();
                actionBarTitle.setText(R.string.enter_details);
                Fragment fragment = EntryFragment.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_frag, fragment)
                        .addToBackStack("null")
                        .commit();
                break;
            case R.id.edit_slot:
                startActivity(new Intent(this, SlotSelector.class));
                break;
            case R.id.sign_out:
                clearData();
                AuthUI.getInstance().signOut(this);
                break;
            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.help_amp_support).setItems(R.array.help_support, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Toast.makeText(getBaseContext(), R.string.help0, Toast.LENGTH_LONG).show();
                        } else if (i == 1) {
                            Toast.makeText(getBaseContext(), R.string.help1,
                                    Toast.LENGTH_LONG).show();
                        } else if (i == 2) {
                            Toast.makeText(getBaseContext(), R.string.help2,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.support:
                Uri uri = Uri.parse(getString(R.string.techcheckone));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.about:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(R.string.app_name);
                String message = getString(R.string.version) + "\nDeveloped by: Andro\nGraphics by: APSR Creatix";
                builder1.setMessage(message);
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
                break;
            case R.id.report:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain")
                        .putExtra(Intent.EXTRA_EMAIL, "androdev700@gmail.com")
                        .putExtra(Intent.EXTRA_SUBJECT, "Companion : ISSUE");
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this app, to ease out your timetable. https://drive.google.com/open?id=0B3ydVLQnm1oLZkF0QnRjOFVqX1k");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share App with Friends"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        actionBarTitle.setText(R.string.home);
        showBottomBar();
        view.setVisibility(View.GONE);
        toolbar.inflateMenu(R.menu.main_menu);
    }

    // View Listeners
    public void card1(View view) {
        hideBottomBar();
        revealBack();
        toolbar.getMenu().clear();
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
        hideBottomBar();
        revealBack();
        toolbar.getMenu().clear();
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
        hideBottomBar();
        revealBack();
        toolbar.getMenu().clear();
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
        hideBottomBar();
        revealBack();
        toolbar.getMenu().clear();
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
        hideBottomBar();
        revealBack();
        toolbar.getMenu().clear();
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
        hideBottomBar();
        revealBack();
        toolbar.getMenu().clear();
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
        hideBottomBar();
        revealBack();
        toolbar.getMenu().clear();
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
        revealBack();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(MainActivity.this,
                Uri.parse("http://academia.srmuniv.ac.in"));
    }

    /*public void suchita(View view) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(MainActivity.this, Uri.parse(getString(R.string.ishucita)));
    }*/

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
                        } else if (which == 3) {
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            builder.setShowTitle(true);
                            builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(MainActivity.this,
                                    Uri.parse("http://srmnotes.weebly.com/even-semester-2016-17.html"));
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void backPressed(View view) {
        onBackPressed();
    }

    public void showTimeClass1(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour1, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass2(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour2, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass3(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour3, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass4(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour4, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass5(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour5, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass6(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour6, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass7(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour7, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass8(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour8, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass9(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour9, Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass10(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour10, Toast.LENGTH_SHORT).show();
    }

    // UI Control
    public void clearData() {
        pref0.edit().clear().apply();
        pref1.edit().clear().apply();
        pref2.edit().clear().apply();
        pref3.edit().clear().apply();
        pref4.edit().clear().apply();
        SharedPreferences slotPref = getSharedPreferences("SlotChoice", MODE_PRIVATE);
        SharedPreferences slotRoomPref = getSharedPreferences("SlotRoom", MODE_PRIVATE);
        SharedPreferences labTime = getSharedPreferences("LabTime", MODE_PRIVATE);
        slotPref.edit().clear().apply();
        slotRoomPref.edit().clear().apply();
        labTime.edit().clear().apply();
        getSharedPreferences("batch", MODE_PRIVATE).edit().clear().apply();
    }

    public static void writeData(String dayOrder, DayOrder obj) {
        DatabaseReference dayOrderRef = userRef.child(dayOrder);
        dayOrderRef.setValue(obj);
    }

    public static void writeDataSlot(Slot slot) {
        DatabaseReference slotRef = userRef.child("slot");
        slotRef.setValue(slot);
    }

    public static void writeLabSlot(LabSlot labSlot) {
        DatabaseReference labRef = userRef.child("lab_slot");
        labRef.setValue(labSlot);
    }

    public static void writeBatch(String batch) {
        DatabaseReference batchRef = userRef.child("batch");
        batchRef.setValue(batch);
    }

    public void setData(ArrayList<String> course, SharedPreferences pref) {
        int index = 0;
        SharedPreferences.Editor editor = pref.edit();
        for (int i = 0; i < 10; i++) {
            editor.putString(hourName[index], course.get(index));
            index++;
        }
        editor.apply();
    }

    public void setSlot(ArrayList<String> course, ArrayList<String> room) {
        SharedPreferences slotPref = getSharedPreferences("SlotChoice", MODE_PRIVATE);
        SharedPreferences slotRoomPref = getSharedPreferences("SlotRoom", MODE_PRIVATE);
        SharedPreferences.Editor editor = slotPref.edit();
        SharedPreferences.Editor editorRoom = slotRoomPref.edit();
        for (int i = 0; i < 7; i++) {
            editor.putString(courses[i], course.get(i));
            editorRoom.putString(courses[i], room.get(i));
        }
        editor.apply();
        editorRoom.apply();
    }

    public void setLabSlot(ArrayList<String> course, ArrayList<String> room, ArrayList<String> time) {
        SharedPreferences slotPref = getSharedPreferences("SlotChoice", MODE_PRIVATE);
        SharedPreferences slotRoomPref = getSharedPreferences("SlotRoom", MODE_PRIVATE);
        SharedPreferences labTime = getSharedPreferences("LabTime", MODE_PRIVATE);
        SharedPreferences.Editor editor = slotPref.edit();
        SharedPreferences.Editor editorRoom = slotRoomPref.edit();
        SharedPreferences.Editor editorTime = labTime.edit();
        for (int i = 0; i < 3; i++) {
            editor.putString(labCourses[i], course.get(i));
            editorRoom.putString(labCourses[i], room.get(i));
            editorTime.putString(labCourses[i], time.get(i));
        }
        editor.apply();
        editorRoom.apply();
        editorTime.apply();
    }

    @Deprecated
    public boolean checkConnection() {
        ConnectivityManager connect = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public void revealBack() {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    public void hideBottomBar() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, bottomNavigationView.getHeight());
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(400);
        bottomNavigationView.setAnimation(animation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomBar() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, bottomNavigationView.getHeight(), 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(400);
        bottomNavigationView.setAnimation(animation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}