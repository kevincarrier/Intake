package org.intakers.intake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Test to check branch
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Add this if you want to launch the manual search function
        Intent intent = new Intent(MainActivity.this , ManualAddActivity.class);
        startActivity(intent);
        */
    }
}
