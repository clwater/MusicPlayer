package wdd.musicplayer.eventbus;

/**
 * Created by gengzhibo on 2017/11/27.
 */

public class EB_UpdataList {
    public static String UPDATAPLAYERLIST = "UpdataPlayerList";
    public static String UPDATALOACLFILELIST = "UpdataLoaclFileList";
    public static String UPDATAPLAYERLISTSHOW = "UpdataPlayerListShow";

    public String list;

    public EB_UpdataList(String list) {
        this.list = list;
    }
}
