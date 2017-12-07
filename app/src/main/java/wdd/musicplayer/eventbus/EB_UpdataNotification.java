package wdd.musicplayer.eventbus;

import wdd.musicplayer.model.Music;

/**
 * 更新通知栏中的页面信息
 */

public class EB_UpdataNotification {
    public Music music;

    public EB_UpdataNotification(Music music) {
        this.music = music;
    }
}
