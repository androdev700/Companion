package com.androdev.companion;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class news extends AppCompatActivity {
    String url="";
    Elements elements;
    ProgressDialog mProgressDialog;
    ListView listView;
    ArrayAdapter<String> adapter;
    static final String[] values = new String[]{"", "", "", "", "", "", "", ""};
    static final String[] valuesnews = new String[]{"about:blank", "about:blank", "about:blank",
            "about:blank", "about:blank", "about:blank", "about:blank", "about:blank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .newscoordinate);

        checkInternetConenction();
        listView = (ListView) findViewById(R.id.listnews);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(valuesnews[position]!="about:blank") {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setShowTitle(true);
                    builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(news.this, Uri.parse(valuesnews[position]));
                }
                else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, " No Events Here!. ", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    private boolean checkInternetConenction() {

        final CoordinatorLayout coordinatorLayout1 = (CoordinatorLayout) findViewById(R.id
                .newscoordinate);

        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            new ParsePage().execute();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout1,
                    " Check Your Internet Connection. ", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (checkInternetConenction()){}
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
            mProgressDialog = new ProgressDialog(news.this);
            mProgressDialog.setTitle("Getting Announcements");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            org.jsoup.nodes.Document doc;
            try {
                doc = Jsoup.connect("http://www.srmuniv.ac.in/featured-announcements").get();
                elements = doc.getElementsByTag("h4");
                url = elements.toString();
                url=url.replaceAll("(<h4> )[^&]*(</h4>)","");
                url=url.replace("<h4>FEATURED ANNOUNCEMENTS</h4>","");
                url=url.replaceAll("/announcement","http://www.srmuniv.ac.in/announcement");
                url=url.replaceAll("\"/","\"http://www.srmuniv.ac.in/");
                url=url.replaceAll("&amp;", "&");
                Pattern p = Pattern.compile(">([^\"]*)</a>");
                Matcher m = p.matcher(url);
                int c = 0;
                while (m.find()) {
                    values[c] = m.group(1);
                    c++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                url = null;
            }
            return url;
        }
        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            Pattern p1 = Pattern.compile("href=\"([^\"]*)\"");
            Matcher m1 = p1.matcher(url);
            int c1 = 0;
            while (m1.find()) {
                valuesnews[c1] = m1.group(1);
                c1++;
            }
            adapter.notifyDataSetChanged();
        }
    }
}