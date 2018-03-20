package yunmao.com.petrichor.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import yunmao.com.petrichor.R;
import yunmao.com.petrichor.api.presenter.impl.BookshelfPresenterImpl;
import yunmao.com.petrichor.api.presenter.impl.NotesPresenterImpl;
import yunmao.com.petrichor.api.view.INotesView;
import yunmao.com.petrichor.bean.table.Notes;
import yunmao.com.petrichor.holder.BookShelfEditorHolder;
import yunmao.com.petrichor.holder.NotesEditorHolder;
import yunmao.com.petrichor.ui.adapter.NotesAdapter;
import yunmao.com.petrichor.ui.fragment.NotesCFragment;
import yunmao.com.petrichor.ui.widget.RecyclerViewDecoration.StaggeredGridDecoration;
import yunmao.com.petrichor.utils.common.DensityUtils;
import yunmao.com.petrichor.utils.common.KeyBoardUtils;
import yunmao.com.petrichor.utils.common.TimeUtils;
import yunmao.com.petrichor.utils.common.UIUtils;

/**
 * Created by msi on 2018/3/13.
 */

public class NotesActivity extends BaseActivity implements INotesView, SwipeRefreshLayout.OnRefreshListener{
//    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //@BindView(R.id.fab)
    FloatingActionButton mFab;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;
    private NotesAdapter mNotesAdapter;
    private List<Notes> mNotes;
    private NotesPresenterImpl mNotesPresenter;
    private BookshelfPresenterImpl mBookshelfPresenter;
    private int spanCount = 1;
    private boolean isSortable;
    String name;
    private ItemTouchHelper touchHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =getIntent();
        name=intent.getStringExtra("name");
        //setContentView(R.layout.notes_fragment);

    }

    @Override
    @SuppressLint("RestrictedApi")
    protected void initEvents() {
        setContentView(R.layout.notes_fragment);
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mSwipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        mToolbar.setTitle("Notes");
        spanCount = getResources().getInteger(R.integer.home_span_count);
        mNotesPresenter = new NotesPresenterImpl(this);
        //mBookshelfPresenter = new BookshelfPresenterImpl(this);
        mNotes = new ArrayList<>();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);

        //设置布局管理器
        mLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mNotesAdapter.getItemColumnSpan(position);
            }
        });
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
//
        //设置adapter
        mNotesAdapter = new NotesAdapter(getApplicationContext(), mNotes, spanCount);
        mNotesAdapter.setSortable(isSortable);
        mRecyclerView.setAdapter(mNotesAdapter);
//
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new NotesActivity.RecyclerViewScrollDetector());
        final int space = DensityUtils.dp2px(this, 4);
        mRecyclerView.addItemDecoration(new StaggeredGridDecoration(space, space, space, space, spanCount));
        mSwipeRefreshLayout.setOnRefreshListener(this);
 //       mFab.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_action_add_white));
//        mFab.setOnClickListener(v -> {
//            final BookShelfEditorHolder bookShelfHolder = new BookShelfEditorHolder(getActivity(), "", "");
//            final NotesEditorHolder notesHolder = new NotesEditorHolder(getActivity(), "", "","",0);
//            final int inputSpace = DensityUtils.dp2px(getActivity(), 16);
//            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setCancelable(false)
//                    .setView(notesHolder.getContentView(), inputSpace, inputSpace, inputSpace, inputSpace)
//                    .setTitle(UIUtils.getContext().getString(R.string.add_notes))
//                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
//                        dialog.dismiss();
//                        KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getActivity());
//                    })
//                    .setPositiveButton(R.string.ok, (dialog, which) -> {
//
//                        if (!notesHolder.check()) {
//                            Snackbar.make(BaseActivity.activity.getToolbar(), R.string.notes_name_is_empty, Snackbar.LENGTH_SHORT).show();
//                        } else {
//                            mNotesPresenter.addNotes(notesHolder.getName(), notesHolder.getPaper(),notesHolder.getNote(),notesHolder.getPage(), TimeUtils.getCurrentTime());
//                            mBookshelfPresenter.addBookshelf(notesHolder.getName(), bookShelfHolder.getRemark(), TimeUtils.getCurrentTime());
//                        }
//                        KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getActivity());
//                    }).create().show();
//        });
        mNotesAdapter.setOnItemClickLitener(new NotesAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }

            @Override
            public void onItemLongClick(View view, int position)
            {
//                final Notes note = mNotes.get(position);
//                final NotesEditorHolder notesHolder = new NotesEditorHolder(getBaseContext(),note.getTitle(), note.getPaper(), note.getNote(),note.getPage());
//                final int inputSpace = DensityUtils.dp2px(getBaseContext(), 16);
//                final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//                builder.setCancelable(false)
//                        .setView(notesHolder.getContentView(), inputSpace, inputSpace, inputSpace, inputSpace)
//                        .setTitle(UIUtils.getContext().getString(R.string.edit_notes))
//                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
//                            dialog.dismiss();
//                            KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getBaseContext());
//                        })
//                        .setPositiveButton(R.string.ok, (dialog, which) -> {
//                            if (!notesHolder.check()) {
//                                Snackbar.make(BaseActivity.activity.getToolbar(), R.string.notes_name_is_empty, Snackbar.LENGTH_SHORT).show();
//                            } else {
//                                note.setTitle(notesHolder.getName());
//                                note.setPaper(notesHolder.getPaper());
//                                note.setNote(notesHolder.getNote());
//                                mNotesPresenter.updateNotes(note);
//                            }
//                            KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getBaseContext());
//                        }).create().show();
            }
        });
//        touchHelper = new ItemTouchHelper(new NotesCFragment.SimpleItemTouchHelperCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
    }
    @Override
    public void onRefresh() {
        mNotesPresenter.findNotes(name);
    }
    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void addData(Object result) {

    }

    class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {
        private int lastVisibleItem;
        private int mScrollThreshold = DensityUtils.dp2px(UIUtils.getContext(), 1);

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mNotesAdapter.getItemCount()) {
                onLoadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;

            if (isSignificantDelta) {
                if (dy > 0) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    }
    private void onLoadMore() {

    }
    @Override
    public void showProgress() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
    }  @Override
    public void refreshData(Object result) {
        if (result instanceof List) {
            mNotes.clear();
            mNotes.addAll((List<Notes>) result);
            mNotesAdapter.notifyDataSetChanged();
        }
    }


}
