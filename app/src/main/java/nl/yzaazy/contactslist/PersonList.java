package nl.yzaazy.contactslist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PersonList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mPersonListView;
    PersonAdapter mPersonAdapter;
    ArrayList<Person> mPersonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_list);
        mPersonListView = (ListView) findViewById(R.id.person_list_item);
        new JsonTask().execute("https://randomuser.me/api/?results=20");
        mPersonAdapter = new PersonAdapter(getLayoutInflater(), mPersonList);
        mPersonListView.setAdapter(mPersonAdapter);
        mPersonListView.setOnItemClickListener(this);
    }

    //
    // Click on selected item in list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("SelectedItem: ", position + "");
        Intent intent = new Intent(getApplicationContext(), PersonDetail.class);
        intent.putExtra("FIRST_NAME", mPersonList.get(position).fistName);
        intent.putExtra("LAST_NAME", mPersonList.get(position).lastName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mPersonList.get(position).profilePicture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.putExtra("PROFILE_PICTURE", byteArray);
        startActivity(intent);
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
                    p.fistName = jPerson.getJSONObject("name").getString("first");
                    p.lastName = jPerson.getJSONObject("name").getString("last");
                    LoadBitmap loadBitmap = new LoadBitmap(jPerson.getJSONObject("picture").getString("large"), p);
                    loadBitmap.execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        class LoadBitmap extends AsyncTask<String, Void, Bitmap> {
            private String mUrl;
            private Person mP;

            LoadBitmap(String url, Person p) {
                mUrl = url;
                mP = p;
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                final String mURL = mUrl;
                try {
                    URL url = new URL(mURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    return BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    // Log exception
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap myBitmap) {
                super.onPostExecute(myBitmap);
                mP.profilePicture = getRoundedCornerBitmap(myBitmap);
                mPersonList.add(mP);
                mPersonAdapter.notifyDataSetChanged();
            }
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 150;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
