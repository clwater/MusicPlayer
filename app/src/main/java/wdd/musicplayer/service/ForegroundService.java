package wdd.musicplayer.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import wdd.musicplayer.R;
import wdd.musicplayer.eventbus.EB_UpdataNotification;
import wdd.musicplayer.model.Music;
import wdd.musicplayer.utils.MediaUtils;

/**
 * Created by gengzhibo on 2017/12/6.
 */

public class ForegroundService extends Service {

    private ForegroundBinder foregroundBinder = new ForegroundBinder();

    public static final String MUSICPLAYER_NEXT = "musicplayer_next";
    public static final String MUSICPLAYER_LAST = "musicplayer_last";
    public static final String MUSICPLAYER_PLAY = "musicplayer_play";
    public static final String MUSICPLAYER_FAV = "musicplayer_fav";

    int index = 1;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        EventBus.getDefault().register(this);

        initNotification(intent);

        return foregroundBinder;
    }

    private void initNotification(Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MUSICPLAYER_NEXT);
        intentFilter.addAction(MUSICPLAYER_LAST);
        intentFilter.addAction(MUSICPLAYER_PLAY);
        intentFilter.addAction(MUSICPLAYER_FAV);
        NotificationBtnClickReceiver notificationBtnClickReceiver = new NotificationBtnClickReceiver();
        registerReceiver(notificationBtnClickReceiver, intentFilter);
    }



    @Subscribe
    public void EVENTBUD_EB_UpdataNotification(EB_UpdataNotification e){
        updataNotification(e.music);
    }

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