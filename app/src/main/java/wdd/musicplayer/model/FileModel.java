package wdd.musicplayer.model;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 数据库表FileModel模型
 * 本地音乐-文件夹model
 */

@Table("FileModel")
public class FileModel {
    public FileModel(String name, String path , int number) {
        this.name = name;
        this.path = path;
        this.number = number;
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
    @NotNull
    public int number;

    public String createTime;
    public String size;
    //文件类型 0=文件夹 1=音频文件 2=其他文件
    public int type;

}
