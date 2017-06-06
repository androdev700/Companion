package com.androdev.companion.views;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androdev.companion.R;

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

public class EventsFragment extends Fragment {

    String url = "";
    Elements elements;
    ListView listView;

    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    ArrayList<String> values = new ArrayList<>();
    ArrayList<String> valuesUrl = new ArrayList<>();

    public EventsFragment() {
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

        View v = inflater.inflate(R.layout.fragment_events, container, false);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.events_coordinator);
        Snackbar.make(coordinatorLayout, "Testing Network..", 250).show();
        if (checkInternetConnection(inflater, container)) {
            listView = (ListView) v.findViewById(R.id.events_list);
            listView.setDivider(null);
            adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, R.id.list_text, values);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setShowTitle(true);
                    builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(getContext(), Uri.parse(valuesUrl.get(position)));
                }
            });
        }
        return v;
    }

    public static Fragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean checkInternetConnection(final LayoutInflater inflater, final ViewGroup container) {

        View v = inflater.inflate(R.layout.fragment_events, container, false);
        final CoordinatorLayout coordinatorLayout1 = (CoordinatorLayout) v.findViewById(R.id.events_coordinator);

        ConnectivityManager connect = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            new ParsePage().execute();
            return true;
        } else if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(getContext(), "Check Your Internet Connection.", Toast.LENGTH_LONG).show();
            Snackbar.make(coordinatorLayout1, "Check Your Internet Connection.", Snackbar.LENGTH_INDEFINITE).show();
            return false;
        }
        return false;
    }

    public class ParsePage extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getContext());
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
                elements = doc.getElementsByTag("h4");
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
            mProgressDialog.dismiss();
            Pattern p1 = Pattern.compile("href=\"([^\"]*)\"");
            Matcher m1 = p1.matcher(url);
            while (m1.find()) {
                valuesUrl.add(m1.group(1));
            }
            adapter.notifyDataSetChanged();
        }
    }
}