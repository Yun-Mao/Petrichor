package yunmao.com.petrichor.api.common.service;

import yunmao.com.petrichor.bean.http.douban.BookSeriesListResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by msi on 2018/2/27.
 * description:丛书服务接口
 */
public interface IBookSeriesService {
    @GET("book/series/{seriesId}/books")
    Observable<Response<BookSeriesListResponse>> getBookSeries(@Path("seriesId") String seriesId, @Query("start") int start, @Query("count") int count, @Query("fields") String fields);

}
