package wdd.musicplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import wdd.musicplayer.model.Music;

/**
 * 系统数据库工具
 * 用于从数据库获取音频信息
 */

public class MediaUtils {

    public static List<Music> DBInfo(Context context){
        List<Music> musicList = new ArrayList<Music>();

        String str[] = { MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.SIZE
        };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, str, null,
                null, null);
        while (cursor.moveToNext()) {
            Music music = new Music();
            music.id = cursor.getString(0);
            music.name = cursor.getString(1);
            music.longTime = cursor.getString(2);
            music.path = cursor.getString(3);
            music.artist = cursor.getString(4);
            musicList.add(music);
        }

        return musicList;
    }

}
