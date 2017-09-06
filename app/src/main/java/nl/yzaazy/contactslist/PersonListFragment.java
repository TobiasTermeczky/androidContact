package nl.yzaazy.contactslist;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PersonListFragment extends Fragment {

    ListView mPersonListView;
    PersonAdapter mPersonAdapter;
    ArrayList<Person> mPersonList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        Activity activity;
        if(context instanceof Activity){
            activity = (Activity) context;
            try {
                mListener = (OnFragmentInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnFragmentInteractionListener ...");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.person_list_fragment, container, false);
        mPersonListView = (ListView) view.findViewById(R.id.person_list_item);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.person_list_swipe_refresh_layout);

        if (savedInstanceState != null) {
            if (!savedInstanceState.getParcelableArrayList("mPersonList").isEmpty()) {
                mPersonList = savedInstanceState.getParcelableArrayList("mPersonList");
            } else {
                new JsonTask().execute("https://randomuser.me/api/?results=20");
            }
        } else {
            new JsonTask().execute("https://randomuser.me/api/?results=20");
        }
        mPersonAdapter = new PersonAdapter(inflater, mPersonList);
        mPersonListView.setAdapter(mPersonAdapter);
        mPersonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SelectedItem: ", position + "");
                mListener.onFragmentInteraction(mPersonList.get(position));
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPersonList.clear();
                mPersonAdapter.notifyDataSetChanged();
                new JsonTask().execute("https://randomuser.me/api/?results=20");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putParcelableArrayList("mPersonList", mPersonList);
    }

    interface OnFragmentInteractionListener {
        void onFragmentInteraction(Person person);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jObject = new JSONObject(result);
                JSONArray jResults = jObject.getJSONArray("results");
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jPerson = jResults.getJSONObject(i);
                    Person p = new Person();
                    p.firstName = WordUtils.capitalize(jPerson.getJSONObject("name").getString("first"));
                    p.lastName = WordUtils.capitalize(jPerson.getJSONObject("name").getString("last"));
                    p.profilePicture = jPerson.getJSONObject("picture").getString("large");
                    mPersonList.add(p);
                    mPersonAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
