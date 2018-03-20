package yunmao.com.petrichor.api.model.impl;

import yunmao.com.petrichor.api.ApiCompleteListener;
import yunmao.com.petrichor.api.common.ServiceFactory;
import yunmao.com.petrichor.api.common.service.IBookReviewsService;
import yunmao.com.petrichor.api.common.service.IBookSeriesService;
import yunmao.com.petrichor.api.model.IBookDetailModel;
import yunmao.com.petrichor.bean.http.douban.BaseResponse;
import yunmao.com.petrichor.bean.http.douban.BookReviewsListResponse;
import yunmao.com.petrichor.bean.http.douban.BookSeriesListResponse;
import yunmao.com.petrichor.common.URL;

import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by msi on 2018/2/27.
 */
//图书详细页
public class BookDetailModelImpl implements IBookDetailModel {

    @Override
    public void loadReviewsList(String bookId, int start, int count, String fields, final ApiCompleteListener listener) {
        IBookReviewsService iBookReviewsService = ServiceFactory.createService(URL.HOST_URL_DOUBAN, IBookReviewsService.class);
        iBookReviewsService.getBookReviews(bookId, start, count, fields)
                .subscribeOn(Schedulers.io())    //请求在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<Response<BookReviewsListResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listener.onFailed(null);
                            return;
                        }
                        listener.onFailed(new BaseResponse(404, e.getMessage()));
                    }

                    @Override
                    public void onNext(Response<BookReviewsListResponse> bookReviewsResponse) {
                        if (bookReviewsResponse.isSuccessful()) {
                            listener.onComplected(bookReviewsResponse.body());
                        } else {
                            listener.onFailed(new BaseResponse(bookReviewsResponse.code(), bookReviewsResponse.message()));
                        }

                    }
                });
    }

    @Override
    public void loadSeriesList(String SeriesId, int start, int count, String fields, final ApiCompleteListener listener) {
        IBookSeriesService iBookSeriesService = ServiceFactory.createService(URL.HOST_URL_DOUBAN, IBookSeriesService.class);
        iBookSeriesService.getBookSeries(SeriesId, start, count, fields)
                .subscribeOn(Schedulers.newThread())    //请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<Response<BookSeriesListResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listener.onFailed(null);
                            return;
                        }
                        listener.onFailed(new BaseResponse(404, e.getMessage()));
                    }

                    @Override
                    public void onNext(Response<BookSeriesListResponse> bookSeriesResponse) {
                        if (bookSeriesResponse.isSuccessful()) {
                            listener.onComplected(bookSeriesResponse.body());
                        } else {
                            listener.onFailed(new BaseResponse(bookSeriesResponse.code(), bookSeriesResponse.message()));
                        }

                    }
                });
    }

    @Override
    public void cancelLoading() {

    }
}
