package nl.yzaazy.contactslist.Helpers;

import android.os.AsyncTask;
import android.util.Log;

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

import nl.yzaazy.contactslist.Interface.PersonInterface;
import nl.yzaazy.contactslist.Model.Person;

public class PersonGetter extends AsyncTask<String, String, String> {
    private PersonInterface mListener;
    ArrayList<Person> mPersonList = new ArrayList<>();

    public PersonGetter(PersonInterface mListener) {
        this.mListener = mListener;
    }

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
                p.email = jPerson.getString("email");
                p.phone = jPerson.getString("phone");
                p.profilePicture = jPerson.getJSONObject("picture").getString("thumbnail");
                p.profilePictureHighRes = jPerson.getJSONObject("picture").getString("large");
                mPersonList.add(p);
            }
            mListener.onGetPersonCompleted(mPersonList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
