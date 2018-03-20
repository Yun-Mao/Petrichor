package yunmao.com.petrichor.api.model.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.common.DatabaseHelper;
import yunmao.com.petrichor.api.model.IBookshelfModel;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.bean.table.Bookshelf;
import yunmao.com.petrichor.utils.common.UIUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by msi on 2018/2/27.
 */
public class BookshelfModelImpl implements IBookshelfModel {
    private SqlBrite sqlBrite = SqlBrite.create();
    private BriteDatabase db = sqlBrite.wrapDatabaseHelper(DatabaseHelper.getInstance(UIUtils.getContext()), Schedulers.io());
    private Subscription subscribe;
    SQLiteDatabase db2;
    @Override
    public void loadBookshelf(ApiCompleteListener listener) {
        if (db != null) {
            Observable<SqlBrite.Query> bookshelf = db.createQuery("bookshelf", "SELECT * FROM bookshelf order by orders DESC");
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
                            List<Bookshelf> bookshelfs = new ArrayList<>();
                            while (cursor.moveToNext()) {
                                Bookshelf bookshelfBean = new Bookshelf();
                                bookshelfBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                                bookshelfBean.setBookCount(cursor.getInt(cursor.getColumnIndex("bookCount")));
                                bookshelfBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                                bookshelfBean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                                bookshelfBean.setOrder(cursor.getLong(cursor.getColumnIndex("orders")));
                                bookshelfBean.setCreateTime(cursor.getString(cursor.getColumnIndex("create_at")));
                                System.out.println(bookshelfBean.getTitle());
                                System.out.println(bookshelfBean.getOrder());
                                bookshelfs.add(bookshelfBean);
                            }
                            cursor.close();
                            listener.onComplected(bookshelfs);
                        }
                    });
        } else {
            listener.onFailed(new BaseResponse(500, "db error : init"));
        }
    }

    @Override
    public void addBookshelf(String title, String remark, String createAt, ApiCompleteListener listener) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("remark", remark);
            values.put("create_at", createAt);
            values.put("orders", System.currentTimeMillis());
            //System.out.println(values.get("orders"));
            db.insert("bookshelf", values);
        } else {
            listener.onFailed(new BaseResponse(500, "db error : add"));
        }
    }

    @Override
    public void updateBookshelf(Bookshelf bookshelf, ApiCompleteListener listener) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("title", bookshelf.getTitle());
            values.put("remark", bookshelf.getRemark());
            values.put("orders",bookshelf.getOrder());
            db.update("bookshelf", values, "id=?", bookshelf.getId() + "");
        } else {
            listener.onFailed(new BaseResponse(500, "db error : update"));
        }
    }

    @Override
    public void orderBookshelf(int id, long front, long behind, ApiCompleteListener listener) {
        if (db != null) {
            long mOrder = front + (behind - front) +1;
            ContentValues values = new ContentValues();
            values.put("orders", mOrder);
            db.update("bookshelf", values, "id=?", id + "");
        } else {
            listener.onFailed(new BaseResponse(500, "db error : update"));
        }
    }

    @Override
    public void deleteBookshelf(String id, ApiCompleteListener listener) {
        if (db != null) {
            db.delete("bookshelf", "id=?", id);
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
    public void findBookshelf(String remark, ApiCompleteListener listener){
        boolean result;
        if (db != null) {

        } else {
            listener.onFailed(new BaseResponse(500, "db error : init"));
        }

    }
}
