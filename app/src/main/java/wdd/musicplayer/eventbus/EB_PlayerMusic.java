package wdd.musicplayer.eventbus;

import wdd.musicplayer.model.Music;

/**
 * Created by gengzhibo on 2017/11/14.
 */

public class EB_PlayerMusic {
    public static String LIST = "list";
    public static String ALLFILE = "allfile";
    public String tag;
    public Music music;
    public String parent;

    public EB_PlayerMusic(String tag, Music music) {
        this.tag = tag;
        this.music = music;
    }

    public EB_PlayerMusic(String tag, String parent) {
        this.tag = tag;
        this.parent = parent;
    }
}
