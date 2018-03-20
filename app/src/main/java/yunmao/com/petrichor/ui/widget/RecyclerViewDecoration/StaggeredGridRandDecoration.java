package yunmao.com.petrichor.ui.widget.RecyclerViewDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by msi on 2018/2/27.
 */
public class StaggeredGridRandDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int top;
    private int right;
    private int bottom;

    public StaggeredGridRandDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = bottom;
        if (parent.getChildAdapterPosition(view) < 2) {
            outRect.top = 2 * top;
        } else {
            outRect.top = top;
        }
        outRect.left = left;
        outRect.right = right;
    }
}
