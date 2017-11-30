package caventa.ansheer.ndk.caventa.commons;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created on 30-11-2017 20:27 under Caventa_Android.
 */
public class Alert_Dialog_Utils {
    public static void show_uncancelled_yes_no_confirmation_for_unsaved_data_and_finish_on_yes(final Context context) {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(context);
        delete_confirmation_dialog.setMessage("Unsaved data will be lost! Continue? ");
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                ((AppCompatActivity)context).finish();
            }
        });
        delete_confirmation_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = delete_confirmation_dialog.create();
        alert.setTitle("Warning!");
        alert.show();
    }
}
