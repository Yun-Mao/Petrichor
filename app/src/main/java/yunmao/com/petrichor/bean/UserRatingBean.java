package yunmao.com.petrichor.bean;

import java.io.Serializable;

/**
 * Created by msi on 2018/2/27.
 */
public class UserRatingBean implements Serializable {
    private int max;
    private String value;
    private int min;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
