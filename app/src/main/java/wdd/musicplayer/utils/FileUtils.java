package wdd.musicplayer.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import wdd.musicplayer.model.FileModel;
import wdd.musicplayer.model.Music;

/**
 * 文件工具类
 */

public class FileUtils {
    public static void findMusicInFile(final Context context, String path , final FindMusicInFileBack  findMusicInFileBack){
        boolean haveMusic = false;
        final List<Music> list = new ArrayList<>();
        File file = new File(path);

        if (!file.exists()){
            return ;
        }

        final File[] files = file.listFiles();

        int musicNumber = 0;
        for (File file1 : files){
            if (checkIsMusic(file1.getName())){
                musicNumber++;
            }
        }

        int musicIndex = 0;
        for (int i = 0; i < files.length ; i++){
//        for (final File childFile : files){
            final  File childFile = files[i];
            String filename = childFile.getName();

            if (checkIsMusic(filename)){
                musicIndex++;
                haveMusic = true;
            }

            if (haveMusic){
                final int finalMusicNumber = musicNumber;
                final int finalMusicIndex = musicIndex;
                updateFile(context, childFile.getPath(), new UpdateFileBack() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        List<Music> musicList = MediaUtils.DBInfo(context);
                        for (int i = 0 ; i <musicList.size() ; i++){
                            Music music = musicList.get(i);
                            if (childFile.getPath().equals(music.path)){
                                list.add(music);
                            }
                        }
                        if (finalMusicIndex >= finalMusicNumber){
                            findMusicInFileBack.onCompleted(list);
                        }
                    }
                });
            }
        }
        if (!haveMusic) {
            findMusicInFileBack.onCompleted(list);
        }
    }

    public static boolean checkIsMusic(String name){
        if ((name.endsWith(".MP3")
                ||name.endsWith(".Mp3")
                ||name.endsWith(".mp3")
                ||name.endsWith(".wav")
                ||name.endsWith(".Wav")
                ||name.endsWith(".WAV")
                ||name.endsWith(".flac")
                ||name.endsWith(".Flac")
                ||name.endsWith(".FLAC")
        )){
            return true;
        }else {
            return false;
        }
    }

    public static List<FileModel> getFilderInfo(String path) {
        List<FileModel> fileModelList = new ArrayList<>();

        File file = new File(path);
        File[] files = FileUtils.sort(file.listFiles());

        String[] a = new String[]{"1" ,"2"};
        Arrays.sort(a , String.CASE_INSENSITIVE_ORDER);

        for (File childFile : files){
            FileModel fileModel = new FileModel();
            fileModel.path = childFile.getPath();
            fileModel.name = childFile.getName();
            fileModel.createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(childFile.lastModified()));

            if (checkIsMusic(childFile.getName())){
                fileModel.type = 1;
                fileModel.size = showLongFileSzie(childFile.length());
            }else if (childFile.isDirectory()){
                fileModel.type = 0;
                fileModel.size = floderChild(childFile) + " 项目";
            }else {
                fileModel.type = 2;
                fileModel.size = showLongFileSzie(childFile.length());
            }
            fileModelList.add(fileModel);

        }

        return fileModelList;
    }

    public static int floderChild(File file){
        File[] files = file.listFiles();
        return files.length;
    }

    public static String showLongFileSzie(Long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
        }
    }

    /**
     * @param path
     * 更新系统扫描指定文件
     */
    public static void updateFile(Context context , String path , final UpdateFileBack updateFileBack)
    {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        updateFileBack.onScanCompleted(path , uri);
                    }
                });
    }

    interface UpdateFileBack{
        void onScanCompleted(String path, Uri uri);
    }
    public interface FindMusicInFileBack{
        void onCompleted(List<Music> musicList);
    }

    public static File[] sort(File[] filelists) {
        //将数组转为集合
        List<File> files = Arrays.asList(filelists);
        //利用集合工具类排序
        Collections.sort(files, new FileCompatator());
        //将文件重新转为数组
        File[] filelists1 = files.toArray(new File[files.size()]);

        return filelists1;
    }
}

class FileCompatator implements Comparator<File> {

    @Override
    public int compare(File f1, File f2) {
        // TODO Auto-generated method stub
        if (f1.isDirectory() && f2.isDirectory()) {// 都是目录

            return f1.getName().compareToIgnoreCase(f2.getName());//都是目录时按照名字排序
        } else if (f1.isDirectory() && f2.isFile()) {//目录与文件.目录在前
            return -1;
        } else if (f2.isDirectory() && f1.isFile()) {//文件与目录
            return 1;
        } else {
            return f1.getName().compareToIgnoreCase(f2.getName());//都是文件
        }

    }

}