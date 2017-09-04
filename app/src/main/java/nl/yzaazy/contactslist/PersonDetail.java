package nl.yzaazy.contactslist;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonDetail extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_detail);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        firstName = (TextView) findViewById(R.id.tvFirstName);
        lastName = (TextView) findViewById(R.id.tvLastName);
        Bundle intent = getIntent().getExtras();
        firstName.setText(intent.getString("FIRST_NAME"));
        lastName.setText(intent.getString("LAST_NAME"));
        byte[] byteArray = intent.getByteArray("PROFILE_PICTURE");
        profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }
}
