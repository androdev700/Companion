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

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by ayushsingh on 04/01/17.
 */

public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String url = "";
    private ListAdapter eventsAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<String> values = new ArrayList<>();
    private ArrayList<String> valuesUrl = new ArrayList<>();

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        mRefreshLayout = v.findViewById(R.id.events_swipe);
        RecyclerView eventsRecycler = v.findViewById(R.id.events_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), android.R.color.holo_green_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_red_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
        eventsRecycler.setHasFixedSize(true);
        eventsRecycler.setLayoutManager(layoutManager);
        eventsAdapter = new ListAdapter(values, new AdapterClickListener() {
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
        eventsRecycler.setAdapter(eventsAdapter);
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
        protected String doInBackground(String... params) {
            org.jsoup.nodes.Document doc;
            values.clear();
            try {
                doc = Jsoup.connect("http://www.srmuniv.ac.in/featured-events").get();
                Elements elements = doc.getElementsByTag("h4");
                url = elements.toString();
                url = url.replaceAll("(<h4> )[^&]*(</h4>)", "");
                url = url.replace("<h4>FEATURED EVENTS</h4>", "");
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
            Pattern p1 = Pattern.compile("href=\"([^\"]*)\"");
            Matcher m1 = p1.matcher(url);
            while (m1.find()) {
                valuesUrl.add(m1.group(1));
            }
            eventsAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    }
}