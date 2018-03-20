package yunmao.com.petrichor.api.model;

import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.bean.table.Bookshelf;

/**
 * Created by msi on 2018/2/27.
 */
public interface IBookshelfModel {
    /**
     * 获取我的书架
     *
     * @param listener 回调
     */
    void loadBookshelf(ApiCompleteListener listener);

    /**
     * 添加一个书架
     *
     * @param title    书架名称
     * @param remark   备注
     * @param createAt 创建时间
     * @param listener 回调
     */
    void addBookshelf(String title, String remark, String createAt, ApiCompleteListener listener);

    /**
     * 修改一个书架
     *
     * @param bookshelf bookshelf
     * @param listener  回调
     */
    void updateBookshelf(Bookshelf bookshelf, ApiCompleteListener listener);

    /**
     * 排序
     *
     * @param id       id
     * @param front    前一个bookshelf order
     * @param behind   后一个bookshelf order
     * @param listener 回调
     */
    void orderBookshelf(int id, long front, long behind, ApiCompleteListener listener);

    /**
     * 清空书架
     *
     * @param id       id
     * @param listener 回调
     */
    void deleteBookshelf(String id, ApiCompleteListener listener);

    /**
     * 取消订阅
     */
    void unSubscribe();
    /**
     * 查询书架
     *
     * @param remark       ISBN
     * @param listener 回调
     */
    void findBookshelf(String remark, ApiCompleteListener listener);
}
