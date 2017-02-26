package org.intakers.intake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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


        final EditText email = (EditText) findViewById(R.id.UsernameEditText);
        final EditText password = (EditText) findViewById(R.id.passwordEditText);
        final Button sign_in_button = (Button) findViewById(R.id.sign_in_button);
        final LoginButton fb_login_button = (LoginButton) findViewById(R.id.login_button);
        fb_login_button.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));


        // Callback registration
        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                String user_fb_email = null;
                                try {
                                    user_fb_email = object.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //PASS THIS
                                //user_fb_email

                                //test
                                System.out.println(user_fb_email);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });


        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length() == 0 || password.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),"All Fields Required",Toast.LENGTH_SHORT).show();
                }

                if (isEmailValid(email.getText().toString())== false) {
                    Toast.makeText(getApplicationContext(),"Not a Valid Email Address",Toast.LENGTH_SHORT).show();
                }



                String email_to_pass = email.getText().toString();
                String password_to_pass = password.getText().toString();

                Intent intent = new Intent();

            }
        });

    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
