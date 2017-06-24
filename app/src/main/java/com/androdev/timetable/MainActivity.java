package com.androdev.timetable;

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

import com.androdev.timetable.dayorder.DayOrder;
import com.androdev.timetable.days.DayFragment1;
import com.androdev.timetable.days.DayFragment2;
import com.androdev.timetable.days.DayFragment3;
import com.androdev.timetable.days.DayFragment4;
import com.androdev.timetable.days.DayFragment5;
import com.androdev.timetable.handlers.DayOrderHandler;
import com.androdev.timetable.handlers.TimeHandler;
import com.androdev.timetable.views.AcademiaFragment;
import com.androdev.timetable.views.EntryFragment;
import com.androdev.timetable.views.EventsFragment;
import com.androdev.timetable.views.HomeOthers;
import com.androdev.timetable.views.HomeWhatsNew;
import com.androdev.timetable.views.HomeYourTimeTableFragment;
import com.androdev.timetable.views.NewsFragment;
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

    private SharedPreferences pref0, pref1, pref2, pref3, pref4, class0, class1, class2, class3, class4;
    private String[] hourName = new String[]{"hour1", "hour2", "hour3", "hour4", "hour5", "hour6",
            "hour7", "hour8", "hour9", "hour10"};
    private String[] className = new String[]{"class1", "class2", "class3", "class4", "class5",
            "class6", "class7", "class8", "class9", "class10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) throws IllegalThreadStateException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        pref0 = getSharedPreferences("day1", MODE_PRIVATE);
        pref1 = getSharedPreferences("day2", MODE_PRIVATE);
        pref2 = getSharedPreferences("day3", MODE_PRIVATE);
        pref3 = getSharedPreferences("day4", MODE_PRIVATE);
        pref4 = getSharedPreferences("day5", MODE_PRIVATE);
        class0 = getSharedPreferences("class1", MODE_PRIVATE);
        class1 = getSharedPreferences("class2", MODE_PRIVATE);
        class2 = getSharedPreferences("class3", MODE_PRIVATE);
        class3 = getSharedPreferences("class4", MODE_PRIVATE);
        class4 = getSharedPreferences("class5", MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        //First Run Check
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Toast.makeText(getBaseContext(), "Tap edit to enter/edit your details!",
                    Toast.LENGTH_LONG).show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

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
        String monthNumber = (String) DateFormat.format("MM", date);
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

                    DatabaseReference fetchDayOrder1 = userRef.child("day_order1");
                    fetchDayOrder1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(DayOrder.class) != null) {
                                DayOrder order = dataSnapshot.getValue(DayOrder.class);
                                ArrayList<String> course = null;
                                ArrayList<String> room = null;
                                if (order != null) {
                                    course = order.getCourse();
                                    room = order.getRoom();
                                }
                                setData(course, room, pref0, class0);
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
                                ArrayList<String> room = null;
                                if (order != null) {
                                    course = order.getCourse();
                                    room = order.getRoom();
                                }
                                setData(course, room, pref1, class1);
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
                                ArrayList<String> room = null;
                                if (order != null) {
                                    course = order.getCourse();
                                    room = order.getRoom();
                                }
                                setData(course, room, pref2, class2);
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
                                ArrayList<String> room = null;
                                if (order != null) {
                                    course = order.getCourse();
                                    room = order.getRoom();
                                }
                                setData(course, room, pref3, class3);
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
                                ArrayList<String> room = null;
                                if (order != null) {
                                    course = order.getCourse();
                                    room = order.getRoom();
                                }
                                setData(course, room, pref4, class4);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

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
                                dayOrder = dayOrder.concat(dataSnapshot.getValue(String.class));
                                timeViewer.startAnimation(animationFadeIn);
                                timeViewer.setText(dayOrder);
                                Toast.makeText(getBaseContext(), dayOrder, Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
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
                hideBottomBar();
                revealBack();
                Fragment fragment = EntryFragment.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_frag, fragment)
                        .addToBackStack("null")
                        .commit();
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
            case R.id.support:
                Uri uri = Uri.parse(getString(R.string.techcheckone));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.about:
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
    public void onBackPressed() {
        super.onBackPressed();
        actionBarTitle.setText(R.string.home);
        showBottomBar();
        view.setVisibility(View.GONE);
    }

    // View Listeners
    public void card1(View view) {
        hideBottomBar();
        revealBack();
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
        actionBarTitle.setText(R.string.academia);
        Fragment fragment = AcademiaFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_frag, fragment)
                .addToBackStack("null")
                .commit();
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
        Toast.makeText(getBaseContext(), R.string.time_hour1,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass2(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour2,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass3(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour3,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass4(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour4,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass5(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour5,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass6(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour6,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass7(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour7,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass8(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour8,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass9(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour9,Toast.LENGTH_SHORT).show();
    }

    public void showTimeClass10(View view) {
        Toast.makeText(getBaseContext(), R.string.time_hour10,Toast.LENGTH_SHORT).show();
    }

    // UI Control
    public void clearData() {
        pref0.edit().clear().apply();
        pref1.edit().clear().apply();
        pref2.edit().clear().apply();
        pref3.edit().clear().apply();
        pref4.edit().clear().apply();
        class0.edit().clear().apply();
        class1.edit().clear().apply();
        class2.edit().clear().apply();
        class3.edit().clear().apply();
        class4.edit().clear().apply();
    }

    public static void writeData(String dayOrder, DayOrder obj) {
        DatabaseReference dayOrderRef = userRef.child(dayOrder);
        dayOrderRef.setValue(obj);
    }

    public void setData(ArrayList<String> course, ArrayList<String> room, SharedPreferences pref,
                        SharedPreferences classPreferences) {
        int index = 0;
        SharedPreferences.Editor editor = pref.edit();
        SharedPreferences.Editor editorClass = classPreferences.edit();
        for (int i = 0; i < 10; i++) {
            editor.putString(hourName[index], course.get(index));
            editorClass.putString(className[index], room.get(index));
            index++;
        }
        editor.apply();
        editorClass.apply();
    }

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