package com.temenos.dshubhamrajput.genericnet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by upriya on 06-03-2017.
 */

public class Addbeneficiary extends AppCompatActivity {

    public String intentData = "internal";
    public Intent commit,commit1;
    public static String benID;
    public static boolean success=true;
    ProgressDialog progressDialog;
    ProgressDialog preprogressDialog;
    String[] errorMessage;
    EditText benAccNo;
    EditText ifscEtext;
    Bundle benBundle,IntentData;
    public static String imp,intentOrigin;
    public static HashMap<String,String> ifscSpinnerVal = new HashMap<>();
    CheckBox withinbank1 ;
    CheckBox neft1 ;
     TextView ifscTextview ;
   ImageView helpIcon ;
    View focusView = null;
    public static String ifsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbeneficiary);
        getSupportActionBar().setTitle("Add beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       withinbank1 = (CheckBox) findViewById(R.id.withinbank);
         neft1 = (CheckBox) findViewById(R.id.neft);
          ifscTextview = (TextView) findViewById(R.id.textView5);
        ifscEtext = (EditText) findViewById(R.id.Ifsc);
        benAccNo = (EditText) findViewById(R.id.BenAccNo);
        final EditText accNoCheck = (EditText) findViewById(R.id.ReenterAccNo);
        final EditText emailUser = (EditText) findViewById(R.id.Email);
        final EditText nickName = (EditText) findViewById(R.id.NickName);
          helpIcon = (ImageView) findViewById(R.id.help_icon);
          
        // for the Qr
        final Bundle extras = getIntent().getExtras();
        if(extras!=null) {

            imp = extras.getString("acctAndIfsc");
            intentOrigin= extras.getString("Intentorigin");
            String[] divide = imp.split(":");
            String toAccount = divide[0];
             ifsc = divide[1];
            final String nick = divide[2];
            final String branchPass = divide[3];

//            final String firstFourLetterToIfsc = ifsc.substring(0,4);
            //setting the account and nick name before finding if the ben is from within bank
            benAccNo.setText(toAccount);
            accNoCheck.setText(toAccount);
            nickName.setText(nick);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new fromAccount().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,ifsc);
            }
            else {
                new fromAccount().execute(ifsc);
            }



        }
//hit the new deal
        new NewDeal().execute();

        withinbank1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (withinbank1.isChecked()) {
                    neft1.setChecked(false);
                    benAccNo.setText("");
                    accNoCheck.setText("");
                    emailUser.setText("");
                    nickName.setText("");
                    ifscEtext.setText("");
                    benAccNo.setError(null);
                    accNoCheck.setError(null);
                    emailUser.setError(null);
                    nickName.setError(null);
                    ifscEtext.setError(null);
                    ifscTextview.setVisibility(View.INVISIBLE);
                    ifscEtext.setVisibility(View.INVISIBLE);
                    helpIcon.setVisibility(View.INVISIBLE);
                    intentData = "internal";
                }
            }
        });
        neft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (neft1.isChecked()) {
                    withinbank1.setChecked(false);
                    benAccNo.setText("");
                    accNoCheck.setText("");
                    emailUser.setText("");
                    nickName.setText("");
                    ifscEtext.setText("");
                    benAccNo.setError(null);
                    accNoCheck.setError(null);
                    emailUser.setError(null);
                    nickName.setError(null);
                    ifscEtext.setError(null);
                    ifscTextview.setVisibility(View.VISIBLE);
                    ifscEtext.setVisibility(View.VISIBLE);
                    helpIcon.setVisibility(View.VISIBLE);
                    intentData = "external";
                }
            }
        });
        Button viewStmt = (Button) findViewById(R.id.BenButton);
        viewStmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(intentData.equals("external")) {
                    if ((ifscEtext.getText().toString()).matches(""))
                        ifscEtext.setError("This field cannot be left blank");
                }
                if ((benAccNo.getText().toString()).matches(""))
                    benAccNo.setError("This field cannot be left blank");

                if ((accNoCheck.getText().toString()).matches(""))
                    accNoCheck.setError("This field cannot be left blank");

                if ((nickName.getText().toString()).matches(""))
                    nickName.setError("This field cannot be left blank");

                if (((benAccNo.getError() == null) && (accNoCheck.getError() == null) && (emailUser.getError() == null) && (nickName.getError() == null) && (ifscEtext.getError() == null))) {
                    String accNo = benAccNo.getText().toString();
                    String emailStr = emailUser.getText().toString();
                    String nick = nickName.getText().toString();

                    if (intentData.equals("external")) {
                        String IFSCstr = ifscEtext.getText().toString();
                        new PostDetails().execute(intentData,accNo, emailStr, nick,IFSCstr);
                    } else {
                        new PostDetails().execute(intentData,accNo,emailStr,nick);
                    }
                }
            }
        });

        accNoCheck.addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (!(accNoCheck.getText().toString().equals(benAccNo.getText().toString()))) {

                    if (!((accNoCheck.getText().toString()).matches("")))
                        accNoCheck.setError("Account numbers don't match");
                }
                else if(((accNoCheck.getText().toString()).equals("")))
                    if(!(benAccNo.getText().toString()).equals(""))
                        accNoCheck.setError("This field cannot be left blank");
                    else
                        accNoCheck.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        emailUser.addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                final String email = emailUser.getText().toString();
                if (!(emailValidator(email))) {
                    if(!(email.equals("")))
                        emailUser.setError("Enter a valid email id");
                } else  {
                    emailUser.setError(null);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ifscEtext.addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                String ifsc = ifscEtext.getText().toString();
                boolean check = ifscMatcher(ifsc);
                if (!check) {
                    if(!(ifsc.equals("")))
                        ifscEtext.setError("IFSC is a 11 digit alpha numeric string");
                }
                else if (ifscEtext.getText().toString().matches("")) {
                    ifscEtext.setError("This field cannot be left blank");
                }
                else
                    ifscEtext.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        benAccNo.addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (!(accNoCheck.getText().toString().equals(benAccNo.getText().toString()))) {
                    if (!((accNoCheck.getText().toString()).equals("")))
                        accNoCheck.setError("Account numbers don't match");
                }
                else if(((accNoCheck.getText().toString()).equals("")))
                    accNoCheck.setError("This field cannot be left blank");
                else
                    accNoCheck.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean ifscMatcher(String ifsc) {
        Pattern pattern;
        Matcher matcher;
        final String ifscChecker = "^[A-Z]{4}\\d{7}$" ;
        pattern = Pattern.compile(ifscChecker);
        matcher = pattern.matcher(ifsc);
        return matcher.matches();
    }

    public void showHelpText(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.ifsc_help)
                .setTitle(R.string.ifschelppopup)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing is done
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void showErrorText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.Error_text)
                .setTitle("ERROR")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing is done
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



    private class PostDetails extends AsyncTask<String, Void, Void> {

        public String localStatus;
        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(Addbeneficiary.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... param) {
            String urlStr = "",Ifsc = "",response,benCustomerNo = "",benName = "",name="",IfscBranch="";
            JSONObject postData = new JSONObject();
            JSONArray array = new JSONArray();
            JSONArray array1 = new JSONArray();
            JSONObject jsonObjarray = new JSONObject();
            JSONObject jsonObjarray1 = new JSONObject();
             benBundle = new Bundle();
            HttpHandler sh1 = new HttpHandler();
            URLRelated urlObj = new URLRelated(getApplicationContext());
            String benAcctNo = param[1];
            String email = param[2];
            String nickName = param[3];
            localStatus= param[0];
            String owningCustomer ="";


            // COMMON FOR BOTH
            try {
                //CREATING JSON OBJECTING
                postData.put("BenAcctNo", benAcctNo);
                postData.put("BenCustomer", benCustomerNo);
                postData.put("BeneficiaryId", benID);
                postData.put("Email", email);
                jsonObjarray1.put("Name1",name);
                array1.put(jsonObjarray1);
                postData.put("Name1MvGroup", array1);
                jsonObjarray.put("Nickname", nickName);
                array.put(jsonObjarray);
                postData.put("NicknameMvGroup", array);
                // reading the session varaiable
                HashMap<String,String> owner;
                SessionManager session =new SessionManager(getApplicationContext());
                owner=session.getUserDetails();
                owningCustomer= owner.get("cusId");
                //
                postData.put("OwningCustomer",owningCustomer);
                if (localStatus.equals("external")) {
                    Ifsc=param[4];
                    postData.put("BankSortCode", Ifsc);
                    benBundle.putString("Ifsc", Ifsc);

                    String trialURL;
                    String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_beneficiary_Obnk_validate"};
                    trialURL = urlObj.getURL(URLAddressList);
                    urlStr= urlObj.getValidateURL(trialURL,benID);

                } else {

                    String trialURL;
                    String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_beneficiary_Wbnk_validate"};
                    trialURL = urlObj.getURL(URLAddressList);
                    urlStr= urlObj.getValidateURL(trialURL,benID);

                }//----------------------
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(success);
            success=sh1.jsonWrite(urlStr,postData );
            if(success) {
                response=sh1.getResponse();
                if (response != null) {
                    if(localStatus.equals("external")) {
                        try {
                            System.out.println(response);
                            JSONObject cus1 = new JSONObject(response);
                            IfscBranch= cus1.getString("BcSortCode");
                            benBundle.putString("IfscBranch", IfscBranch);
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            System.out.println(response);
                            JSONObject cus1 = new JSONObject(response);
                            benCustomerNo = cus1.getString("BenCustomer");
                            JSONArray cusArray1 = cus1.getJSONArray("Name1MvGroup");
                            for (int k = 0; k < cusArray1.length(); k++) {
                                JSONObject cus3 = cusArray1.getJSONObject(k);
                                benName = cus3.getString("Name1");
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            //common bundle
            benBundle.putString("BenAcctNo", benAcctNo);
            benBundle.putString("BenCustomer",  benCustomerNo);
            benBundle.putString("BeneficiaryId",benID);
            benBundle.putString("Email", email);
            benBundle.putString("Nickname", nickName);
            benBundle.putString("Benname", benName);
            benBundle.putString("OwningCustomer", owningCustomer);
            benBundle.putString("getintent", intentData);
            commit = new Intent(Addbeneficiary.this, ConfirmPage.class);
            commit.putExtras(benBundle);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            HttpHandler errorObj;

            String text, info;

            HashMap<String, HashMap<String, String>> errorList;
            HashMap<String, String> error;
            if (success) {
                progressDialog.dismiss();
                startActivity(commit);
            }else {
                progressDialog.dismiss();
                errorObj = new HttpHandler();
                errorList = errorObj.getErrorList();
                errorMessage= new String[errorList.size()];
                for (int i = 0; i < errorList.size(); i++) {
                    error = errorList.get("Error" + i);
                    text = error.get("text");
                    info = error.get("info");//field
                    errorMessage[i]=info;
                }
                for(int i=0;i<errorList.size();i++)
                {
                    if(intentData.equals("internal")) {
                        if (errorMessage[i].contains("BenCustomer"))
                            benAccNo.setError("Check Account Number Entered");
                    }

                    else if(errorMessage[i].contains("SortCode"))
                        ifscEtext.setError("Check IFSC Code Entered");

                }

            }

        }
    }


    private class NewDeal extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(Addbeneficiary.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url;
            URLRelated urlObj = new URLRelated(getApplicationContext());
            String[] URLAddressList= {"url_ip","url_iris_project","url_company","url_beneficiary_Wbnk_new"};
            url = urlObj.getURL(URLAddressList);
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    benID = jsonObj.getString("BeneficiaryId");
                } catch (final JSONException e) {
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            super.onPostExecute(result);
        }
    }
//    private class QRBen extends  AsyncTask<String, Void, Void>
private class fromAccount extends AsyncTask<String, Void, Boolean> {
    /**
     * Establishes connection with the url and authenticates the user name
     * and password.
     */
    @Override
    protected void onPreExecute() {
        preprogressDialog= new ProgressDialog(Addbeneficiary.this);
        preprogressDialog.setMessage("Please wait...");
        preprogressDialog.show();
        preprogressDialog.setCancelable(false);
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpHandler sh = new HttpHandler();
        URLRelated urlObj = new URLRelated(getApplicationContext());
        // Making a request to url and getting response
        String owningCustomer;
        boolean flag =true;
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

                for (int i = 0; i < item.length(); i++) {
                    JSONObject acctNoOfCustomer = item.getJSONObject(i);
                    final String diffAcctNo = acctNoOfCustomer.getString("AccountNo");
                    final String ifscCode = acctNoOfCustomer.getString("Ifsc");
                    if(!ifscCode.equals(""))
                        ifscSpinnerVal.put("ifscCodeString",ifscCode);

                }
                 String firstFourLetterToIfsc = params[0].substring(0,4);
                 String firstFourLetterFromIfsc = ifscSpinnerVal.get("ifscCodeString").substring(0,4);
                if(firstFourLetterFromIfsc.equals(firstFourLetterToIfsc))
                {
                    flag=true;
                }
                else
                {
                     flag=false;
                }


            } catch (final JSONException e) {


            }
        } else {

        }
        return flag;
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        preprogressDialog.dismiss();
        super.onPostExecute(aBoolean);
        if(aBoolean) {
            neft1.setChecked(false);
            neft1.setEnabled(false);
            withinbank1.setChecked(true);
            withinbank1.setEnabled(false);
            ifscTextview.setVisibility(View.INVISIBLE);
            ifscEtext.setVisibility(View.INVISIBLE);
            helpIcon.setVisibility(View.INVISIBLE);
            intentData = "internal";
        }
        else
        {
            withinbank1.setChecked(false);
            withinbank1.setEnabled(false);
            neft1.setChecked(true);
            neft1.setEnabled(false);
            ifscTextview.setVisibility(View.VISIBLE);
            ifscEtext.setVisibility(View.VISIBLE);
            helpIcon.setVisibility(View.VISIBLE);
            ifscEtext.setText(ifsc);
            intentData = "external";
        }

    }
}
    public void CallScanner(View v)
    {
        commit1 = new Intent(Addbeneficiary.this, QrCodeScan.class);
        IntentData = new Bundle();
        IntentData.putString("ScanCode","Ben");
        commit1.putExtras(IntentData);
        startActivity(commit1);
    }
}


