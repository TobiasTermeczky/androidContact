package nl.yzaazy.contactslist.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import nl.yzaazy.contactslist.Helpers.BitmapGetter;
import nl.yzaazy.contactslist.Helpers.OnSwipeListener;
import nl.yzaazy.contactslist.Model.Person;
import nl.yzaazy.contactslist.R;

import static android.content.ContentValues.TAG;

public class PersonDetailFragment extends Fragment implements View.OnTouchListener {

    TextView firstName;
    TextView lastName;
    TextView email;
    TextView phone;
    ImageView profilePicture;
    View view;
    GestureDetector gestureDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.person_detail_fragment, container, false);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        firstName = (TextView) view.findViewById(R.id.tvFirstName);
        lastName = (TextView) view.findViewById(R.id.tvLastName);
        email = (TextView) view.findViewById(R.id.tvEmail);
        phone = (TextView) view.findViewById(R.id.tvPhone);
        gestureDetector = new GestureDetector(getContext(), new OnSwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                if (direction == Direction.down) {
                    //do your stuff
                    Log.d(TAG, "onSwipe: down");
                    view.setVisibility(View.GONE);
                    firstName.setText("");
                    lastName.setText("");
                    profilePicture.setImageBitmap(null);
                }
                return true;
            }
        });
        view.setOnTouchListener(this);
        if (savedInstanceState != null) {
            if (!"".equals(savedInstanceState.getString("firstName")) && !"".equals(savedInstanceState.getString("lastName"))) {
                firstName.setText(savedInstanceState.getString("firstName"));
                lastName.setText(savedInstanceState.getString("lastName"));
                email.setText(savedInstanceState.getString("email"));
                phone.setText(savedInstanceState.getString("phone"));
                profilePicture.setImageBitmap((Bitmap) savedInstanceState.getParcelable("profilePicture"));
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        } else {
            view.setVisibility(View.GONE);
        }
        return view;
    }


    public void populateDetails(Person person) {
        firstName.setText(person.firstName);
        lastName.setText(person.lastName);
        email.setText(person.email);
        phone.setText(person.phone);
        new BitmapGetter().download(person.profilePictureHighRes, profilePicture);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putString("firstName", firstName.getText().toString());
        saveInstanceState.putString("lastName", lastName.getText().toString());
        saveInstanceState.putString("email", email.getText().toString());
        saveInstanceState.putString("phone", phone.getText().toString());
        Bitmap profileBitmap;
        try {
            profileBitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
        } catch (NullPointerException e) {
            profileBitmap = null;
        }
        saveInstanceState.putParcelable("profilePicture", profileBitmap);
    }
}