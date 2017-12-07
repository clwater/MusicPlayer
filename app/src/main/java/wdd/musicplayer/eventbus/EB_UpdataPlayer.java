package wdd.musicplayer.eventbus;

/**
 * Created by gengzhibo on 2017/12/7.
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
