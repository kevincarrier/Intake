package org.intakers.intake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.logging.Logger;


public class LoginActivity extends AppCompatActivity {

    Logger log = Logger.getAnonymousLogger();


    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        //logger.logPurchase(BigDecimal.valueOf(4.32), Currency.getInstance("USD"));


        final EditText username = (EditText) findViewById(R.id.UsernameEditText);
        final EditText password = (EditText) findViewById(R.id.passwordEditText);
        final Button sign_in_button = (Button) findViewById(R.id.sign_in_button);
        final LoginButton fb_login_button = (LoginButton) findViewById(R.id.login_button);
        final TextView info = (TextView) findViewById(R.id.textView);
        //fb_login_button.setReadPermissions("Email");


        // Callback registration
        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //sign_in_button.setText("You are already logged in");
                //Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                log.info("onSuccess()");
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                /*
                String[] login_info = {loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().getToken()};
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                intent.putExtra("login_key", login_info);
                startActivity(intent);
                */

            }


      @Override
            public void onCancel() {
                log.info("onCancel()");
                // App code
                info.setText("Login attempt canceled.");
            }


            @Override
            public void onError(FacebookException exception) {
                log.info("onError");
                // App code
                info.setText("Login attempt failed.");
            }
        });

        //fb login button test
        fb_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"FB button clicked",Toast.LENGTH_SHORT).show();
            }
        });


        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().length() == 0 || password.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),"All Fields Required",Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(LoginActivity.this , MainActivity.class));
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
