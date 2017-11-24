package wdd.musicplayer.model;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by gengzhibo on 2017/11/23.
 */

@Table("ListItemModel")
public class ListItemModel {

    //主键，自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public long id;

    @NotNull
    public String name;
    @NotNull
    public String time;
    @NotNull
    public String artist;
    @NotNull
    public String parent;
    @NotNull
    public String path;

    public ListItemModel(String name, String time, String artist, String parent , String path) {
        this.name = name;
        this.time = time;
        this.artist = artist;
        this.parent = parent;
        this.path = path;
    }
}
