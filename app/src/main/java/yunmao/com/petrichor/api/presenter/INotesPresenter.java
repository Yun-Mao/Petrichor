package yunmao.com.petrichor.api.presenter;

import yunmao.com.petrichor.bean.table.Notes;

/**
 * Created by msi on 2018/3/5.
 * .Presenter是从Model中获取数据并提供给View的层，Presenter还负责处理后台任务。
 */

public interface INotesPresenter {
    /**
     * 获取我的书架
     */
    void loadNotes();

    /**
     * 添加一个书摘
     *
     * @param title    书
     * @param paper   原文
     * @param note   书摘
     * @param createAt 创建时间
     */
    void addNotes(String title, String paper, String note, int page,String createAt);

    /**
     * 修改一个书架
     *
     * @param notes notes
     */
    void updateNotes(Notes notes);

    /**
     * 排序
     *
     * @param id     id
     * @param front  前一个bookshelf order
     * @param behind 后一个bookshelf order
     */
    void orderNotes(int id, long front, long behind);

    /**
     * 清空书架
     *
     * @param id id
     */
    void deleteNotes(String id);

    /**
     * 取消订阅
     */
    void unSubscribe();

    void findNotes(String name);
}
