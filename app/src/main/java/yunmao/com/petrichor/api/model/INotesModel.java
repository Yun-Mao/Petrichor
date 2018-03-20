package yunmao.com.petrichor.api.model;

import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.bean.table.Notes;

/**
 * Created by msi on 2018/3/5.
 * .Model 是数据源层。比如数据库接口或者远程服务器的api。
 */

public interface INotesModel {
    /**
     * 获取我的书摘
     *
     * @param listener 回调
     */
    void loadNotes(ApiCompleteListener listener);

    /**
     * 添加一个书摘
     *
     * @param title    书
     * @param paper   原文
     * @param note   书摘
     * @param createAt 创建时间
     * @param listener 回调
     */
    void addNotes(String title, String paper, String note,int page, String createAt, ApiCompleteListener listener);

    /**
     * 修改一个书架
     *
     * @param notes notes
     * @param listener  回调
     */
    void updateNotes(Notes notes, ApiCompleteListener listener);
    /**
     * 排序
     *
     * @param id       id
     * @param front    前一个notes order
     * @param behind   后一个notes order
     * @param listener 回调
     */
    void orderNotes(int id, long front, long behind, ApiCompleteListener listener);

    /**
     * 清空书架
     *
     * @param id       id
     * @param listener 回调
     */
    void deleteNotes(String id, ApiCompleteListener listener);

    /**
     * 取消订阅
     */
    void unSubscribe();
    /**
     * 查询书架
     *
     * @param name       name
     * @param listener 回调
     */
    void findNotes(String name, ApiCompleteListener listener);
}
