package yunmao.com.petrichor.api.view;

/**
 * Created by msi on 2018/3/5.
 */

public interface INotesView {
    void showMessage(String msg);

    void showProgress();

    void hideProgress();

    void refreshData(Object result);

    void addData(Object result);
}
