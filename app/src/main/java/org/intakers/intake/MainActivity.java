package org.intakers.intake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Test to check branch
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       ManualAddFragment theFrag = new ManualAddFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fraggy, theFrag).commit();
    }
}
