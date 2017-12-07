package wdd.musicplayer.model;


import java.io.Serializable;

/**
 * 音频信息
 */

public class Music implements Serializable {
    public String name , artist , path , filename; //音频名称,作者,路径,文件名
    public Long ALBUM_ID , id ; //手机系统数据库相关id , 应用数据库id
    public int longTime;        //持续时间
    public boolean favorite = false , isPlaying = false; //是否在收藏列表中,是否正在播放
}
