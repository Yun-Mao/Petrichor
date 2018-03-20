package yunmao.com.petrichor.api.presenter.impl;

import java.util.List;

import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.model.IBookshelfModel;
import yunmao.com.petrichor.api.model.INotesModel;
import yunmao.com.petrichor.api.model.impl.BookshelfModelImpl;
import yunmao.com.petrichor.api.model.impl.NotesModelImpl;
import yunmao.com.petrichor.api.presenter.INotesPresenter;
import yunmao.com.petrichor.api.view.IBookListView;
import yunmao.com.petrichor.api.view.INotesView;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.bean.table.Bookshelf;
import yunmao.com.petrichor.bean.table.Notes;

/**
 * Created by msi on 2018/3/5.
 */

public class NotesPresenterImpl implements INotesPresenter, ApiCompleteListener {
    private INotesView mNotesView;
    private INotesModel mNotesModel;

    public NotesPresenterImpl(INotesView view) {
        mNotesView = view;
        mNotesModel = new NotesModelImpl();
    }

    @Override
    public void loadNotes() {
        mNotesView.showProgress();
        mNotesModel.loadNotes(this);
    }
    @Override
    public void addNotes(String title, String paper, String note,int page, String createAt) {
        mNotesView.showProgress();
        mNotesModel.addNotes(title, paper,note,page, createAt, this);
    }

    @Override
    public void updateNotes(Notes notes) {
        mNotesView.showProgress();
        mNotesModel.updateNotes(notes, this);
    }

    @Override
    public void orderNotes(int id, long front, long behind) {
        mNotesView.showProgress();
        mNotesModel.orderNotes(id, front, behind, this);
    }

    @Override
    public void deleteNotes(String id) {
        mNotesView.showProgress();
        mNotesModel.deleteNotes(id, this);
    }

    @Override
    public void unSubscribe() {
        mNotesModel.unSubscribe();
    }

    @Override
    public void onComplected(Object result) {
        if (result instanceof List) {
            mNotesView.refreshData(result);
        } else if (result instanceof BaseResponse) {
            mNotesView.showMessage(((BaseResponse) result).getCode() + "|" + ((BaseResponse) result).getMsg());
        }
        mNotesView.hideProgress();
    }

    @Override
    public void onFailed(BaseResponse msg) {
        mNotesView.hideProgress();
        if (msg != null) {
            mNotesView.showMessage(msg.getCode() + "|" + msg.getMsg());
        }
    }
    @Override
    public void findNotes(String name) {
        mNotesView.showProgress();
        mNotesModel.findNotes(name,this);
    }
}
