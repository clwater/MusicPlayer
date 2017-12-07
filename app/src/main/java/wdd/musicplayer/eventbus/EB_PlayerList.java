package wdd.musicplayer.eventbus;

/**
 * 播放页面内容更改
 * 非通知栏控制
 */

public class EB_PlayerList {
    public static String FAVORITE = "favorite"; //是否添加到收藏页面
    public static String NOWTIME = "nowtime";   //播放进度

    public String viewPart;
    public String time;

    //两种参数不同的构造方法 更改播放进度需提供time参数来更新数据
    public EB_PlayerList(String viewPart) {
        this.viewPart = viewPart;
    }

    public EB_PlayerList(String viewPart, String time) {
        this.viewPart = viewPart;
        this.time = time;
    }
}
