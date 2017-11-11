package wdd.musicplayer.model;

/**
 * Created by yszsyf on 2017/11/11.
 */

public class PlayMode {
    public final static int SINGLE = 0,
               LOOP = 1,
               LIST = 2,
               SHUFFLE = 3;
    public int index;

    //构造函数 初始化模式为LOOP
    public PlayMode() {
        index = SINGLE;
    }

    //更换模式,在四种模式中循环更换
    public void next(){
        index++;
        if (index > SHUFFLE){
            index = SINGLE;
        }
    }
}
