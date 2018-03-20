package yunmao.com.petrichor.api.presenter;

/**
 * Created by msi on 2018/2/27.
 */
public interface IBookListPresenter {
    void loadBooks(String q, String tag, int start, int count, String fields);

    void cancelLoading();
}
