package caventa.ansheer.ndk.caventa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.R;
import ndk.prism.common_utils.Network_Utils;
import ndk.prism.common_utils.Toast_Utils;
import ndk.prism.common_utils.Update_Utils;

public class Splash_Screen extends Activity {

//    String now_playing, earned;
    Context application_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        application_context = getApplicationContext();
        attempt_Update_Check();

		/*
         * Showing splashscreen while making network calls to download necessary
		 * data before launching the app Will use AsyncTask to make http call
		 */
//        new PrefetchData().execute();

    }

    private Update_Check_Task Update_Task = null;

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private class Update_Check_Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {

            return Update_Utils.get_server_version("http://" + General_Data.SERVER_IP_ADDRESS + "/android/get_version.php");
        }

        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            Update_Task = null;
//			showProgress(false);

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                Network_Utils.display_Friendly_Exception_Message(application_context, network_action_response_array[1]);
                Log.d(General_Data.TAG, network_action_response_array[1]);
                finish();
            } else {

                try {
                    JSONArray json_Array = new JSONArray(network_action_response_array[1]);

                    if (check_system_status(json_Array.getJSONObject(0).getString("system_status"))) {

                        if (Integer.parseInt(json_Array.getJSONObject(0).getString("version_code")) != Update_Utils.getVersionCode(application_context)) {
                            update_applicatiion(Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")));

//                            system_maintanance_flag = false;
                        } else {
                            if (Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")) != Update_Utils.getVersionName(application_context)) {
                                update_applicatiion(Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")));
//                                system_maintanance_flag = false;
                            } else {
                                Toast.makeText(application_context, "Latest Version...", Toast.LENGTH_LONG).show();
//                                system_maintanance_flag = true;

                                // After completing http call
                                // will close this activity and lauch main activity
                                Intent i = new Intent(Splash_Screen.this, Sales_Persons.class);
//                                i.putExtra("now_playing", now_playing);
//                                i.putExtra("earned", earned);
                                startActivity(i);

                                // close this activity
                                finish();
                            }
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }
            }
        }

//        @Override
//        protected void onCancelled() {
//            Update_Task = null;
//            showProgress(false);
//        }
    }

    private boolean check_system_status(String system_status) {
        if (Integer.parseInt(system_status) == 0) {
            Toast.makeText(application_context, "System is in Maintenance, Try Again later...", Toast.LENGTH_LONG).show();
//            system_maintanance_flag = false;
            finish();
        } else if (Integer.parseInt(system_status) == 1) {
            Toast.makeText(application_context, "System Status is OK", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
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
        //get destination to update file and set Uri.
        //Download directory in external storage.
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "Caventa_Manager_" + version_name + ".apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists()) {
            if (!file.delete()) {
                Toast_Utils.longToast(getApplicationContext(), "Deletion failure, please clear your downloads...");
            }
        }

        //get url of app on server
        String url = "http://" + General_Data.UPDATE_URL;

        //set download manager
        Log.d(General_Data.TAG, url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Downloading Update...");
        request.setTitle("Caventa Manager " + version_name);

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
//                showProgress(false);
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri,
                        manager.getMimeTypeForDownloadedFile(downloadId));
                startActivity(install);

                unregisterReceiver(this);
                finish();
            }
        };

        //register receiver for when .apk download is compete
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

//    @SuppressWarnings("deprecation")
//    DefaultHttpClient http_client;
//    @SuppressWarnings("deprecation")
//    HttpPost http_post;
//    @SuppressWarnings("deprecation")
//    ArrayList<NameValuePair> name_pair_value;

    private void attempt_Update_Check() {
        if (Update_Task != null) {
            return;
        }

        // Show a progress spinner, and kick off a background task to perform the update check.
        if (isOnline()) {
//            showProgress(true);
            Update_Task = new Update_Check_Task();
            Update_Task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
//            system_maintanance_flag = false;
            finish();
        }

    }

//    /*
//     * Async Task to make http call
//     */
//    private class PrefetchData extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // before making http calls
//            Log.e("JSON", "Pre execute");
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//			/*
//			 * Will make http call here This call will download required data
//			 * before launching the app
//			 * example:
//			 * 1. Downloading and storing SQLite
//			 * 2. Downloading images
//			 * 3. Parsing the xml / json
//			 * 4. Sending device information to server
//			 * 5. etc.,
//			 */
//            JsonParser jsonParser = new JsonParser();
//            String json = jsonParser
//                    .getJSONFromUrl("http://api.androidhive.info/game/game_stats.json");
//
//            Log.e("Response: ", "> " + json);
//
//            if (json != null) {
//                try {
//                    JSONObject jObj = new JSONObject(json)
//                            .getJSONObject("game_stat");
//                    now_playing = jObj.getString("now_playing");
//                    earned = jObj.getString("earned");
//
//                    Log.e("JSON", "> " + now_playing + earned);
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // After completing http call
//            // will close this activity and lauch main activity
//            Intent i = new Intent(Splash_Screen.this, MainActivity.class);
//            i.putExtra("now_playing", now_playing);
//            i.putExtra("earned", earned);
//            startActivity(i);
//
//            // close this activity
//            finish();
//        }
//
//    }

}
