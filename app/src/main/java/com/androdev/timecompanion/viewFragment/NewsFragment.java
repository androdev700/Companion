package com.androdev.timecompanion.viewFragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androdev.timecompanion.R;
import com.androdev.timecompanion.adapter.ListAdapter;
import com.androdev.timecompanion.handler.AdapterClickListener;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ayushsingh on 04/01/17.
 */

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String url = "";
    private ListAdapter newsAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<String> values = new ArrayList<>();
    private ArrayList<String> valuesUrl = new ArrayList<>();

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        mRefreshLayout = v.findViewById(R.id.news_swipe);
        RecyclerView newsRecycler = v.findViewById(R.id.news_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), android.R.color.holo_green_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_red_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(layoutManager);
        newsAdapter = new ListAdapter(values, new AdapterClickListener() {
            @Override
            public void onClick(View view, int position) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setShowTitle(true);
                builder.setToolbarColor(ActivityCompat.getColor(getContext(), R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(view.getContext(),
                        Uri.parse(valuesUrl.get(position)));
            }
        });
        newsRecycler.setAdapter(newsAdapter);
        onRefresh();
        return v;
    }

    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ||
                activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET);
    }

    @Override
    public void onRefresh() {
        if (checkConnection()) {
            new ParsePage().execute();
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            mRefreshLayout.setRefreshing(false);
        }
    }

    private class ParsePage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            org.jsoup.nodes.Document doc;
            try {
                doc = Jsoup.connect("http://www.srmuniv.ac.in/featured-announcements").get();
                Elements elements = doc.getElementsByTag("h4");
                url = elements.toString();
                url = url.replaceAll("(<h4> )[^&]*(</h4>)", "");
                url = url.replace("<h4>FEATURED ANNOUNCEMENTS</h4>", "");
                url = url.replaceAll("/announcement", "http://www.srmuniv.ac.in/announcement");
                url = url.replaceAll("\"/", "\"http://www.srmuniv.ac.in/");
                url = url.replaceAll("&amp;", "&");
                Pattern p = Pattern.compile(">([^\"]*)</a>");
                Matcher m = p.matcher(url);
                while (m.find()) {
                    values.add(m.group(1));
                }
            } catch (IOException e) {
                e.printStackTrace();
                url = null;
            }
            return url;
        }

        @Override
        protected void onPostExecute(String result) {
            Pattern p = Pattern.compile("href=\"([^\"]*)\"");
            Matcher m = p.matcher(url);
            while (m.find()) {
                valuesUrl.add(m.group(1));
            }
            newsAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    }
}

