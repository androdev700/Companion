package com.androdev.timecompanion.activities;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdev.timecompanion.R;
import com.androdev.timecompanion.dayorder.DayOrder;
import com.androdev.timecompanion.days.DayFragment1;
import com.androdev.timecompanion.days.DayFragment2;
import com.androdev.timecompanion.days.DayFragment3;
import com.androdev.timecompanion.days.DayFragment4;
import com.androdev.timecompanion.days.DayFragment5;
import com.androdev.timecompanion.handlers.DayOrderHandler;
import com.androdev.timecompanion.handlers.TimeHandler;
import com.androdev.timecompanion.labslot.LabSlot;
import com.androdev.timecompanion.slot.Slot;
import com.androdev.timecompanion.viewFragments.EntryFragment;
import com.androdev.timecompanion.viewFragments.EventsFragment;
import com.androdev.timecompanion.viewFragments.HomeOthers;
import com.androdev.timecompanion.viewFragments.HomeWhatsNew;
import com.androdev.timecompanion.viewFragments.HomeYourTimeTableFragment;
import com.androdev.timecompanion.viewFragments.NewsFragment;
import com.bumptech.glide.Glide;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private int count = 0;
    private static final String TAG = "MainActivity";
    private String dayOrder = "Today is ";
    private String dayOrderTomorrow = "Tomorrow will be ";
    private String mUsername;
    private String displayName = "You're not signed in";
    private String displayEmail = "You're not signed in";
    private String displayPhotoUrl;
    private boolean backPressed = true;

    private TextView actionBarTitle;
    private Toolbar toolbar;
    private ImageView view;
    private BottomNavigationView bottomNavigationView;
    private TextView timeViewer;
    private ProgressBar progressBarDayOrder;
    private Menu menu;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseReference;
    private static DatabaseReference userRef;

    private final Animation animationFadeIn = new AlphaAnimation(0.0f, 1.0f);
    private final Animation animationFadeOut = new AlphaAnimation(1.0f, 0.0f);

    private SharedPreferences prefDisplayName;
    private SharedPreferences pref0, pref1, pref2, pref3, pref4, class0, class1, class2, class3, class4;
    private String[] hourName = new String[]{"hour1", "hour2", "hour3", "hour4", "hour5", "hour6",
            "hour7", "hour8", "hour9", "hour10"};
    private String[] courses = new String[]{"courseA", "courseB", "courseC", "courseD", "courseE", "courseF", "courseG"};
    private String[] labCourses = new String[]{"courseLab1", "courseLab2", "courseLab3", "courseLab4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) throws IllegalThreadStateException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First Run Check
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Toast.makeText(getBaseContext(), "Thanks for downloading!", Toast.LENGTH_SHORT).show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();

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
        prefDisplayName = getSharedPreferences("account_info", MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        view = (ImageView) findViewById(R.id.toolbar_image);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        RelativeLayout dayView = (RelativeLayout) findViewById(R.id.header);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        timeViewer = (TextView) findViewById(R.id.time_text);
        progressBarDayOrder = (ProgressBar) findViewById(R.id.progressBarDayOrder);
        progressBarDayOrder.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);

        //Setting Home ActionBarText
        actionBarTitle.setText(R.string.home);
        view.setVisibility(View.GONE);

        //Customizing Animations
        animationFadeIn.setDuration(300);
        animationFadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        animationFadeOut.setDuration(300);
        animationFadeOut.setInterpolator(new AccelerateDecelerateInterpolator());

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
                            count = 1;
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

        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);
        Date tomorrow = gc.getTime();
        String dayTomorrow = (String) DateFormat.format("dd", tomorrow);
        String monthNumberTomorrow = (String) DateFormat.format("MM", tomorrow);
        final String tomorrowDate = dayTomorrow + " " + monthNumberTomorrow;

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
                    prepSync();
                    mUsername = user.getUid();
                    userRef = mDatabaseReference.child(mUsername);

                    displayName = user.getDisplayName();
                    displayEmail = user.getEmail();
                    if (user.getPhotoUrl() != null) {
                        displayPhotoUrl = user.getPhotoUrl().toString();
                        Log.d(TAG, displayPhotoUrl);
                    }
                    SharedPreferences.Editor editorName = prefDisplayName.edit();
                    editorName.putString("name", displayName);
                    editorName.putString("email", displayEmail);
                    editorName.putString("photoUrl", displayPhotoUrl);
                    editorName.apply();
                    Log.d(TAG, displayName);

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
                                String data = getSharedPreferences("batch", MODE_PRIVATE).getString("batch", "");
                                if (data.equals("1")) {
                                    initDatabaseFirst();
                                } else if (data.equals("2")) {
                                    initDatabaseSecond();
                                }
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
                                int count = 0;
                                String batch = getSharedPreferences("batch", MODE_PRIVATE).getString("batch", "");
                                for (String e : time) {
                                    if (!e.isEmpty()) {
                                        String[] labTime = e.split(" ");
                                        if (batch.equals("1")) {
                                            initLabDatabaseFirst(Integer.parseInt(labTime[0]), Integer.parseInt(labTime[1]), count++);
                                        } else if (batch.equals("2")) {
                                            initLabDatabaseSecond(Integer.parseInt(labTime[0]), Integer.parseInt(labTime[1]), count++);
                                        }
                                    }
                                }
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
                                }
                            }
                            progressBarDayOrder.setAnimation(animationFadeOut);
                            progressBarDayOrder.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });

                    DatabaseReference dayRefTomorrow = FirebaseDatabase.getInstance().getReference("CheckOrder").child(tomorrowDate);
                    dayRefTomorrow.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot.getValue(Long.class) != null) {
                                    dayOrderTomorrow = Long.toString(dataSnapshot.getValue(Long.class));
                                    dayOrderTomorrow = String.format("%s %s", getString(R.string.tomorrow_is_day), dayOrderTomorrow);
                                    Log.d(TAG, dayOrderTomorrow);
                                }
                            } catch (DatabaseException e) {
                                dayOrderTomorrow = "Tomorrow will be a holiday";
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                } else {
                    // User is signed out
                    displayName = "You're not signed in";
                    Log.d(TAG, displayName);
                    SharedPreferences.Editor editorName = prefDisplayName.edit();
                    editorName.putString("name", displayName);
                    editorName.putString("email", displayEmail);
                    editorName.apply();
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
        View.OnClickListener timerView = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long isFirstRun = getSharedPreferences("PREFERENCE_VIEW_ORDER", MODE_PRIVATE)
                        .getLong("isFirstRun", 0);
                if (isFirstRun < 3) {
                    Toast.makeText(getBaseContext(), "Hold to view Tomorrow's Day Order..", Toast.LENGTH_LONG).show();
                }
                getSharedPreferences("PREFERENCE_VIEW_ORDER", MODE_PRIVATE).edit().putLong("isFirstRun", ++isFirstRun).apply();

                if (count % 2 == 0) {
                    timeViewer.startAnimation(animationFadeIn);
                    timeViewer.setText(timeHandler.timeUpdate());
                    count = 1;
                } else {
                    count = 0;
                    if (dayOrder.equals("Today is ")) {
                        timeViewer.startAnimation(animationFadeIn);
                        timeViewer.setText(R.string.fetching_day);
                    } else {
                        timeViewer.startAnimation(animationFadeIn);
                        timeViewer.setText(dayOrder);
                    }
                }
            }
        };
        dayView.setOnClickListener(timerView);

        dayView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (dayOrderTomorrow.equals("Tomorrow is ")) {
                    Toast.makeText(MainActivity.this, "Fetching tomorrow's day order.. Try Again..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, dayOrderTomorrow, Toast.LENGTH_LONG).show();
                }
                return false;
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
    public void onBackPressed() {
        super.onBackPressed();
        actionBarTitle.setText(R.string.home);
        showBottomBar();
        view.setVisibility(View.GONE);
        if (!backPressed) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            MenuItem item = menu.findItem(R.id.time_format);
            Boolean isEnabled = getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isEnabled", true);
            item.setChecked(isEnabled);
            backPressed = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Log.d(TAG,"onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.time_format);
        Boolean isEnabled = getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true);
        item.setChecked(isEnabled);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_details:
                backPressed = false;
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
            case R.id.time_format:
                if (item.isChecked()) {
                    item.setChecked(false);
                    getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE).edit().putBoolean("isEnabled", false).apply();
                    Toast.makeText(getBaseContext(), "24 Hours Disabled", Toast.LENGTH_SHORT).show();
                } else {
                    item.setChecked(true);
                    getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE).edit().putBoolean("isEnabled", true).apply();
                    Toast.makeText(getBaseContext(), "24 Hours Enabled", Toast.LENGTH_SHORT).show();
                }
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
            case R.id.account:
                AlertDialog.Builder accountView = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                View accountDialog = layoutInflater.inflate(R.layout.dialog_account, null);
                accountView.setTitle("Signed in using");
                accountView.setView(accountDialog);
                AlertDialog dialog = accountView.create();

                ImageView accountPicture = accountDialog.findViewById(R.id.account_picture);
                TextView accountName = accountDialog.findViewById(R.id.account_name);
                TextView accountEmail = accountDialog.findViewById(R.id.account_email);

                accountName.setText(prefDisplayName.getString("name", "You're not signed in"));
                accountEmail.setText(prefDisplayName.getString("email", "You're not signed in"));

                if (!prefDisplayName.getString("photoUrl", "").equals("")) {
                    Glide.with(accountPicture.getContext()).load(prefDisplayName.getString("photoUrl", "")).into(accountPicture);
                }
                dialog.show();
                break;
            case R.id.about:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_about, null);
                builder1.setTitle(getString(R.string.app_name) + " " + getString(R.string.version));
                builder1.setView(dialogView);
                AlertDialog alertDialog = builder1.create();

                Button buttonSupport;
                ImageView androTwitter, androGPlus, androGithub, apsrTwitter, apsrGPlus, apsrGithub;
                androTwitter = dialogView.findViewById(R.id.andro_twitter);
                androGPlus = dialogView.findViewById(R.id.andro_gplus);
                androGithub = dialogView.findViewById(R.id.andro_github);

                apsrTwitter = dialogView.findViewById(R.id.apsr_twitter);
                apsrGPlus = dialogView.findViewById(R.id.apsr_gplus);
                apsrGithub = dialogView.findViewById(R.id.apsr_github);

                buttonSupport = dialogView.findViewById(R.id.support_button);

                androTwitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.techcheckone));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                androGPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://plus.google.com/118295527636701188660");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                androGithub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://github.com/androdev700");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                apsrTwitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://twitter.com/apsrsince97");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                apsrGPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://plus.google.com/107299379660184638336");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                apsrGithub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://github.com/apsrcreatix");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                buttonSupport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://www.paypal.me/androdev700");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                alertDialog.show();
                break;
            case R.id.report:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"androdev700@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "COMPANION : ISSUE");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail.."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this app, to ease out your timetable. " + getString(R.string.app_url));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share App with Friends"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // View Listeners
    public void card1(View view) {
        backPressed = false;
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
        backPressed = false;
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
        backPressed = false;
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
        backPressed = false;
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
        backPressed = false;
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
        backPressed = false;
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
        backPressed = false;
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
        //First Run Check
        Boolean isFirstRun = getSharedPreferences("PREFERENCE_ACADEMIA", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Just a reminder..")
                    .setMessage("Login in once here, and never log in again. \n(Signing into Academia on other device might sign you out of Companion.)")
                    .setNeutralButton("Gotcha!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            builder.setShowTitle(true);
                            builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(MainActivity.this,
                                    Uri.parse("http://academia.srmuniv.ac.in"));
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(MainActivity.this,
                    Uri.parse("http://academia.srmuniv.ac.in"));
        }
        getSharedPreferences("PREFERENCE_ACADEMIA", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();

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
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour1, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour1, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass2(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour2, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour2, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass3(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour3, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour3, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass4(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour4, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour4, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass5(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour5, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour5, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass6(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour6, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour6, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass7(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour7, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour7, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass8(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour8, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour8, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass9(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour9, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour9, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimeClass10(View view) {
        if (getSharedPreferences("TIME_FORMAT_PREFERENCE", MODE_PRIVATE)
                .getBoolean("isEnabled", true)) {
            Toast.makeText(getBaseContext(), R.string.time_hour10, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.twelve_time_hour10, Toast.LENGTH_SHORT).show();
        }
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
        if (course.size() == 4) {
            for (int i = 0; i < 4; i++) {
                editor.putString(labCourses[i], course.get(i));
                editorRoom.putString(labCourses[i], room.get(i));
                editorTime.putString(labCourses[i], time.get(i));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                editor.putString(labCourses[i], course.get(i));
                editorRoom.putString(labCourses[i], room.get(i));
                editorTime.putString(labCourses[i], time.get(i));
            }
        }

        editor.apply();
        editorRoom.apply();
        editorTime.apply();
    }

    private SharedPreferences.Editor venueEdit;
    private ArrayList<SharedPreferences> courseClassPrefList;
    private SharedPreferences slotRoomPref;

    public void prepSync() {
        courseClassPrefList = new ArrayList<>();
        courseClassPrefList.add(class0);
        courseClassPrefList.add(class1);
        courseClassPrefList.add(class2);
        courseClassPrefList.add(class3);
        courseClassPrefList.add(class4);
        slotRoomPref = getSharedPreferences("SlotRoom", MODE_PRIVATE);
    }

    public void initDatabaseFirst() {
        int index = 0;
        venueEdit = courseClassPrefList.get(0).edit();
        venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(1).edit();
        venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 1;
        venueEdit = courseClassPrefList.get(1).edit();
        venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 2;
        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 3;
        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(0).edit();
        venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 4;
        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 5;
        venueEdit = courseClassPrefList.get(0).edit();
        venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 6;
        venueEdit = courseClassPrefList.get(1).edit();
        venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();
    }

    public void initDatabaseSecond() {
        int index = 0;
        venueEdit = courseClassPrefList.get(0).edit();
        venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(1).edit();
        venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 1;
        venueEdit = courseClassPrefList.get(1).edit();
        venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 2;
        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 3;
        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(0).edit();
        venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 4;
        venueEdit = courseClassPrefList.get(4).edit();
        venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 5;
        venueEdit = courseClassPrefList.get(0).edit();
        venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(2).edit();
        venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        index = 6;
        venueEdit = courseClassPrefList.get(1).edit();
        venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
        venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();

        venueEdit = courseClassPrefList.get(3).edit();
        venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
        venueEdit.apply();
    }

    public void initLabDatabaseFirst(int startTime, int endTime, int indexLab) {
        if (endTime < 11) {
            venueEdit = courseClassPrefList.get(0).edit();
        } else if (endTime < 16) {
            venueEdit = courseClassPrefList.get(1).edit();
        } else if (endTime < 31) {
            venueEdit = courseClassPrefList.get(2).edit();
        } else if (endTime < 36) {
            venueEdit = courseClassPrefList.get(3).edit();
        } else if (endTime < 51) {
            venueEdit = courseClassPrefList.get(4).edit();
        }
        String s = Integer.toString(startTime % 10);

        while (startTime <= endTime) {
            Log.d("TAG", "hour".concat(s));
            venueEdit.putString("class".concat(s), slotRoomPref.getString(labCourses[indexLab], ""));
            startTime++;
            int n = Integer.parseInt(s);
            s = Integer.toString(++n);
        }
        venueEdit.apply();
    }

    public void initLabDatabaseSecond(int startTime, int endTime, int indexLab) {
        if (endTime < 6) {
            venueEdit = courseClassPrefList.get(0).edit();
        } else if (endTime < 21) {
            venueEdit = courseClassPrefList.get(1).edit();
        } else if (endTime < 26) {
            venueEdit = courseClassPrefList.get(2).edit();
        } else if (endTime < 41) {
            venueEdit = courseClassPrefList.get(3).edit();
        } else if (endTime < 46) {
            venueEdit = courseClassPrefList.get(4).edit();
        }
        String s = Integer.toString(startTime % 10);

        while (startTime <= endTime) {
            Log.d("TAG", "hour".concat(s));
            venueEdit.putString("class".concat(s), slotRoomPref.getString(labCourses[indexLab], ""));
            startTime++;
            int n = Integer.parseInt(s);
            s = Integer.toString(++n);
        }
        venueEdit.apply();
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
        animation.setDuration(300);
        bottomNavigationView.setAnimation(animation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomBar() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, bottomNavigationView.getHeight(), 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(300);
        bottomNavigationView.setAnimation(animation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}