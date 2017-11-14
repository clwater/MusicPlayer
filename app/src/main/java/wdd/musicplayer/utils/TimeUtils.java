package wdd.musicplayer.utils;

/**
 * Created by gengzhibo on 2017/11/14.
 */

public class TimeUtils {
    public static String tranTime(int longTime){
        //获取时间 单位为毫秒
        int time = Integer.valueOf(longTime);
        //将单位更改为秒
        time = time / 1000;
        //获取分钟数量
        int Mtime = time / 60;
        //获取秒数
        int Stime = time % 60;
        //格式化为分钟:秒的显示样式
        String showTime = String.format("%02d:%02d" , Mtime, Stime);

        return showTime;
    }
}
