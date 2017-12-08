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
        //获取当前播放的音频信息
        Music music = (Music) intent.getSerializableExtra("music");
        //更新通知栏内容
        updataNotification(music);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void updataNotification(Music music) {
        //初始化RemoteViews对象
        RemoteViews remoteViews = initviewInfo(music);

        bindBoradcast(remoteViews);

        //构建通知栏内容
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)     //通知栏的图标
                .setContentTitle("")                    //通知栏的标题
                .setContentText("")                     //通知栏的文本(以上三个没有被使用 但是仍要进行设置)
                .setContent(remoteViews) ;              //设置自定义通知栏的布局
        //通过通知栏的内容 构建通知栏
        Notification notification = builder.build();

        //前台 service
        startForeground(1 ,notification);
    }


    /**
     * @param music
     * @return
     * 构建RemoteViews对象 用于返回通知栏中的自定义布局
     */
    private RemoteViews initviewInfo(Music music) {

        //从资源文件中获取布局的xml文件
        RemoteViews  notificationRemoteViews = new RemoteViews(this.getPackageName(), R.layout.service_notification);
        //定义资源图像id
        int favId , playId;
        //音频图片Bitmap
        Bitmap iconBitmap;
        //音频的名称及信息
        String name , info;

        //初始化播放按钮及收藏按钮
        playId = R.drawable.ic_play;
        favId =  R.drawable.ic_favorite_no;


        //如果当前没有播放音频
        if (music.path == null){
            //设置音频图片为应用图标
            notificationRemoteViews.setImageViewResource(R.id.imageview_notification_image , R.mipmap.ic_launcher);
            name = "暂无歌曲信息";
            info = "";
        }else {
            //获取音频图片
            iconBitmap = MediaUtils.parseAlbum(music);

            //如果获取到了音频图片(部分音频文件可能没有设置图片信息)
            if (iconBitmap == null){
                //设置为应用图标信息
                notificationRemoteViews.setImageViewResource(R.id.imageview_notification_image , R.mipmap.ic_launcher);
            }else {
                //设置为音频图片
                notificationRemoteViews.setImageViewBitmap(R.id.imageview_notification_image, iconBitmap);
            }
            //获取音频名称及作者
            name = music.name;
            info = music.artist;
            //获取收藏按钮资源
            if (music.favorite){
                favId = R.drawable.ic_favorite_yes;
            }
            //获取播放按钮资源
            if (music.isPlaying){
                playId = R.drawable.ic_pause;
            }else {
                playId = R.drawable.ic_play;
            }
        }

        //设置收藏按钮及播放按钮图片
        notificationRemoteViews.setImageViewResource(R.id.imageview_notification_fav , favId);
        notificationRemoteViews.setImageViewResource(R.id.imageview_notification_play , playId);
        //设置音频名及信息
        notificationRemoteViews.setTextViewText(R.id.textview_notification_name , name);
        notificationRemoteViews.setTextViewText(R.id.textview_notification_info , info);
        return notificationRemoteViews;
    }

    /**
     * @param remoteViews
     * 设置通知栏中按钮点击的相应事件
     * 当点击后通过广播发送事件
     */
    private void bindBoradcast(RemoteViews remoteViews) {
        //设置一个PendingIntent同于收藏按钮被点击后的响应
        //该PendingIntent绑定了一个广播 此广播添加了MUSICPLAYER_FAV标识
        //当收藏按钮被点击后 跳转PendingIntent 该PendingIntent发送了一个带有MUSICPLAYER_FAV标识的广播
        //此广播被NotificationBtnClickReceiver接首并处理
        //下面分别为下一首/上一首/播放按钮
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}