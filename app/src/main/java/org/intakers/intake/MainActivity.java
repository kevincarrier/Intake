package org.intakers.intake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent received = getIntent();
        String dataReceived = received.getStringExtra("login_key");

        Logger log = Logger.getAnonymousLogger();
        log.info(dataReceived);

    }
}
