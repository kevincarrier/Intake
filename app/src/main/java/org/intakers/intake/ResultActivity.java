package org.intakers.intake;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
//import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Created by chrx on 2/26/17.
 */

public class ResultActivity extends AppCompatActivity {

    Logger log = Logger.getAnonymousLogger();
    String urlString = " https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=9mm0lhA9e5I5iXMmxvVX9AVQAydZunC62oFvbjMS&nutrients=208&nutrients=203&nutrients=255&nutrients=204&nutrients=205&nutrients=291&nutrients=269&nutrients=301&nutrients=303&nutrients=304&nutrients=305&nutrients=306&nutrients=307&nutrients=309&nutrients=401&nutrients=324&nutrients=605&nutrients=606&nutrients=262&nutrients=323&ndbno=18148";

    TextView result;

    ListView nutrientsListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resulter);


        Intent recievedInfo = getIntent();
        String recievedURL = recievedInfo.getStringExtra("URL");


        //Intent newIntent = getIntent();
        //String foodName = newIntent.getStringExtra("FoodName");


        retrieveData(recievedURL);
        //run();
    }


    public void retrieveData(final String recievedURL) {
        Thread t = new Thread() {
            public void run() {
                //result = (TextView) findViewById(R.id.resultView);
                log.info("run() is called");
                try

                {
                    URL url = new URL(recievedURL);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));
                    String tmpString = "";
                    String response = "";
                    while (tmpString != null) {
                        response.concat(tmpString);
                        response = response + tmpString;
                        tmpString = reader.readLine();
                    }
                    Message msg = Message.obtain();
                    msg.obj = response;

                    Log.d("downloaded data", response);
                    responseHandler.sendMessage(msg);
                } catch (
                        Exception e
                        )

                {
                    Log.d("downloaded data", "hi");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }



    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                JSONObject blockObject = new JSONObject((String) msg.obj);
                Log.d("SUCCESS" , "SUCCESS");

                JSONObject report = blockObject.getJSONObject("report");
                JSONArray foods = report.getJSONArray("foods");
                JSONObject food_uno= foods.getJSONObject(0); //never more than one food
                JSONArray nutrients = food_uno.getJSONArray("nutrients");
                JSONObject nutrient_uno = nutrients.getJSONObject(0);

                ((TextView) findViewById(R.id.FoodTitle)).setText(food_uno.getString("name"));

                //make loop
                String[] nutrientsLists;
                nutrientsLists = new String[nutrients.length()];

                nutrientsListView = (ListView) findViewById(R.id.listviewNutrient);
                ArrayAdapter<String> nutrientsAdapter = new ArrayAdapter<String>(ResultActivity.this , android.R.layout.simple_list_item_1 , nutrientsLists);
                nutrientsListView.setAdapter(nutrientsAdapter);
                int i=0;


                Log.d("nutrients" , "nutrients" + nutrients.length());
                while(i < nutrients.length()) {
                    Log.d("Iter", "iter");
                    nutrientsLists[i] =
                        nutrients.getJSONObject(i).getString("nutrient") + "\n" +
                        nutrients.getJSONObject(i).getString("value") + " " +
                        nutrients.getJSONObject(i).getString("unit");
                        i++;
                    //JSONObject nutrient_uno = nutrients.getJSONObject(0);
                }



                //((TextView) findViewById(R.id.resultView)).setText(nutrient_uno.getString("nutrient_id"));
            } catch (JSONException e) {
                Log.d("FAILED" , "FAILED");
                e.printStackTrace();
            }
            return true;
        }
    });
}


