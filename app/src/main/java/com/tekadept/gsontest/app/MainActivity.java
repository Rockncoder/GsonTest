package com.tekadept.gsontest.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TestGsonTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TestGsonTask extends AsyncTask<String, Void, String> {

        /**
         * @param notUsed
         * @return
         */
        @Override
        protected String doInBackground(String... notUsed) {
            TestPojo tp = new TestPojo();
            Gson gson = new Gson();
            String result = null;

            try {
                String json = URLEncoder.encode(gson.toJson(tp), "UTF-8");
                String url = String.format("%s%s", Constants.JsonTestUrl, json);
                result = getStream(url);
            } catch (Exception ex){
                Log.v(Constants.LOG_TAG, "Error: " + ex.getMessage());
            }
            return result;
        }

        /**
         * onPostExecute converts the JSON results into a JsonValidate
         * @param result - should be the JSON data
         */
        @Override
        protected void onPostExecute(String result) {
            // convert JSON string to a POJO
            JsonValidate jv = convertFromJson(result);
            if (jv != null) {
                Log.v(Constants.LOG_TAG, "Conversion Succeed: " + result);
            } else {
                Log.v(Constants.LOG_TAG, "Conversion Failed");
            }
        }

        /**
         * converts a string of JSON data into a JsonValidate
         * @param result - the JSON data to convert
         * @return a JsonValidate object
         */
        private JsonValidate convertFromJson(String result) {
            JsonValidate jv = null;
            if (result != null && result.length() > 0) {
                try {
                    Gson gson = new Gson();
                    jv = gson.fromJson(result, JsonValidate.class);
                } catch (Exception ex) {
                    Log.v(Constants.LOG_TAG, "Error: " + ex.getMessage());
                }
            }
            return jv;
        }

        /**
         * @param url - the complete URL to the web service including query params
         * @return - the results from the service
         */
        private String getStream(String url) {
            String response = "";

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
