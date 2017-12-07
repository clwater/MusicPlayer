package wdd.musicplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import wdd.musicplayer.R;
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        registerReceiver();
        Music music = (Music) intent.getSerializableExtra("music");
        initNotification(music);

        return foregroundBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initNotification(Music music) {
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.service_notification);
        // 获取remoteViews（参数一：包名；参数二：布局资源）


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

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContent(remoteViews)
                .setContentText("");
        Notification notification = mBuilder.build();


        Bitmap iconBitmap , favBitmap , playBitmap;
        String name , info;


        playBitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_play);
        favBitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_favorite_no);

        if (music.path == null){
            iconBitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_launcher);
            name = "暂无歌曲信息";
            info = "";
        }else {
            iconBitmap = MediaUtils.parseAlbum(music);
            name = music.name;
            info = music.artist;
            if (music.favorite){
                favBitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_favorite_yes);
            }
            if (music.isPlaying){
                playBitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_pause);
            }
        }

        RemoteViews  notificationRemoteViews = notification.contentView;
        notificationRemoteViews.setImageViewBitmap(R.id.imageview_notification_image , iconBitmap);
        notificationRemoteViews.setImageViewBitmap(R.id.imageview_notification_fav , favBitmap);
        notificationRemoteViews.setImageViewBitmap(R.id.imageview_notification_play , playBitmap);
        notificationRemoteViews.setTextViewText(R.id.textview_notification_name , name);
        notificationRemoteViews.setTextViewText(R.id.textview_notification_info , info);


        

        //前台 service
        startForeground(1,notification);
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