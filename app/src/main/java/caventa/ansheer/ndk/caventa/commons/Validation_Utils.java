package caventa.ansheer.ndk.caventa.commons;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import caventa.ansheer.ndk.caventa.constants.General_Data;

/**
 * Created on 30-11-2017 20:13 under Caventa_Android.
 */
public class Validation_Utils {
    public static void reset_errors(EditText[] edit_texts) {
        for (EditText edit_text : edit_texts) {
            edit_text.setError(null);
        }
    }

    public static boolean non_empty_check(EditText[] editTexts) {
        for (EditText editText : editTexts) {
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean zero_check(Double[] doubles) {
        for (Double a_Double : doubles) {
            if (a_Double == 0) {
                return true;
            }
        }
        return false;
    }

    public static Pair<Boolean, EditText> zero_check(Pair[] editText_Error_Pairs) {
        for (Pair<EditText, String> editText_Error_Pair : editText_Error_Pairs) {

            if (Double.parseDouble(editText_Error_Pair.first.getText().toString()) == 0) {
                editText_Error_Pair.first.setError(editText_Error_Pair.second);
                return new Pair<>(true, editText_Error_Pair.first);
            }
        }
        return new Pair<>(false, null);
    }

    public static Pair<Boolean, EditText> empty_check(Pair[] editText_Error_Pairs) {
        for (Pair<EditText, String> editText_Error_Pair : editText_Error_Pairs) {
            if (TextUtils.isEmpty(editText_Error_Pair.first.getText().toString())) {
                editText_Error_Pair.first.setError(editText_Error_Pair.second);
                return new Pair<>(true, editText_Error_Pair.first);
            }
        }
        return new Pair<>(false, null);
    }

    public static Pair<Boolean, EditText> number_check_double(Pair[] editText_Error_Pairs) {
        for (Pair<EditText, String> editText_Error_Pair : editText_Error_Pairs) {
            try {
                Double.parseDouble(editText_Error_Pair.first.getText().toString());
            } catch (NumberFormatException ex) {
                editText_Error_Pair.first.setError(editText_Error_Pair.second);
                return new Pair<>(true, editText_Error_Pair.first);
            }
        }
        return new Pair<>(false, null);
    }

    public static Pair<Boolean, EditText> number_check_integer(Pair[] editText_Error_Pairs) {
        for (Pair<EditText, String> editText_Error_Pair : editText_Error_Pairs) {
            try {
                Integer.parseInt(editText_Error_Pair.first.getText().toString());
            } catch (NumberFormatException ex) {
                editText_Error_Pair.first.setError(editText_Error_Pair.second);
                return new Pair<>(true, editText_Error_Pair.first);
            }
        }
        return new Pair<>(false, null);
    }

    public static void handle_json_insertion_response_and_switch_with_finish(String[] network_action_response_array, AppCompatActivity current_activity, Class to_switch_activity, Context context, View view_to_focus_on_error,View view_to_toggle) {
        Log.d(General_Data.TAG, network_action_response_array[0]);
        Log.d(General_Data.TAG, network_action_response_array[1]);

        if (network_action_response_array[0].equals("1")) {
            Toast.makeText(context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
            Log.d(General_Data.TAG, network_action_response_array[1]);
        } else {

            try {
                JSONObject json = new JSONObject(network_action_response_array[1]);
                String response_code = json.getString("status");
                switch (response_code) {
                    case "0":
                        Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
                        Activity_Utils.start_activity_with_finish(current_activity,to_switch_activity);
                        break;
                    case "1":
                        Toast.makeText(context, "Error : " + json.getString("error"), Toast.LENGTH_LONG).show();
                        view_to_focus_on_error.requestFocus();
                        break;
                    default:
                        Toast.makeText(context, "Error : Check json", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(General_Data.TAG, e.getLocalizedMessage());
            }
        }
        view_to_toggle.setEnabled(true);
    }


}
