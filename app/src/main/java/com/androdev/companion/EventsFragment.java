package com.androdev.companion;

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
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by ayushsingh on 04/01/17.
 */

public class EventsFragment extends Fragment {

    String url1 = "";
    Elements elements1;
    ListView listView1;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    static final String[] values1 = new String[]{"", "", "", "", "", "", "", ""};
    static final String[] valuesUrl = new String[]{"about:blank", "about:blank", "about:blank",
            "about:blank", "about:blank", "about:blank", "about:blank", "about:blank"};

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
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getString(R.string.events));
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id
                .events_coordinator);
        Snackbar.make(coordinatorLayout, "Testing Network.." ,250).show();
        if (checkInternetConnection(inflater, container)) {

            listView1 = (ListView) v.findViewById(R.id.events_list);
            adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, values1);
            listView1.setAdapter(adapter);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!Objects.equals(valuesUrl[position], "about:blank")) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setShowTitle(true);
                        builder.setToolbarColor(Color.parseColor("#FF03A9F4"));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getContext(), Uri.parse(valuesUrl[position]));
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
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    private boolean checkInternetConnection(final LayoutInflater inflater, final ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        final CoordinatorLayout coordinatorLayout1 = (CoordinatorLayout) v.findViewById(R.id
                .events_coordinator);

        ConnectivityManager connect = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            new ParsePage().execute();
            return true;
        } else if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
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
                valuesUrl[c1] = m1.group(1);
                c1++;
            }
            adapter.notifyDataSetChanged();
        }
    }
}