package yunmao.com.petrichor.api;

import yunmao.com.petrichor.bean.http.douban.BaseResponse;
/**
 * Created by msi on 2018/2/27.
 * View是指显示数据并且和用户交互的层。在安卓中，它们可以是一个Activity，一个Fragment，一个android.view.View或者是一个Dialog。
 */
//网络请求回掉
public interface ApiCompleteListener {
    void onComplected(Object result);

    void onFailed(BaseResponse msg);
}