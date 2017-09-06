package nl.yzaazy.contactslist;

import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PersonMainActivity extends AppCompatActivity implements PersonListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_main_activity);
    }

    @Override
    public void onFragmentInteraction(Person person) {
        PersonDetailFragment detailFragment = (PersonDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_details);
        if (detailFragment != null ) {
            detailFragment.populateDetails(person);
        }
    }
}
