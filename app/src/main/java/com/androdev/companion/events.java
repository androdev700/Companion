package com.androdev.companion;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class events extends AppCompatActivity {

    String url1 = "";
    Elements elements1;
    ListView listView1;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    static final String[] values1 = new String[]{"", "", "", "", "", "", "", ""};
    static final String[] valuesurl = new String[]{"about:blank", "about:blank", "about:blank",
            "about:blank", "about:blank", "about:blank", "about:blank", "about:blank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .eventscoordinate);

        checkInternetConenction();

        listView1 = (ListView) findViewById(R.id.listevents);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values1);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (valuesurl[position] != "about:blank") {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setShowTitle(true);
                    builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(events.this, Uri.parse(valuesurl[position]));
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, " No Events Here!. ", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    private boolean checkInternetConenction() {
        final CoordinatorLayout coordinatorLayout1 = (CoordinatorLayout) findViewById(R.id
                .eventscoordinate);
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            new ParsePage().execute();
            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout1,
                    " Check Your Internet Connection. ", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (checkInternetConenction()) {
                            }
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }
        return false;
    }

    public class ParsePage extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(events.this);
            mProgressDialog.setTitle("Getting Events");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            org.jsoup.nodes.Document doc;
            try {
                doc = Jsoup.connect("http://www.srmuniv.ac.in/featured-events").get();
                elements1 = doc.getElementsByTag("h4");
                url1 = elements1.toString();
                url1 = url1.replaceAll("(<h4> )[^&]*(</h4>)", "");
                url1 = url1.replace("<h4>FEATURED EVENTS</h4>", "");
                url1 = url1.replaceAll("&amp;", "&");
                Pattern p = Pattern.compile(">([^\"]*)</a>");
                Matcher m = p.matcher(url1);
                int c = 0;
                while (m.find()) {
                    values1[c] = m.group(1);
                    c++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                url1 = null;
            }
            return url1;
        }
        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            Pattern p1 = Pattern.compile("href=\"([^\"]*)\"");
            Matcher m1 = p1.matcher(url1);
            int c1 = 0;
            while (m1.find()) {
                valuesurl[c1] = m1.group(1);
                c1++;
            }
            adapter.notifyDataSetChanged();
        }
    }
}
