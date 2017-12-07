package wdd.musicplayer.eventbus;

/**
 * 通过通知栏控制播放
 */

public class EB_UpdataPlayer {
    public static String LAST = "last";
    public static String NEXT = "next";
    public static String PALY = "paly";
    public static String FAV = "fav";
    public String tag;

    public EB_UpdataPlayer(String tag) {
        this.tag = tag;
    }
}
