package com.abdullah_rehman.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog dialog;
    EditText etEmail, etPassword;
    Button btnLogin;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Login Status");
        alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {

                } else if (TextUtils.isEmpty(password)) {

                } else {
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute(email, password);
//                    new BackgroundWorker(LoginActivity.this).execute(email,password);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null)
            dialog.cancel();
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setCancelable(false);
            dialog.setMessage("Please wait");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String urlString = "http://rehmanian.000webhostapp.com/login.php";
            try {
                URL url = new URL(urlString);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String parameters = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(parameters);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }

                bufferedReader.close();
                outputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String response) {
//            super.onPostExecute(aVoid);
            dialog.cancel();
            if (response.contains("Error")) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject object = new JSONObject(response);
                    alertDialog.setMessage("Welcome " + object.getString("name"));
                    alertDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    class BackgroundWorker extends AsyncTask<String, Void, String> {
//        Context context;
//
//        BackgroundWorker(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String login_url = "http://rehmanian.000webhostapp.com/login.php";
//
//            String email = params[0];
//            String password = params[1];
//            try {
//                URL url = new URL(login_url);
//                //URL url = new URL(login_url+"login.php");
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//
//                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
//                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
//
//                bufferedWriter.write(post_data);
//                bufferedWriter.flush();
//                bufferedWriter.close();
//                outputStream.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
//
//                String result = "";
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    result += line;
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                return result;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return "error";
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "error";
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
////            alertDialog = new AlertDialog.Builder(context).create();
////            alertDialog.setTitle("Login Status");
//            dialog = new ProgressDialog(context);
//            dialog.setMessage("Loading, please wait");
//            dialog.show();
//            dialog.setCancelable(false);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            dialog.cancel();
//            alertDialog.setMessage(result);
//            alertDialog.show();
////            if(result.contains("Success")){
////                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
////            } else {
////                Toast.makeText(LoginActivity.this, "Error in Login! Try again.", Toast.LENGTH_SHORT).show();
////            }
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }
//    }
}
