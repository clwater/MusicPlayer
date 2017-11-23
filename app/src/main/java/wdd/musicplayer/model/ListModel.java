package wdd.musicplayer.model;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by gengzhibo on 2017/11/23.
 */

@Table("ListItemModel")
public class ListModel {

    //主键，自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public long id;

    @NotNull
    public String name;
    @NotNull
    public int number;

    public ListModel(String name, int number) {
        this.name = name;
        this.number = number;
    }
}
