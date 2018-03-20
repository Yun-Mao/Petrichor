package yunmao.com.petrichor.api.common.service;

import yunmao.com.petrichor.bean.http.douban.BookReviewsListResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
/**
 * Created by msi on 2018/2/27.
 * description:书籍评论服务接口
 */
public interface IBookReviewsService {
    @GET("book/{bookId}/reviews")
    Observable<Response<BookReviewsListResponse>> getBookReviews(@Path("bookId") String bookId, @Query("start") int start, @Query("count") int count, @Query("fields") String fields);
}
