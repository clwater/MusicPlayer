package wdd.musicplayer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import wdd.musicplayer.eventbus.EB_PlayerMusic;
import wdd.musicplayer.eventbus.EB_UpdataPlayer;

/**
 * Created by gengzhibo on 2017/12/6.
 */

public class NotificationBtnClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ForegroundService.MUSICPLAYER_FAV)){
            EventBus.getDefault().post(new EB_UpdataPlayer(EB_UpdataPlayer.FAV));
        }else if (intent.getAction().equals(ForegroundService.MUSICPLAYER_NEXT)){
            EventBus.getDefault().post(new EB_UpdataPlayer(EB_UpdataPlayer.NEXT));
        }else if (intent.getAction().equals(ForegroundService.MUSICPLAYER_LAST)){
            EventBus.getDefault().post(new EB_UpdataPlayer(EB_UpdataPlayer.LAST));
        }else if (intent.getAction().equals(ForegroundService.MUSICPLAYER_PLAY)){
            EventBus.getDefault().post(new EB_UpdataPlayer(EB_UpdataPlayer.PALY));
        }
    }
}
