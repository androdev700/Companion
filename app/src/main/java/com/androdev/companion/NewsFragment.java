package com.androdev.companion;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ayushsingh on 04/01/17.
 */

public class NewsFragment extends Fragment {

    String url="";
    Elements elements;
    ProgressDialog mProgressDialog;
    ListView listView;
    ArrayAdapter<String> adapter;
    static final String[] values = new String[]{"", "", "", "", "", "", "", ""};
    static final String[] valuesnews = new String[]{"about:blank", "about:blank", "about:blank",
            "about:blank", "about:blank", "about:blank", "about:blank", "about:blank"};

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getString(R.string.announcement));

        View v = inflater.inflate(R.layout.fragment_news,container, false);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id
                .news_coordinator);
        Snackbar.make(coordinatorLayout, "Testing Network.." ,250).show();
        if (checkInternetConnection(inflater, container)) {
            listView = (ListView) v.findViewById(R.id.news_list);
            adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!Objects.equals(valuesnews[position], "about:blank")) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setShowTitle(true);
                        builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getContext(), Uri.parse(valuesnews[position]));
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, " No Events Here!. ",
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            });
        }
        return v;
    }

    public static Fragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    private boolean checkInternetConnection(final LayoutInflater inflater, final ViewGroup container) {

        View v = inflater.inflate(R.layout.fragment_news,container, false);
        final CoordinatorLayout coordinatorLayout1 = (CoordinatorLayout) v.findViewById(R.id
                .news_coordinator);

        ConnectivityManager connect =(ConnectivityManager)getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            new ParsePage().execute();
            return true;
        } else if (
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(getContext(),"Check Your Internet Connection.",Toast.LENGTH_LONG).show();
            Snackbar.make(coordinatorLayout1,
                    " Check Your Internet Connection. ", Snackbar.LENGTH_INDEFINITE).show();
            return false;
        }
        return false;
    }

    public class ParsePage extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getContext());
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

