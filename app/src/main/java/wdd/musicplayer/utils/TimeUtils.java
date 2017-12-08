package wdd.musicplayer.utils;

/**
 * 时间工具类
 */

public class TimeUtils {
    /**
     * @param longTime
     * @return
     * 格式化时间展示
     */
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
