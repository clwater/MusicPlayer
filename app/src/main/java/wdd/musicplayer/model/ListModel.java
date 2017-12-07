package wdd.musicplayer.model;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * ListModel表
 * 播放列表概要情况
 * 简单包含列表名称及包含的数量
 */

@Table("ListModel")
public class ListModel implements Serializable {

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
