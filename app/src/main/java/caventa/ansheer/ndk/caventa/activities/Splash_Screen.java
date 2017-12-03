package caventa.ansheer.ndk.caventa.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.commons.Activity_Utils;
import caventa.ansheer.ndk.caventa.commons.Application_VCS_Utils;
import caventa.ansheer.ndk.caventa.commons.Network_Utils;
import caventa.ansheer.ndk.caventa.commons.Server_Utils;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import ndk.prism.common_utils.Update_Utils;

import static caventa.ansheer.ndk.caventa.commons.Network_Utils.display_Long_no_FAB_no_network_bottom_SnackBar;
import static ndk.prism.common_utils.Network_Utils.display_Friendly_Exception_Message;

//TODO:Full screen splash

public class Splash_Screen extends AppCompatActivity {

    Context application_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        application_context = getApplicationContext();
        attempt_Update_Check();
    }

    private Update_Check_Task Update_Task = null;

    private class Update_Check_Task extends AsyncTask<Void, Void, String[]> {

        AppCompatActivity current_activity;
        public Update_Check_Task(AppCompatActivity current_activity) {
            this.current_activity=current_activity;
        }

        @Override
        protected String[] doInBackground(Void... params) {

            return Update_Utils.get_server_version(General_Data.SERVER_IP_ADDRESS + "/android/get_version.php");
        }

        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            Update_Task = null;

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                display_Friendly_Exception_Message(application_context, network_action_response_array[1]);
                Log.d(General_Data.TAG, network_action_response_array[1]);
                finish();
            } else {

                try {
                    JSONArray json_Array = new JSONArray(network_action_response_array[1]);

                    if (Server_Utils.check_system_status(Splash_Screen.this,json_Array.getJSONObject(0).getString("system_status"))) {

                        if (Integer.parseInt(json_Array.getJSONObject(0).getString("version_code")) != Update_Utils.getVersionCode(application_context)) {
                            update_application(Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")));

                        } else {
                            if (Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")) != Update_Utils.getVersionName(application_context)) {
                                update_application(Float.parseFloat(json_Array.getJSONObject(0).getString("version_name")));
                            } else {
                                Toast.makeText(application_context, "Latest Version...", Toast.LENGTH_SHORT).show();
                                // After completing http call will close this activity and launch main activity
                                Activity_Utils.start_activity_with_finish_and_tab_index(current_activity,Dashboard_Page.class,0);
//                                Activity_Utils.start_activity_with_finish(current_activity,Dashboard_Page.class);
                            }
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }
            }
        }

    }

    private void update_application(final float version_name) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("New version is available, please update...").setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Application_VCS_Utils.download_and_install_apk("Caventa Manager",version_name,Splash_Screen.this);
                    }
                });
        AlertDialog alert = builder1.create();
        alert.setTitle("Warning!");
        alert.show();

    }

    private void attempt_Update_Check() {
        if (Update_Task != null) {
            return;
        }

        if (Network_Utils.isOnline(application_context)) {
            Update_Task = new Update_Check_Task(this);
            Update_Task.execute((Void) null);
        } else {
            View.OnClickListener retry_Failed_Network_Task = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attempt_Update_Check();
                }
            };
            display_Long_no_FAB_no_network_bottom_SnackBar(getWindow().getDecorView(),retry_Failed_Network_Task);
        }

    }


}
