package wdd.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wdd.musicplayer.R;
import wdd.musicplayer.eventbus.EB_UpdataNotification;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.MediaUtils;

/**
 * 前台service 通知栏常驻
 */

public class ForegroundService extends Service {

    //构建Service的Binder用户bind回调
    private ForegroundBinder foregroundBinder = new ForegroundBinder();

    public static final String MUSICPLAYER_NEXT = "musicplayer_next";
    public static final String MUSICPLAYER_LAST = "musicplayer_last";
    public static final String MUSICPLAYER_PLAY = "musicplayer_play";
    public static final String MUSICPLAYER_FAV = "musicplayer_fav";




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        EventBus.getDefault().register(this);

        //初始化通知栏信息
        initNotification(intent);

        return foregroundBinder;
    }

    private void initNotification(Intent intent) {
        //注册广播
        registerReceiver();
        Music music = (Music) intent.getSerializableExtra("music");
        updataNotification(music);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void updataNotification(Music music) {

        RemoteViews remoteViews = initviewInfo( music);

        bindBoradcast(remoteViews);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContent(remoteViews)
                .setContentText("");
        Notification notification = builder.build();

        //前台 service
        startForeground(1 ,notification);
    }



    private RemoteViews initviewInfo(Music music) {

        RemoteViews  notificationRemoteViews = new RemoteViews(this.getPackageName(), R.layout.service_notification);

        int favBitmap , playBitmap;
        Bitmap iconBitmap;
        String name , info;


        playBitmap = R.drawable.ic_play;
        favBitmap =  R.drawable.ic_favorite_no;

        if (music.path == null){
            notificationRemoteViews.setImageViewResource(R.id.imageview_notification_image , R.mipmap.ic_launcher);
            name = "暂无歌曲信息";
            info = "";
        }else {
            iconBitmap = MediaUtils.parseAlbum(music);
            if (iconBitmap == null){
                notificationRemoteViews.setImageViewResource(R.id.imageview_notification_image , R.mipmap.ic_launcher);
            }else {
                notificationRemoteViews.setImageViewBitmap(R.id.imageview_notification_image, iconBitmap);
            }
            name = music.name;
            info = music.artist;
            if (music.favorite){
                favBitmap = R.drawable.ic_favorite_yes;
            }
            if (music.isPlaying){
                playBitmap = R.drawable.ic_pause;
            }else {
                playBitmap = R.drawable.ic_play;
            }
        }

        notificationRemoteViews.setImageViewResource(R.id.imageview_notification_fav , favBitmap);
        notificationRemoteViews.setImageViewResource(R.id.imageview_notification_play , playBitmap);
        notificationRemoteViews.setTextViewText(R.id.textview_notification_name , name);
        notificationRemoteViews.setTextViewText(R.id.textview_notification_info , info);

        return notificationRemoteViews;

    }

    private void bindBoradcast(RemoteViews remoteViews) {
        PendingIntent favIntent = PendingIntent.getBroadcast(this, 1,
                new Intent(MUSICPLAYER_FAV), 0);
        remoteViews.setOnClickPendingIntent(R.id.imageview_notification_fav, favIntent);

        PendingIntent nextIntent = PendingIntent.getBroadcast(this, 2,
                new Intent(MUSICPLAYER_NEXT), 0);
        remoteViews.setOnClickPendingIntent(R.id.imageview_notification_next, nextIntent);

        PendingIntent lastIntent = PendingIntent.getBroadcast(this, 3,
                new Intent(MUSICPLAYER_LAST), 0);
        remoteViews.setOnClickPendingIntent(R.id.imageview_notification_last, lastIntent);

        PendingIntent playIntent = PendingIntent.getBroadcast(this, 4,
                new Intent(MUSICPLAYER_PLAY), 0);
        remoteViews.setOnClickPendingIntent(R.id.imageview_notification_play, playIntent);
    }


    private void registerReceiver() {
        //动态注册系统广播 用户通知栏中点击相关按钮后通过系统广播发送信息以便于程序内其它模块接受
        //构建广播拦截器及相关拦截action(添加拦截器已过滤无用广播)
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MUSICPLAYER_NEXT);
        intentFilter.addAction(MUSICPLAYER_LAST);
        intentFilter.addAction(MUSICPLAYER_PLAY);
        intentFilter.addAction(MUSICPLAYER_FAV);

        //将过滤器及本Service绑定
        NotificationBtnClickReceiver notificationBtnClickReceiver = new NotificationBtnClickReceiver();
        registerReceiver(notificationBtnClickReceiver, intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param e
     * 通过EventBus调用更新通知栏显示的内容
     */
    @Subscribe
    public void EVENTBUD_EB_UpdataNotification(EB_UpdataNotification e){
        updataNotification(e.music);
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     * service默认方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //client 可以通过Binder获取Service实例
    public class ForegroundBinder extends Binder {
        public ForegroundService getService() {
            return ForegroundService.this;
        }
    }
}