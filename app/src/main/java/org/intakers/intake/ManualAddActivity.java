package org.intakers.intake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ManualAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_add);

        ManualAddFragment theFrag = new ManualAddFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fraggy, theFrag).commit();

    }
}
