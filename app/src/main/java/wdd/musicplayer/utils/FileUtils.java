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

    /**
     * @param context
     * @param path
     * @param findMusicInFileBack
     * 获取某个路径下的音频文件
     */
    public static void findMusicInFile(final Context context, String path , final FindMusicInFileBack  findMusicInFileBack){
        boolean haveMusic = false;
        final List<Music> list = new ArrayList<>();
        //根据路径构建文件
        File file = new File(path);
        //如当前路径不是文件夹则返回
        if (!file.exists()){
            return ;
        }

        //获取当前文件夹下所有文件的列表
        final File[] files = file.listFiles();

        //当前文件下的音频文件数量
        int musicNumber = 0;
        for (File file1 : files){
            if (checkIsMusic(file1.getName())){
                musicNumber++;
            }
        }

        //遍历当前文件中音频的数量
        int musicIndex = 0;
        //遍历当前文件夹
        for (int i = 0; i < files.length ; i++){
//        for (final File childFile : files){
            //获取当面文件
            final  File childFile = files[i];
            //获取文件名
            String filename = childFile.getName();

            //查看当前文件是否为音乐
            if (checkIsMusic(filename)){
                //增加musicIndex数量
                musicIndex++;
                //当前为音频文件的标识符
                haveMusic = true;

                //构建final类型的文件夹中所有的音频音频文件数量及已经遍历到的音频数量
                //因为此方法中有call回调方法 在回调方法外定义的对象需要使用final
                final int finalMusicNumber = musicNumber;
                final int finalMusicIndex = musicIndex;
                //更新系统扫描指定文件
                updateFile(context, childFile.getPath(), new UpdateFileBack() {
                    /**
                     * @param path
                     * @param uri
                     * 当系统更新完后 接受回调方法
                     */
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                        //从数据中更新音频信息
                        List<Music> musicList = MediaUtils.DBInfo(context);
                        //验证当前音频是否在系统音频数据库中
                        for (int i = 0 ; i <musicList.size() ; i++){
                            Music music = musicList.get(i);
                            if (childFile.getPath().equals(music.path)){
                                list.add(music);
                            }
                        }
                        if (finalMusicIndex == finalMusicNumber){
                            //如果当前文件下所有音频文件均被访问则通过回调返回调用方
                            findMusicInFileBack.onCompleted(list);
                        }
                    }
                });
            }

        }
        if (!haveMusic) {
            //如果当前路径下没有音频文件 提供一个回调
            findMusicInFileBack.onCompleted(list);
        }
    }

    /**
     * @param name
     * @return
     * 检查当前文件是否为音频文件
     */
    public static boolean checkIsMusic(String name){
        //将name全部替换为小写
        name = name.toLowerCase();

        if ((name.endsWith(".mp3")
                ||name.endsWith(".wav")
                ||name.endsWith(".flac")
        ))
        {
            return true;
        }else {
            return false;
        }
    }

    /**
     * @param path
     * @return
     * 获取文件夹信息
     */
    public static List<FileModel> getFilderInfo(String path) {
        List<FileModel> fileModelList = new ArrayList<>();

        //构建文件
        File file = new File(path);
        //获取当前文件夹下的文件目录并对其进行排序
        File[] files = FileUtils.sort(file.listFiles());



//        String[] a = new String[]{"1" ,"2"};
//        Arrays.sort(a , String.CASE_INSENSITIVE_ORDER);

        //遍历当前文件夹下文件
        for (File childFile : files){
            //构建FileModel对象
            FileModel fileModel = new FileModel();
            //设置fileModel的路径
            fileModel.path = childFile.getPath();
            //设置fileModel的名称
            fileModel.name = childFile.getName();
            //设置fileModel的创建时间
            fileModel.createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(childFile.lastModified()));

            //如果当前为音频文件
            //设置type及size为文件大小
            if (checkIsMusic(childFile.getName())){
                fileModel.type = 1;
                fileModel.size = showLongFileSzie(childFile.length());
                //如果当前是文件夹
                //设置type及 size为此文件夹中文件的数量
            }else if (childFile.isDirectory()){
                fileModel.type = 0;
                fileModel.size = floderChild(childFile) + " 项目";
            }else {
                //如果当前为其它文件
                //设置type及size为文件大小
                fileModel.type = 2;
                fileModel.size = showLongFileSzie(childFile.length());
            }
            //将fileModel添加到fileModel列表中
            fileModelList.add(fileModel);

        }

        return fileModelList;
    }

    /**
     * @param file
     * @return
     * 返回当前文件夹中的文件数量
     */
    public static int floderChild(File file){
        File[] files = file.listFiles();
        return files.length;
    }

    /**
     * @param length
     * @return
     * 获取当前文件大小
     */
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
        //更新当前文件下的音频文件的信息到系统数据库中
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    /**
                     * @param path
                     * @param uri
                     * 当系统数据更新完了则调用此方法
                     */
                    public void onScanCompleted(String path, Uri uri) {
                        //调用回调方法了来更新视图
                        updateFileBack.onScanCompleted(path , uri);
                    }
                });
    }

    /**
     * 回调接口
     */
    interface UpdateFileBack{
        void onScanCompleted(String path, Uri uri);
    }

    /**
     * 回调接口
     */
    public interface FindMusicInFileBack{
        void onCompleted(List<Music> musicList);
    }

    /**
     * @param filelists
     * @return
     * 将列表中的文件进行排序
     */
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

/**
 * 文件通过文件名排序的辅助类
 */
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