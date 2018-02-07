package com.example.rishabhk.ambu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by rishabhk on 1/12/18.
 */

public class CustomerBill extends AppCompatActivity {
    String username;
    private String apiPath = "http://semidigit.com/fm/android/customer_bill.php";
    private CustomerBill.CustomerBillTask mAuthTask = null;
    TextView tv_bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_bill);
        tv_bill = (TextView) findViewById(R.id.tv_bill);
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        mAuthTask = new CustomerBill.CustomerBillTask(this, username);
        mAuthTask.execute((Void) null);
    }

    public class CustomerBillTask extends AsyncTask<Void, Void, String> {

        String response = "";
        HashMap<String, String> postDataParams;
        String username;
        Context context;
        private ProgressDialog pdia;

        CustomerBillTask(Context context, String username) {
            this.username = username;
            this.context = context;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(context);
            pdia.setMessage("Retrieving your bill...");
            pdia.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");
            postDataParams.put("user_id", username);

            HttpConnectionService service = new HttpConnectionService();
            response = service.sendRequest(apiPath, postDataParams);
            try {
                System.out.println("********inside try block");
                JSONObject resultJsonObject = new JSONObject(response);
                JSONArray bill_array = resultJsonObject.getJSONArray("output");
                String str = "";
                for (int i = 0; i < bill_array.length(); i++) {
                    resultJsonObject = bill_array.getJSONObject(i);
                    Iterator<?> keys = resultJsonObject.keys();
                    while(keys.hasNext() ) {
                        String key = (String)keys.next();
                        str = str + key + " : ";
                        str = str + resultJsonObject.get(key);
                        str = str + "\n";
                    }
                    str = str + "\n\n";

                }
                return str;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;


        }
        // TODO: register the new account here

        @Override
        protected void onPostExecute(final String bill) {
            mAuthTask = null;
            pdia.dismiss();
            tv_bill.setText(bill);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

    }
}
