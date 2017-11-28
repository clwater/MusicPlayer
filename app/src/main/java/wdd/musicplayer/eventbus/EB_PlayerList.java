package wdd.musicplayer.eventbus;

/**
 * Created by gengzhibo on 2017/11/27.
 */

public class EB_PlayerList {
    public static String FAVORITE = "favorite";
    public static String NOWTIME = "nowtime";

    public String viewPart;
    public String time;

    public EB_PlayerList(String viewPart) {
        this.viewPart = viewPart;
    }

    public EB_PlayerList(String viewPart, String time) {
        this.viewPart = viewPart;
        this.time = time;
    }
}
