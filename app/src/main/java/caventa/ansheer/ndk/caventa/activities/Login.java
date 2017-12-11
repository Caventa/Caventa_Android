package caventa.ansheer.ndk.caventa.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import ndk.prism.common_utils.Network_Utils;
import ndk.prism.common_utils.Toast_Utils;
import ndk.prism.common_utils.Update_Utils;


public class Login extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private Update_Check_Task Update_Task = null;

    // UI references.
    private EditText username;
    private EditText passcode;
    private View mProgressView;
    private View mLoginFormView;
    private boolean system_maintanance_flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        attempt_Update_Check();

//        if (system_maintanance_flag) {

            // Set up the login form.
            username = findViewById(R.id.username);
            passcode = findViewById(R.id.passcode);
            passcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button sign_in_button = findViewById(R.id.sign_in_button);
            sign_in_button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
//        }
//        else {
//            finish();
//        }

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        username.setError(null);
        passcode.setError(null);

        // Store values at the time of the login attempt.
        String entered_username = username.getText().toString();
        String entered_passcode = passcode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid passcode, if the user entered one.
        if (TextUtils.isEmpty(entered_passcode)) {
            passcode.setError("Please enter passcode");
            focusView = passcode;
            cancel = true;
        }

        // Check for a valid username, if the user entered one.
        if (TextUtils.isEmpty(entered_username)) {
            username.setError("Please enter username");
            focusView = username;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {

            InputMethodManager inputManager =
                    (InputMethodManager) getApplicationContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            if (isOnline()) {
                showProgress(true);
                mAuthTask = new UserLoginTask(entered_username, entered_passcode);
                mAuthTask.execute((Void) null);
            } else {
                Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
            }

        }
    }


    private void attempt_Update_Check() {
        if (Update_Task != null) {
            return;
        }

        // Show a progress spinner, and kick off a background task to perform the update check.
        if (isOnline()) {
            showProgress(true);
            Update_Task = new Update_Check_Task();
            Update_Task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }

    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow for very easy animations. If available, use these APIs to fade-in the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /*Represents an asynchronous login/registration task used to authenticate the user.*/
    private class UserLoginTask extends AsyncTask<Void, Void, String[]> {

        private String network_action_response;
        private final String task_username;
        private final String task_passcode;

        UserLoginTask(String username, String passcode) {
            task_username = username;
            task_passcode = passcode;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String[] doInBackground(Void... params) {

            try {

                http_client = new DefaultHttpClient();
                http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_admin_login.php");
                name_pair_value = new ArrayList<>(2);
                name_pair_value.add(new BasicNameValuePair("username", task_username));

                name_pair_value.add(new BasicNameValuePair("password", task_passcode));

                http_post.setEntity(new UrlEncodedFormEntity(name_pair_value));

                ResponseHandler<String> response_handler = new BasicResponseHandler();
                network_action_response = http_client.execute(http_post, response_handler);
                return new String[]{"0", network_action_response};

            } catch (UnsupportedEncodingException e) {
                return new String[]{"1", e.getLocalizedMessage()};
            } catch (ClientProtocolException e) {
                return new String[]{"1", e.getLocalizedMessage()};
            } catch (IOException e) {
                return new String[]{"1", e.getLocalizedMessage()};
            }
        }

        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            mAuthTask = null;
            showProgress(false);

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                Toast.makeText(Login.this, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                Log.d(General_Data.TAG, network_action_response_array[1]);
            } else {

                try {
                    JSONArray json = new JSONArray(network_action_response_array[1]);
                    String count = json.getJSONObject(0).getString("count");
                    switch (count) {
                        case "1":
                            SharedPreferences settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE,
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username", task_username);
                            editor.apply();
                            Intent i = new Intent(Login.this, Dashboard_Page.class);
                            startActivity(i);
                            finish();
                            break;
                        case "0":
                            Toast.makeText(Login.this, "Login Failure!", Toast.LENGTH_LONG).show();
                            username.requestFocus();
                            break;
                        default:
                            Toast.makeText(Login.this, "Error : Check json", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(Login.this, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    private class Update_Check_Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {

            return Update_Utils.get_flavoured_server_version("master", General_Data.SERVER_IP_ADDRESS + "/android/get_version.php");
        }

        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            Update_Task = null;
            showProgress(false);

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                Network_Utils.display_Friendly_Exception_Message(Login.this, network_action_response_array[1]);
                Log.d(General_Data.TAG, network_action_response_array[1]);
            } else {

                try {
                    JSONArray json_Array = new JSONArray(network_action_response_array[1]);
                    //update_applicatiion(Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")));
                    if (Integer.parseInt(json_Array.getJSONObject(0).getString("version_code")) == 0) {
                        Toast.makeText(Login.this, "System is in Maintenance, Try Again later...", Toast.LENGTH_LONG).show();
                        system_maintanance_flag = false;
                    } else if (Integer.parseInt(json_Array.getJSONObject(0).getString("version_code")) != Update_Utils.getVersionCode(Login.this)) {
                        update_applicatiion(Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")));

                        system_maintanance_flag = false;
                    } else {
                        if (Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")) != Update_Utils.getVersionName(Login.this)) {
                            update_applicatiion(Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")));
                            system_maintanance_flag = false;
                        } else {
                            Toast.makeText(Login.this, "Latest Version...", Toast.LENGTH_LONG).show();
                            system_maintanance_flag = true;
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(Login.this, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }
            }
        }

        @Override
        protected void onCancelled() {
            Update_Task = null;
            showProgress(false);
        }
    }

    private void update_applicatiion(final float version_name) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("New version is available, please update...").setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        download_and_install_apk(version_name);
                    }
                });
        AlertDialog alert = builder1.create();
        alert.setTitle("Warning!");
        alert.show();

    }

    private void download_and_install_apk(float version_name) {

//        showProgress(true);
//        //get destination to update file and set Uri.
//        //Download directory in external storage.
//        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//        String fileName = "Snake_Master_" + version_name + ".apk";
//        destination += fileName;
//        final Uri uri = Uri.parse("file://" + destination);
//
//        //Delete update file if exists
//        File file = new File(destination);
//        if (file.exists()) {
//            if (!file.delete()) {
//                Toast_Utils.longToast(getApplicationContext(), "Deletion failure, please clear your downloads...");
//            }
//        }
//
//        //get url of app on server
//        String url = General_Data.MASTER_UPDATE_URL;
//
//        //set download manager
//        Log.d(General_Data.TAG, url);
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        request.setDescription("Downloading Update...");
//        request.setTitle("Snake Master " + version_name);
//
//        //set destination
//        request.setDestinationUri(uri);
//
//        // get download service and enqueue file
//        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        final long downloadId = manager.enqueue(request);
//
//        //set BroadcastReceiver to install app when .apk is downloaded
//        BroadcastReceiver onComplete = new BroadcastReceiver() {
//            public void onReceive(Context ctxt, Intent intent) {
//                showProgress(false);
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                install.setDataAndType(uri,
//                        manager.getMimeTypeForDownloadedFile(downloadId));
//                startActivity(install);
//
//                unregisterReceiver(this);
//                finish();
//            }
//        };
//
//        //register receiver for when .apk download is compete
//        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @SuppressWarnings("deprecation")
    DefaultHttpClient http_client;
    @SuppressWarnings("deprecation")
    HttpPost http_post;
    @SuppressWarnings("deprecation")
    ArrayList<NameValuePair> name_pair_value;

}

