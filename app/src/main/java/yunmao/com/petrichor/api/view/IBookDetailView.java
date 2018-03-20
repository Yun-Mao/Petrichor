package yunmao.com.petrichor.api.view;

/**
 * Created by msi on 2018/2/27.
 */
public interface IBookDetailView {
    void showMessage(String msg);

    void showProgress();

    void hideProgress();

    void updateView(Object result);
}
