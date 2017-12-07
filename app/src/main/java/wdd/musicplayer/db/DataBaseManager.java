package wdd.musicplayer.db;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import wdd.musicplayer.model.ListItemModel;

/**
 * Created by gengzhibo on 2017/11/15.
 */

public class DataBaseManager {
    //控制单例
    private static DataBaseManager manager;
    //数据库名称
    private static final String DB_NAME = "music.db";
    //创建LiteOrm对象
    private LiteOrm liteOrm;

    private DataBaseManager(Context context) {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(context, DB_NAME);
        }
    }

    /**
     * @param context
     * @return 保证单例对象唯一
     */
    public static DataBaseManager getInstance(Context context) {
        context = context.getApplicationContext();
        if (manager == null) {
            synchronized (DataBaseManager.class) {
                if (manager == null) {
                    manager = new DataBaseManager(context);
                }
            }
        }
        return manager;
    }

    /**
     * 插入一条记录
     */
    public <T> long insert(T t) {
        return liteOrm.save(t);
    }

    /**
     * 查询所有
     */
    public <T> List<T> queryAll(Class<T> cla) {
        return liteOrm.query(cla);
    }

    /**
     * 查询某字段 等于 Value的值
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryByWhere(Class<T> cla, String field, String[] value) {
        return liteOrm.query(new QueryBuilder(cla).where(field + "=?",  value));
    }

    /**
     * 删除某个
     */
    public <T> void delete(T t) {
        liteOrm.delete(t);
    }

    /**
     * 更新某个数据
     */
    public <T> int update(T t) {
        return liteOrm.update(t);
    }

    /**
     * 根据id查询
     */
    public <T> T query(long id, Class<T> clazz) {
        return liteOrm.queryById(id, clazz);
    }


    /**
     * @param path
     * @return
     * 获取"收藏"菜单歌曲列表
     */
    public ArrayList<ListItemModel> queryMusinInFavorite(String path){
        return liteOrm.query(new QueryBuilder<ListItemModel>(ListItemModel.class)
                .whereEquals("path", path)
                .whereAppendAnd()
                .whereEquals("parent", "收藏"));
    }

    /**
     * @param path
     * @return
     * 判断某个路径下的歌曲是否被收藏
     */
    public boolean isQueryMusinInFavorite(String path){
        return queryMusinInFavorite(path).size() > 0;
    }
}
