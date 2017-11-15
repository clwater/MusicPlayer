package wdd.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gengzhibo on 2017/11/15.
 */

public class SharedPreferencesUtils {
    static final String FIRST = "first";

    public static void setFirst(Context context){
        getEditor(context).putBoolean(FIRST , false).commit();
    }

    public static boolean getFirst(Context context){
        return getSharedPreferences(context).getBoolean(FIRST , true);
    }


    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getApplicationContext().
                getSharedPreferences("WDD_MUSICPLAYER", Context.MODE_PRIVATE);
    }
}
