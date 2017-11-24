package wdd.musicplayer.eventbus;

import wdd.musicplayer.model.ListModel;

/**
 * Created by gengzhibo on 2017/11/24.
 */

public class EB_RenamePlayerListName {
    public ListModel listModel;

    public EB_RenamePlayerListName(ListModel listModel) {
        this.listModel = listModel;
    }
}
