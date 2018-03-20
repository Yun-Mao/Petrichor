package yunmao.com.petrichor.api.presenter.impl;

import yunmao.com.petrichor.R;
import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.model.IBookDetailModel;
import yunmao.com.petrichor.api.model.impl.BookDetailModelImpl;
import yunmao.com.petrichor.api.presenter.IBookDetailPresenter;
import yunmao.com.petrichor.api.view.IBookDetailView;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.utils.common.NetworkUtils;
import yunmao.com.petrichor.utils.common.UIUtils;
/**
 * Created by msi on 2018/2/27.
 */
public class BookDetailPresenterImpl implements IBookDetailPresenter, ApiCompleteListener {
    private IBookDetailView mBookDetailView;
    private IBookDetailModel mBookDetailModel;

    public BookDetailPresenterImpl(IBookDetailView view) {
        mBookDetailView = view;
        mBookDetailModel = new BookDetailModelImpl();
    }

    @Override
    public void loadReviews(String bookId, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(UIUtils.getContext())) {
            mBookDetailView.showMessage(UIUtils.getContext().getString(R.string.poor_network));
            mBookDetailView.hideProgress();
        }
        mBookDetailView.showProgress();
        mBookDetailModel.loadReviewsList(bookId, start, count, fields, this);
    }

    @Override
    public void loadSeries(String SeriesId, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(UIUtils.getContext())) {
            mBookDetailView.showMessage(UIUtils.getContext().getString(R.string.poor_network));
//            mBookDetailView.hideProgress();
        }
//        mBookDetailView.showProgress();
        mBookDetailModel.loadSeriesList(SeriesId, start, count, fields, this);
    }

    @Override
    public void cancelLoading() {
        mBookDetailModel.cancelLoading();
    }

    /**
     * 访问接口成功
     *
     * @param result
     */
    @Override
    public void onComplected(Object result) {
        mBookDetailView.updateView(result);
        mBookDetailView.hideProgress();
    }

    /**
     * 取消正在加载的http请求
     */
    @Override
    public void onFailed(BaseResponse msg) {
        mBookDetailView.hideProgress();
        if (msg == null) {
            return;
        }
        mBookDetailView.showMessage(msg.getMsg());
    }
}
