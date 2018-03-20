package yunmao.com.petrichor.api.presenter.impl;

import yunmao.com.petrichor.BaseApplication;
import yunmao.com.petrichor.R;
import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.model.IBookListModel;
import yunmao.com.petrichor.api.model.impl.BookListModelImpl;
import yunmao.com.petrichor.api.presenter.IBookListPresenter;
import yunmao.com.petrichor.api.view.IBookListView;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.bean.http.douban.BookListResponse;
import yunmao.com.petrichor.utils.common.NetworkUtils;

/**
 * Created by msi on 2018/2/27.
 */
public class BookListPresenterImpl implements IBookListPresenter, ApiCompleteListener {
    private IBookListView mBookListView;
    private IBookListModel mBookListModel;

    public BookListPresenterImpl(IBookListView view) {
        mBookListView = view;
        mBookListModel = new BookListModelImpl();
    }

    /**
     * 加载数据
     */
    @Override
    public void loadBooks(String q, String tag, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(BaseApplication.getApplication())) {
            mBookListView.showMessage(BaseApplication.getApplication().getString(R.string.poor_network));
            mBookListView.hideProgress();
//            return;
        }
        mBookListView.showProgress();
        mBookListModel.loadBookList(q, tag, start, count, fields, this);
    }

    @Override
    public void cancelLoading() {
        mBookListModel.cancelLoading();
    }

    /**
     * 访问接口成功
     *
     * @param result 返回结果
     */
    @Override
    public void onComplected(Object result) {
        if (result instanceof BookListResponse) {
            int index = ((BookListResponse) result).getStart();
            if (index == 0) {
                mBookListView.refreshData(result);
            } else {
                mBookListView.addData(result);
            }
            mBookListView.hideProgress();
        }
    }

    /**
     * 请求失败
     *
     * @param msg 错误信息
     */
    @Override
    public void onFailed(BaseResponse msg) {
        mBookListView.hideProgress();
        if (msg == null) {
            return;
        }
        mBookListView.showMessage(msg.getMsg());
    }
}
