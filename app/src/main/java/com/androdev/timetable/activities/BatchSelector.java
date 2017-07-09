package com.androdev.timetable.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androdev.timetable.R;

public class BatchSelector extends AppCompatActivity {

    String batch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_selector);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.batch_view);
        final Button batch1 = (Button) findViewById(R.id.radioBatch1);
        final Button batch2 = (Button) findViewById(R.id.radioBatch2);
        final Button proceedButton = (Button) findViewById(R.id.proceed_batch);
        final Button fillSlots = (Button) findViewById(R.id.fill_slots);
        final TextView batchDisplay = (TextView) findViewById(R.id.batch_display);
        final TextView warning = (TextView) findViewById(R.id.warning);
        SharedPreferences batchPref = getSharedPreferences("batch", MODE_PRIVATE);
        final SharedPreferences.Editor batchEditor = batchPref.edit();

        warning.setVisibility(View.GONE);
        final Thread timeThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!checkConnection()) {
                                layout.setVisibility(View.GONE);
                                warning.setVisibility(View.VISIBLE);
                            } else {
                                layout.setVisibility(View.VISIBLE);
                                warning.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        };
        timeThread.start();

        batch1.setEnabled(false);
        batch2.setEnabled(false);

        batch = batchPref.getString("batch","none");
        if (batch.equals("none")) {
            batch1.setEnabled(true);
            batch2.setEnabled(true);
            batch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    batch1.setEnabled(false);
                    batch2.setEnabled(true);
                    batchEditor.putString("batch", "1");
                    batchEditor.putBoolean("batchSelected",true);
                    batchEditor.apply();
                    batch = "1";
                    batchDisplay.setText("Batch 1 Selected");
                    MainActivity.writeBatch(batch);
                }
            });

            batch2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    batch2.setEnabled(false);
                    batch1.setEnabled(true);
                    batchEditor.putString("batch", "2");
                    batchEditor.putBoolean("batchSelected",true);
                    batchEditor.apply();
                    batch = "2";
                    batchDisplay.setText("Batch 2 Selected");
                    MainActivity.writeBatch(batch);
                }
            });
        } else {
            batchDisplay.setText("Batch ".concat(batch).concat(" Selected"));
        }

        fillSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedButton.setEnabled(true);
                startActivity(new Intent(BatchSelector.this, SlotSelector.class));
            }
        });
    }

    public void proceed(View view) {
        finish();
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
}
