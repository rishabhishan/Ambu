package com.example.rishabhk.ambu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by rishabhk on 1/12/18.
 */

public class CustomerBill extends AppCompatActivity {
    String username;
    private String apiPath = "http://semidigit.com/fm/android/customer_bill.php";
    private CustomerBill.CustomerBillTask mAuthTask = null;
    ArrayList<String> room_name, device_id, srv_id, stored_count, current_count, last_updated;
    ArrayList<RowItemForBill> rowItems;
    ListView mylistview;
    CustomAdapterBillList adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_list_result);
        room_name = new ArrayList<>();
        device_id = new ArrayList<>();
        srv_id = new ArrayList<>();
        stored_count = new ArrayList<>();
        current_count = new ArrayList<>();
        last_updated = new ArrayList<>();
        makeHttpRequest();
    }

    public void makeHttpRequest(){
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        mAuthTask = new CustomerBill.CustomerBillTask(this, username);
        mAuthTask.execute((Void) null);
    }

    public void initialize() {
        float total = 0;
        rowItems = new ArrayList<RowItemForBill>();
        for (int i = 0; i < room_name.size(); i++) {
            RowItemForBill item;
            item = new RowItemForBill(room_name.get(i), stored_count.get(i), current_count.get(i), last_updated.get(i));
            rowItems.add(item);
            total = total + Float.parseFloat(stored_count.get(i)) + Float.parseFloat(current_count.get(i));
        }
        mylistview = (ListView) findViewById(R.id.list);
        adapter = new CustomAdapterBillList(CustomerBill.this, rowItems);
        mylistview.setAdapter(adapter);
        TextView tv_total = (TextView) this.findViewById(R.id.total);
        tv_total.setText(String.valueOf(total) +" L");
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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

            String[] list = {"Kitchen", "Main Bathroom", "Bathroom 1", "Bathroom 2", "Bathroom 3"};
            Random r = new Random();


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
                    room_name.add(list[(i%list.length)]);
                    device_id.add((String)resultJsonObject.get("Device ID"));
                    srv_id.add((String)resultJsonObject.get("SRV ID"));
                    stored_count.add((String)resultJsonObject.get("Stored Count"));
                    current_count.add((String)resultJsonObject.get("Current Count"));
                    last_updated.add((String)resultJsonObject.get("Last Updated"));
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
            System.out.println("************ Showing bill");
            System.out.println("************ " + bill);
            initialize();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

    }
}
