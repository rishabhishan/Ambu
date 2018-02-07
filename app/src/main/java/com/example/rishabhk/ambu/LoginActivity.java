package com.example.rishabhk.ambu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {
    String username, password;
    EditText et_username, et_password;
    Button bt_login;
    private LoginActivity.UserLoginTask mAuthTask = null;


    private String apiPath = "http://semidigit.com/fm/android/logincheck.php";
    private JSONArray restulJsonArray;
    private int success = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.btnLogin);
    }

    public void login(View v){

        username = et_username.getText().toString();
        password = et_password.getText().toString();
        if (validate() == false){
            return;
        }
        mAuthTask = new LoginActivity.UserLoginTask(this, username, password);
        mAuthTask.execute((Void) null);

    }

    public boolean validate() {
        boolean valid = true;
        if (username.isEmpty()) {
            et_username.setError("Empty username");
            valid = false;
        } else {
            et_username.setError(null);
        }

        if (password.isEmpty()) {
            et_password.setError("Empty password");
            valid = false;
        } else {
            et_password.setError(null);
        }

        return valid;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        String response = "";
        HashMap<String, String> postDataParams;
        String username;
        String password;
        Context context;
        private ProgressDialog pdia;

        UserLoginTask(Context context, String username, String password) {
            this.username = username;
            this.password = password;
            this.context = context;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(context);
            pdia.setMessage("Authenticating...");
            pdia.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");
            postDataParams.put("user_id", username);
            postDataParams.put("password", password);
            postDataParams.put("type_of_user", "cu");
            HttpConnectionService service = new HttpConnectionService();
            response = service.sendRequest(apiPath, postDataParams);
            try {
                JSONObject resultJsonObject = new JSONObject(response);
                /*Iterator<?> keys = resultJsonObject.keys();
                while(keys.hasNext() ) {
                    String key = (String)keys.next();
                    System.out.println("****** "+key);
                    System.out.println("****** "+resultJsonObject.get(key));
                }*/
                if ((int)(resultJsonObject.get("success")) == 1){
                    return true;
                }
                else
                    return false;

            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
            }
            return null;


        }
            // TODO: register the new account here

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            pdia.dismiss();
            if (success){
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("username", username).apply();
                Intent intent = new Intent(context, CustomerBill.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

    }
}
