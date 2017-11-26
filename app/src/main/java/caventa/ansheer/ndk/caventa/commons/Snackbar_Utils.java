package caventa.ansheer.ndk.caventa.commons;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Srf on 26-11-2017.
 */

public class Snackbar_Utils {
    public static void display_Long_no_action_bottom_SnackBar(View view,String message)
    {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
