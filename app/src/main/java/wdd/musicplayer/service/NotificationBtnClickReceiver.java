package wdd.musicplayer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by gengzhibo on 2017/12/6.
 */

public class NotificationBtnClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ForegroundService.MUSICPLAYER_FAV)){
            Log.d("wdd" , "MUSICPLAYER_FAV");

        }else if (intent.getAction().equals(ForegroundService.MUSICPLAYER_NEXT)){
            Log.d("wdd" , "MUSICPLAYER_NEXT");


        }else if (intent.getAction().equals(ForegroundService.MUSICPLAYER_LAST)){
            Log.d("wdd" , "MUSICPLAYER_LAST");


        }else if (intent.getAction().equals(ForegroundService.MUSICPLAYER_PLAY)){
            Log.d("wdd" , "MUSICPLAYER_PLAY");


        }
    }
}
