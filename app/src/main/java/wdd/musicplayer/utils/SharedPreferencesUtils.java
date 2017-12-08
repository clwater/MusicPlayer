package wdd.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * SharedPreferences是一个本地存储的方法
 * 构建一个key-value对应的存储类型
 */

public class SharedPreferencesUtils {
    static final String FIRST = "first";

    /**
     * @param context
     * 设置用户已经打开过页面
     */
    public static void setFirst(Context context){
        getEditor(context).putBoolean(FIRST , false).commit();
    }

    /**
     * @param context
     * @return
     * 判断当前应用是否首次被打开
     */
    public static boolean getFirst(Context context){
        return getSharedPreferences(context).getBoolean(FIRST , true);
    }


    /**
     * @param context
     * @return
     * 写入到SharedPreferences
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    /**
     * @param context
     * @return
     * 初始化SharedPreferences对象
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getApplicationContext().
                getSharedPreferences("WDD_MUSICPLAYER", Context.MODE_PRIVATE);
    }
}
