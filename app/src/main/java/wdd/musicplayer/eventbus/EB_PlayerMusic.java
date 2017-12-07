package wdd.musicplayer.eventbus;

import wdd.musicplayer.model.Music;

/**
 * 跨页面接受播放音乐的请求
 */

public class EB_PlayerMusic {
    public static String LIST = "list";     //从歌单页面发起
    public static String ALLFILE = "allfile";   //从本地数据库查看页面发起(本地音乐-所有音乐)
    public String tag;
    public Music music;
    public String parent;


    /**
     * @param tag
     * @param music
     * 从所有页面中发起的请求  提供music参数,播放此音频
     */
    public EB_PlayerMusic(String tag, Music music) {
        this.tag = tag;
        this.music = music;
    }

    /**
     * @param tag
     * @param parent
     * 从播放里面页面发起 提供parent参数,用于从数据库中查询播放列表的内容
     */
    public EB_PlayerMusic(String tag, String parent) {
        this.tag = tag;
        this.parent = parent;
    }
}
