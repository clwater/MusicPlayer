package wdd.musicplayer.eventbus;

/**
 * 更新本地文件-文件页面信息
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
