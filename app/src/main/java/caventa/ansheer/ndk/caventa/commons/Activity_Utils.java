package caventa.ansheer.ndk.caventa.commons;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.github.kimkevin.cachepot.CachePot;

public class Activity_Utils {
    public static void start_activity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void start_activity_with_finish(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_object_push_and_finish(Context context, Class activity, Object object_to_push) {
        Intent intent = new Intent(context, activity);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_object_push(Context context, Class activity, Object object_to_push) {
        Intent intent = new Intent(context, activity);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
    }

    public static void start_activity_with_object_push_and_origin(Context context, Class activity, Object object_to_push, String origin) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("origin",origin);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
    }

    public static void start_activity_with_object_push_and_finish_and_origin(Context context, Class activity, Object object_to_push, String origin) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("origin",origin);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }


}
