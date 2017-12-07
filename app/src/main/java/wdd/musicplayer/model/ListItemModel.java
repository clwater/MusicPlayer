package wdd.musicplayer.model;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * ListItemModel表
 * 主要作用是保存播放列表的详细
 * 通过parent键区分在哪个播放列表中
 */

@Table("ListItemModel")
public class ListItemModel {

    //主键，自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public long id;

    @NotNull
    public String name;
    @NotNull
    public int time;
    @NotNull
    public String artist;
    @NotNull
    public String parent;
    @NotNull
    public String path;

    public ListItemModel(String name, int time, String artist, String parent , String path) {
        this.name = name;
        this.time = time;
        this.artist = artist;
        this.parent = parent;
        this.path = path;
    }
}
