package yunmao.com.petrichor.api.model;

import yunmao.com.petrichor.api.ApiCompleteListener;

/**
 * Created by msi on 2018/2/27.
 */
public interface IBookListModel {
    /**
     * 获取图书接口
     */
    void loadBookList(String q, String tag, int start, int count, String fields, ApiCompleteListener listener);

    /**
     * 取消加载数据
     */
    void cancelLoading();
}
