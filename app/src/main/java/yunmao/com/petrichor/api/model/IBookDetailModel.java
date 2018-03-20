package yunmao.com.petrichor.api.model;

import yunmao.com.petrichor.api.ApiCompleteListener;
/**
 * Created by msi on 2018/2/27.
 */
public interface IBookDetailModel {
    /**
     * 获取图书评论
     */
    void loadReviewsList(String bookId, int start, int count, String fields, ApiCompleteListener listener);

    /**
     * 获取推荐丛书
     */
    void loadSeriesList(String SeriesId, int start, int count, String fields, ApiCompleteListener listener);

    /**
     * 取消加载数据
     */
    void cancelLoading();
}
