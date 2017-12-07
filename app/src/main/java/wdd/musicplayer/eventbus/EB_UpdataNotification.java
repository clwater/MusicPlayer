package wdd.musicplayer.eventbus;

import wdd.musicplayer.model.Music;

/**
 * Created by gengzhibo on 2017/12/7.
 */

public class EB_UpdataNotification {
    public Music music;

    public EB_UpdataNotification(Music music) {
        this.music = music;
    }
}
