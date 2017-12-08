package wdd.musicplayer.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import wdd.musicplayer.model.Music;

/**
 * 系统数据库工具
 * 用于从数据库获取音频信息
 */

public class MediaUtils {

    /**
     * @param context
     * @return
     * 从数据库中查询音频信息
     */
    public static List<Music> DBInfo(Context context){
        List<Music> musicList = new ArrayList<Music>();

        //构建需要从数据库中查询的内容
        String str[] = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DISPLAY_NAME
        };

        //构建数据的查询游标 查询数据库中所有的音频信息
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, str, null,
                null, null);

        //移动游标 直至游标中所有的数据被去除
        while (cursor.moveToNext()) {
            //构建music对象 并将相关的内容赋值为music
            Music music = new Music();
            music.id = cursor.getLong(0);
            music.name = cursor.getString(1);
            music.longTime = cursor.getInt(2);
            music.path = cursor.getString(3);
            music.artist = cursor.getString(4);
            music.ALBUM_ID = cursor.getLong(5);
            music.filename = cursor.getString(6);
            //将music对象添加到music列表中
            musicList.add(music);
        }

        return musicList;
    }


    /**
     * @param music
     * @return
     * 获取歌曲的封面图片
     */
    public static Bitmap parseAlbum(Music music) {
        File file = new File(music.path);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        try {
            metadataRetriever.setDataSource(file.getAbsolutePath());
        } catch (IllegalArgumentException e) {
        }
        byte[] albumData = metadataRetriever.getEmbeddedPicture();
        if (albumData != null) {
            return BitmapFactory.decodeByteArray(albumData, 0, albumData.length);
        }
        return null;
    }


    /**
     * 将图片裁剪为圆形
     */
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        //构建Bitmap对象 用于保存得到的图像
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //设置画布
        Canvas canvas = new Canvas(output);
        //设置颜色
        final int color = 0xff424242;
        //设置画笔 用于在画布上绘制
        final Paint paint = new Paint();
        //设置rect 一个填充绘制的范围 将其设置为当前Bitmap的大小
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        //设置抗锯齿 使得绘制的图像更平滑
        paint.setAntiAlias(true);
        //设置画笔的颜色
        canvas.drawARGB(0, 0, 0, 0);
        //设置画笔颜色
        paint.setColor(color);
        //绘制一个圆形 圆点是画布的中心 , 半径为画布宽度的一半
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        //设置过度模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //将画布中的内容绘制到Bitmap对象中
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
