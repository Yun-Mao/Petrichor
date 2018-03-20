package yunmao.com.petrichor.api.model.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.common.DatabaseHelper;
import yunmao.com.petrichor.api.model.INotesModel;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.bean.table.Notes;
import yunmao.com.petrichor.utils.common.UIUtils;

/**
 * Created by msi on 2018/3/5.
 */

public class NotesModelImpl implements INotesModel {
    private SqlBrite sqlBrite = SqlBrite.create();
    private BriteDatabase db = sqlBrite.wrapDatabaseHelper(DatabaseHelper.getInstance(UIUtils.getContext()), Schedulers.io());
    private Subscription subscribe;

    @Override
    public void loadNotes(ApiCompleteListener listener) {
        if (db != null) {
            Observable<SqlBrite.Query> bookshelf = db.createQuery("notes", "SELECT * FROM notes order by orders DESC");
            subscribe = bookshelf.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SqlBrite.Query>() {
                        @Override
                        public void call(SqlBrite.Query query) {
                            Cursor cursor = query.run();
                            if (cursor == null || cursor.getCount() < 0) {
                                cursor.close();
                                return;
                            }
                            List<Notes> notes = new ArrayList<>();
                            while (cursor.moveToNext()) {
                                Notes notesBean = new Notes();
                                notesBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                                notesBean.setNote(cursor.getString(cursor.getColumnIndex("note")));
                                notesBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                                notesBean.setPaper(cursor.getString(cursor.getColumnIndex("paper")));
                                notesBean.setPage(cursor.getInt(cursor.getColumnIndex("page")));
                                notesBean.setOrder(cursor.getLong(cursor.getColumnIndex("orders")));
                                notesBean.setCreateTime(cursor.getString(cursor.getColumnIndex("create_at")));
                                notes.add(notesBean);
                            }
                            cursor.close();
                            listener.onComplected(notes);
                        }
                    });
        } else {
            listener.onFailed(new BaseResponse(500, "db error : init"));
        }
    }

    @Override
    public void addNotes(String title, String paper, String note, int page, String createAt, ApiCompleteListener listener) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("paper", paper);
            values.put("note", note);
            values.put("page", page);
            values.put("create_at", createAt);
            values.put("orders", System.currentTimeMillis());
            db.insert("notes", values);
        } else {
            listener.onFailed(new BaseResponse(500, "db error : add"));
        }
    }

    @Override
    public void updateNotes(Notes notes, ApiCompleteListener listener) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("title", notes.getTitle());
            values.put("paper", notes.getPaper());
            values.put("note", notes.getNote());
            values.put("page", notes.getPage());
            values.put("orders", notes.getOrder());
            db.update("notes", values, "id=?", notes.getId() + "");
        } else {
            listener.onFailed(new BaseResponse(500, "db error : update"));
        }
    }
    @Override
    public void orderNotes(int id, long front, long behind, ApiCompleteListener listener) {
        if (db != null) {
            long mOrder = front + (behind - front) / 2;
            ContentValues values = new ContentValues();
            values.put("orders", mOrder);
            db.update("notes", values, "id=?", id + "");
        } else {
            listener.onFailed(new BaseResponse(500, "db error : update"));
        }
    }

    @Override
    public void deleteNotes(String id, ApiCompleteListener listener) {
        if (db != null) {
            db.delete("notes", "id=?", id);
        } else {
            listener.onFailed(new BaseResponse(500, "db error : delete"));
        }
    }

    @Override
    public void unSubscribe() {
        if (subscribe != null) {
            subscribe.unsubscribe();
            db.close();
        }
    }
    public void findNotes(String name, ApiCompleteListener listener){
        if (db != null) {
            Observable<SqlBrite.Query> bookshelf = db.createQuery("notes", "SELECT * FROM notes where title='"+name+"'"+"order by page");
            subscribe = bookshelf.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SqlBrite.Query>() {
                        @Override
                        public void call(SqlBrite.Query query) {
                            Cursor cursor = query.run();
                            //System.out.println(name);
                            //System.out.println(cursor.getCount());
                            if (cursor == null || cursor.getCount() < 0) {
                                cursor.close();
                                return;
                            }
                            List<Notes> notes = new ArrayList<>();
                            while (cursor.moveToNext()) {
                                Notes notesBean = new Notes();
                                notesBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                                notesBean.setNote(cursor.getString(cursor.getColumnIndex("note")));
                                notesBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                                notesBean.setPaper(cursor.getString(cursor.getColumnIndex("paper")));
                                notesBean.setPage(cursor.getInt(cursor.getColumnIndex("page")));
                                notesBean.setOrder(cursor.getLong(cursor.getColumnIndex("orders")));
                                notesBean.setCreateTime(cursor.getString(cursor.getColumnIndex("create_at")));
                                notes.add(notesBean);
                            }
                            cursor.close();
                            listener.onComplected(notes);
                        }
                    });
        } else {
            listener.onFailed(new BaseResponse(500, "db error : init"));
        }
    }
}
