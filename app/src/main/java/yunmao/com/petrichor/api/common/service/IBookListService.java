package yunmao.com.petrichor.api.common.service;

import yunmao.com.petrichor.bean.http.douban.BookListResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
/**
 * Created by msi on 2018/2/27.
 * description:书籍搜索服务接口
 */
public interface IBookListService {
    @GET("book/search")
    Observable<Response<BookListResponse>> getBookList(@Query("q") String q, @Query("tag") String tag, @Query("start") int start, @Query("count") int count, @Query("fields") String fields);
}
