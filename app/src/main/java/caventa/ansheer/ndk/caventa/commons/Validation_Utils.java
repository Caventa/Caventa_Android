package caventa.ansheer.ndk.caventa.commons;

import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.widget.EditText;

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

    public static Pair<Boolean, EditText> empty_check(Pair[] editText_Error_Pairs) {
        for (Pair<EditText, String> editText_Error_Pair : editText_Error_Pairs) {
            if (TextUtils.isEmpty(editText_Error_Pair.first.getText().toString())) {
                editText_Error_Pair.first.setError(editText_Error_Pair.second);
                return new Pair<>(true, editText_Error_Pair.first);
            }
        }
        return new Pair<>(false, null);
    }

}
