package yunmao.com.petrichor.api.presenter.impl;

import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.model.IBookshelfModel;
import yunmao.com.petrichor.api.model.impl.BookshelfModelImpl;
import yunmao.com.petrichor.api.presenter.IBookshelfPresenter;
import yunmao.com.petrichor.api.view.IBookListView;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.bean.table.Bookshelf;

import java.util.List;

/**
 * Created by msi on 2018/2/27.
 */
public class BookshelfPresenterImpl implements IBookshelfPresenter, ApiCompleteListener {
    private IBookListView mBookListView;
    private IBookshelfModel mBookshelfModel;

    public BookshelfPresenterImpl(IBookListView view) {
        mBookListView = view;
        mBookshelfModel = new BookshelfModelImpl();
    }

    @Override
    public void loadBookshelf() {
        mBookListView.showProgress();
        mBookshelfModel.loadBookshelf(this);
    }

    @Override
    public void addBookshelf(String title, String remark, String createAt) {
        mBookListView.showProgress();
        mBookshelfModel.addBookshelf(title, remark, createAt, this);
    }

    @Override
    public void updateBookshelf(Bookshelf bookshelf) {
        mBookListView.showProgress();
        mBookshelfModel.updateBookshelf(bookshelf, this);
    }

    @Override
    public void orderBookshelf(int id, long front, long behind) {
        mBookListView.showProgress();
        mBookshelfModel.orderBookshelf(id, front, behind, this);
    }

    @Override
    public void deleteBookshelf(String id) {
        mBookListView.showProgress();
        mBookshelfModel.deleteBookshelf(id, this);
    }

    @Override
    public void unSubscribe() {
        mBookshelfModel.unSubscribe();
    }

    @Override
    public void onComplected(Object result) {
        if (result instanceof List) {
            mBookListView.refreshData(result);
        } else if (result instanceof BaseResponse) {
            mBookListView.showMessage(((BaseResponse) result).getCode() + "|" + ((BaseResponse) result).getMsg());
        }
        mBookListView.hideProgress();
    }

    @Override
    public void onFailed(BaseResponse msg) {
        mBookListView.hideProgress();
        if (msg != null) {
            mBookListView.showMessage(msg.getCode() + "|" + msg.getMsg());
        }
    }
    @Override
    public void findBookshelf(String remark) {
        mBookListView.showProgress();
        mBookshelfModel.findBookshelf(remark,this);
    }
}
