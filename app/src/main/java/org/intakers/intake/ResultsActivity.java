package org.intakers.intake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.intakers.intake.API.Post;
import org.intakers.intake.API.report;

public class ResultsActivity extends AppCompatActivity {

    public final String food_url = " https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=9mm0lhA9e5I5iXMmxvVX9AVQAydZunC62oFvbjMS&nutrients=208&nutrients=203&nutrients=255&nutrients=204&nutrients=205&nutrients=291&nutrients=269&nutrients=301&nutrients=303&nutrients=304&nutrients=305&nutrients=306&nutrients=307&nutrients=309&nutrients=401&nutrients=324&nutrients=605&nutrients=606&nutrients=262&nutrients=323&ndbno=18148";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

     StringRequest request = new StringRequest(Request.Method.GET , food_url , new Response.Listener<String>() {
         @Override
         public void onResponse(String response){

             report myreport = new Gson().fromJson(response , report.class);

             Toast.makeText(ResultsActivity.this , "Test" , Toast.LENGTH_SHORT).show();

         }
     },
     new Response.ErrorListener(){
         @Override
         public void onErrorResponse(VolleyError error) {

             Toast.makeText(ResultsActivity.this , "Error" , Toast.LENGTH_SHORT).show();
         }
     });

    }

    /*
    // Trailing slash is needed
    public static final String BASE_URL = " https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=9mm0lhA9e5I5iXMmxvVX9AVQAydZunC62oFvbjMS&nutrients=208&nutrients=203&nutrients=255&nutrients=204&nutrients=205&nutrients=291&nutrients=269&nutrients=301&nutrients=303&nutrients=304&nutrients=305&nutrients=306&nutrients=307&nutrients=309&nutrients=401&nutrients=324&nutrients=605&nutrients=606&nutrients=262&nutrients=323&ndbno=18148";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
     */
    }

