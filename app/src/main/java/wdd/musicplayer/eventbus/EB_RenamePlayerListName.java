package wdd.musicplayer.eventbus;

import wdd.musicplayer.model.ListModel;

/**
 * 重命名播放列表后更新列表名称
 */

public class EB_RenamePlayerListName {
    public ListModel listModel;

    public EB_RenamePlayerListName(ListModel listModel) {
        this.listModel = listModel;
    }
}
