package wdd.musicplayer.model;

/**
 * 播放模式
 */

public class PlayModel {
    public final static int SINGLE = 0,
            LIST = 1,
            LOOP = 2,
            SHUFFLE = 3;
    public int index;

    //构造函数 初始化模式为LOOP
    public PlayModel() {
        index = LOOP;
    }

    //更换模式,在四种模式中循环更换
    public void next(){
        index++;
        if (index > SHUFFLE){
            index = SINGLE;
        }
    }
}
