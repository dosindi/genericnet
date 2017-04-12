package com.temenos.dshubhamrajput.genericnet;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/*
 * Created by dshubhamrajput on 31-03-2017.
 */


public class QrCodeGenerate extends AppCompatActivity{
    private String TAG = MainActivity.class.getSimpleName();
    ProgressDialog preProgressDialog;
    Button buttonScan;
    private EditText EditTextName,EditTextAddress,EditTextAmount;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrgenerator);
        buttonScan = (Button) findViewById(R.id.qr_Button);
        EditTextName = (EditText) findViewById(R.id.cusNameEditText);
        EditTextAddress = (EditText) findViewById(R.id.cusAddressEditText);
        EditTextAmount = (EditText) findViewById(R.id.cusAmountEditText);


        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String name = EditTextName.getText().toString();
                String address = EditTextAddress.getText().toString();
                String amount = EditTextAmount.getText().toString();
                String data = name+":"+address+":"+amount;

                Bitmap myBitmap = QRCode.from(data).bitmap();
                ImageView myImage = (ImageView) findViewById(R.id.qrCode);
                myImage.setImageBitmap(myBitmap);
            }
        });
    }

    private class qrCodeDropDownGen extends AsyncTask<Void, Void, Boolean> {
        /**
         * Establishes connection with the url and authenticates the user name
         * and password.
         */
        @Override
        protected void onPreExecute() {
            preProgressDialog = new ProgressDialog(QrCodeGenerate.this);
            preProgressDialog.setMessage("Please wait...");
            preProgressDialog.show();
            preProgressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            // Making a request to url and getting response
            String owningCustomer;
            // changes here
            HashMap<String,String> owner;
            SessionManager session =new SessionManager(getApplicationContext());
            owner=session.getUserDetails();
            owningCustomer= owner.get("cusId");

            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_cusaccno"};
            String cusAcctNos= urlObj.getURLParameter(URLAddressList,owningCustomer);

            String jsonCusAcct = sh.makeServiceCallGet(cusAcctNos);

            if (jsonCusAcct != null) {
                try {
                    JSONObject jsonObjCusAcct = new JSONObject(jsonCusAcct);
                    JSONObject firstObj = jsonObjCusAcct.getJSONObject("_embedded");
                    JSONArray item = firstObj.getJSONArray("item");
                    final Spinner spinner = (Spinner)findViewById(R.id.editText);

                    final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);

                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    for (int i = 0; i < item.length(); i++) {
                        JSONObject acctNoOfCustomer = item.getJSONObject(i);
                        final String diffAcctNo = acctNoOfCustomer.getString("AccountNo");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setAdapter(spinnerAdapter);

                                spinnerAdapter.add(diffAcctNo);
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinnerAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }
}
