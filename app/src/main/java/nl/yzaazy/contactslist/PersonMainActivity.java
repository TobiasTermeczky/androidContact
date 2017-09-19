package nl.yzaazy.contactslist;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nl.yzaazy.contactslist.Fragments.PersonDetailFragment;
import nl.yzaazy.contactslist.Fragments.PersonListFragment;
import nl.yzaazy.contactslist.Helpers.ShakeDetector;
import nl.yzaazy.contactslist.Model.Person;

public class PersonMainActivity extends AppCompatActivity implements PersonListFragment.OnFragmentInteractionListener {
    MediaPlayer mPlayer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this.getApplicationContext();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mPlayer = MediaPlayer.create(context, R.raw.akon_lonely);
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                Log.i("SHAKE: ", "Device has been shaken");

                if (!mPlayer.isPlaying()) {
                    mPlayer.start();
                } else {
                    mPlayer.pause();
                    mPlayer.seekTo(0);
                }
            }
        });
        setContentView(R.layout.person_main_activity);
    }

    @Override
    public void onFragmentInteraction(Person person) {
        PersonDetailFragment detailFragment = (PersonDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_details);
        if (detailFragment != null) {
            detailFragment.populateDetails(person);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        mPlayer.pause();
        mPlayer.seekTo(0);
        super.onPause();
    }
}
