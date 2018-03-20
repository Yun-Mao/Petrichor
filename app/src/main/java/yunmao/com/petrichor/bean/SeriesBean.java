package yunmao.com.petrichor.bean;

import java.io.Serializable;

/**
 * Created by msi on 2018/2/27.
 */
public class SeriesBean implements Serializable{
    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
