package com.example.week6;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Will contain the raw JSON response as a string.
    String data = null;

    ProgressBar progressBar;

    ImageView image1,image2,image3;
    TextView name1,name2,name3;
    TextView studid1,studid2,studid3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        new JSONRequest().execute();

    }

    private void findViews(){

        progressBar = findViewById(R.id.progressBar);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        studid1 = findViewById(R.id.studid1);
        studid2 = findViewById(R.id.studid2);
        studid3 = findViewById(R.id.studid3);

    }


    private class JSONRequest extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("https://api.myjson.com/bins/zjv68");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    data = null;
                }
                data = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                data = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            //read JSON
            try {
                JSONObject returnedData = new JSONObject(data);
                JSONArray studentArray = returnedData.getJSONArray("students");
                JSONObject studentObject1 = studentArray.getJSONObject(0);
                JSONObject studentObject2 = studentArray.getJSONObject(1);
                JSONObject studentObject3 = studentArray.getJSONObject(2);

                String studentName1 = studentObject1.getString("studentName");
                String studentName2 = studentObject2.getString("studentName");
                String studentName3 = studentObject3.getString("studentName");
                String studentID1 = studentObject1.getString("studentID");
                String studentID2 = studentObject2.getString("studentID");
                String studentID3 = studentObject3.getString("studentID");
                String studentphoto1 = studentObject1.getString("studentPhoto");
                String studentphoto2 = studentObject2.getString("studentPhoto");
                String studentphoto3 = studentObject3.getString("studentPhoto");


                name1.setText(studentName1);
                name2.setText(studentName2);
                name3.setText(studentName3);
                studid1.setText(studentID1);
                studid2.setText(studentID2);
                studid3.setText(studentID3);
                Picasso.get().load(studentphoto1).into(image1);
                Picasso.get().load(studentphoto2).into(image2);
                Picasso.get().load(studentphoto3).into(image3);

            }catch(JSONException e){
                e.printStackTrace();

            }
        }
    }
}
