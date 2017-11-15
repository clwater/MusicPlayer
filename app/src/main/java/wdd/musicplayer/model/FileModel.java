package wdd.musicplayer.model;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by gengzhibo on 2017/11/15.
 */

@Table("FileModel")
public class FileModel {
    public FileModel(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public FileModel() {
    }

    //主键，自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public long id;

    @NotNull
    public String name;

    @NotNull
    public String path;

}
