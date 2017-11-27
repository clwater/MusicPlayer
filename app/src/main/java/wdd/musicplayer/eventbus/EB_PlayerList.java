package wdd.musicplayer.eventbus;

/**
 * Created by gengzhibo on 2017/11/27.
 */

public class EB_PlayerList {
    public static String FAVORITE = "favorite";

    public String viewPart;

    public EB_PlayerList(String viewPart) {
        this.viewPart = viewPart;
    }
}
