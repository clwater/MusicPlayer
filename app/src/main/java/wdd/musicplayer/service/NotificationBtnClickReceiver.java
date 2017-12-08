package wdd.musicplayer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import wdd.musicplayer.eventbus.EB_UpdataPlayer;

/**
 * 广播接收器
 * 接受通知栏中点击相关按钮点击后发送的广播
 */

public class NotificationBtnClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //通过intent.getAction()获取当先广播的标识
        //根据相关标识通过EventBus发送相关请求
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
