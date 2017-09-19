package nl.yzaazy.contactslist.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nl.yzaazy.contactslist.Helpers.PersonGetter;
import nl.yzaazy.contactslist.Interface.PersonInterface;
import nl.yzaazy.contactslist.Model.Person;
import nl.yzaazy.contactslist.Adapter.PersonAdapter;
import nl.yzaazy.contactslist.R;

public class PersonListFragment extends Fragment implements PersonInterface{

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
                new PersonGetter(this).execute("https://randomuser.me/api/?results=20");
            }
        } else {
            new PersonGetter(this).execute("https://randomuser.me/api/?results=20");
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
                new PersonGetter(PersonListFragment.this).execute("https://randomuser.me/api/?results=20");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putParcelableArrayList("mPersonList", mPersonList);
    }

    @Override
    public void onGetPersonCompleted(ArrayList<Person> mPersonList) {
        this.mPersonList.addAll(mPersonList);
        mPersonAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Person person);
    }


}
