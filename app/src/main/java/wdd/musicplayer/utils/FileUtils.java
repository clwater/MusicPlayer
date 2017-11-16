package wdd.musicplayer.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wdd.musicplayer.model.Music;

/**
 * 文件工具类
 */

public class FileUtils {
    public static List<Music> findMusicInFile(String path){
        List<Music> list = new ArrayList<>();
        File file = new File(path);

        if (!file.exists()){
            return list;
        }

        File[] files = file.listFiles();



        return list;
    }
}
