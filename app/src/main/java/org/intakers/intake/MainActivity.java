package org.intakers.intake;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    Logger log = Logger.getAnonymousLogger();

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView txtSpeechInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent received = getIntent();
        String dataReceived = received.getStringExtra("login_key");

        Logger log = Logger.getAnonymousLogger();
        log.info(dataReceived);

        /*
        Intent intent = new Intent(MainActivity.this , LoginActivity.class);
        startActivity(intent);
        */



        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



        //Set up the tabs on the tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_restaurant_menu_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_list_black_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //set the fragment viewpager for swiping
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        log.info("Tab count: " + tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        log.info("viewpager adapater is set");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                log.info("action_settings clicked");
                return true;

            case R.id.action_microphone:
                // User chose the "microphone" option, begin speech to text
                log.info("action_microphone clicked");
                promptSpeechInput();
                return true;

            case R.id.action_camera:
                //User chose the camera option
                log.info("action_camera clicked");
                Intent intent = new Intent(MainActivity.this, DescribeActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                log.info("default is clicked");
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.directory, menu);

        return true;
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        log.info("promptSpeechInput");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        log.info("hi");
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    log.info(result.get(0));

                    Intent sendFood = new Intent(MainActivity.this, FoodMappingActivity.class);
                    sendFood.putExtra("food", result.get(0));
                    startActivity(sendFood);

                    //txtSpeechInput.setText(result.get(0));
                    log.info(result.get(0));
                }
                break;
            }

        }
    }



    }

