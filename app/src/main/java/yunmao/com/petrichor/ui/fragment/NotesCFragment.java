package yunmao.com.petrichor.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import butterknife.BindView;
import yunmao.com.petrichor.R;
import yunmao.com.petrichor.api.presenter.impl.BookshelfPresenterImpl;
import yunmao.com.petrichor.api.presenter.impl.NotesPresenterImpl;
import yunmao.com.petrichor.api.view.IBookListView;
import yunmao.com.petrichor.api.view.INotesView;
import yunmao.com.petrichor.bean.table.Bookshelf;
import yunmao.com.petrichor.bean.table.Notes;
import yunmao.com.petrichor.holder.BookShelfEditorHolder;
import yunmao.com.petrichor.holder.NotesEditorHolder;
import yunmao.com.petrichor.ui.activity.BaseActivity;
import yunmao.com.petrichor.ui.activity.MainActivity;
import yunmao.com.petrichor.ui.adapter.BookShelfAdapter;
import yunmao.com.petrichor.ui.adapter.NotesAdapter;
import yunmao.com.petrichor.ui.widget.RecyclerViewDecoration.StaggeredGridDecoration;
import yunmao.com.petrichor.utils.common.DensityUtils;
import yunmao.com.petrichor.utils.common.KeyBoardUtils;
import yunmao.com.petrichor.utils.common.TimeUtils;
import yunmao.com.petrichor.utils.common.UIUtils;

/**
 * Created by msi on 2018/3/5.
 */

public class NotesCFragment extends BaseFragment implements INotesView, SwipeRefreshLayout.OnRefreshListener,IBookListView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;
    private NotesAdapter mNotesAdapter;
    private List<Notes> mNotes;
    private NotesPresenterImpl mNotesPresenter;
    private BookshelfPresenterImpl mBookshelfPresenter;
    private int spanCount = 1;
    private boolean isSortable;

    private ItemTouchHelper touchHelper;

    public static NotesCFragment mInstance;

    public static NotesCFragment newInstance() {
        Bundle args = new Bundle();
        NotesCFragment fragment = new NotesCFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.notes_fragment, container, false);
        if (savedInstanceState != null) {
            isSortable = savedInstanceState.getBoolean("isSortable");
        }
    }

    @Override
    @SuppressLint("RestrictedApi")
    protected void initEvents() {
        mToolbar.setTitle("Notes");
        spanCount = getResources().getInteger(R.integer.home_span_count);
        mNotesPresenter = new NotesPresenterImpl(this);
        mBookshelfPresenter = new BookshelfPresenterImpl(this);
        mNotes = new ArrayList<>();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);

        //设置布局管理器
        mLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mNotesAdapter.getItemColumnSpan(position);
            }
        });
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //设置adapter
        mNotesAdapter = new NotesAdapter(getActivity(), mNotes, spanCount);
        mNotesAdapter.setSortable(isSortable);
        mRecyclerView.setAdapter(mNotesAdapter);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new NotesCFragment.RecyclerViewScrollDetector());
        final int space = DensityUtils.dp2px(getActivity(), 4);
        mRecyclerView.addItemDecoration(new StaggeredGridDecoration(space, space, space, space, spanCount));
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mFab.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.ic_action_add_white));
        mFab.setOnClickListener(v -> {
            final BookShelfEditorHolder bookShelfHolder = new BookShelfEditorHolder(getActivity(), "", "");
            final NotesEditorHolder notesHolder = new NotesEditorHolder(getActivity(), "", "","",0);
            final int inputSpace = DensityUtils.dp2px(getActivity(), 16);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false)
                    .setView(notesHolder.getContentView(), inputSpace, inputSpace, inputSpace, inputSpace)
                    .setTitle(UIUtils.getContext().getString(R.string.add_notes))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                        KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getActivity());
                    })
                    .setPositiveButton(R.string.ok, (dialog, which) -> {

                        if (!notesHolder.check()) {
                            Snackbar.make(BaseActivity.activity.getToolbar(), R.string.notes_name_is_empty, Snackbar.LENGTH_SHORT).show();
                        } else {
                            mNotesPresenter.addNotes(notesHolder.getName(), notesHolder.getPaper(),notesHolder.getNote(),notesHolder.getPage(), TimeUtils.getCurrentTime());
                            mBookshelfPresenter.addBookshelf(notesHolder.getName(), bookShelfHolder.getRemark(), TimeUtils.getCurrentTime());
                        }
                        KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getActivity());
                    }).create().show();
        });
        mNotesAdapter.setOnItemClickLitener(new NotesAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                final Notes note = mNotes.get(position);
                final NotesEditorHolder notesHolder = new NotesEditorHolder(getActivity(),note.getTitle(), note.getPaper(), note.getNote(),note.getPage());
                final int inputSpace = DensityUtils.dp2px(getActivity(), 16);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false)
                        .setView(notesHolder.getContentView(), inputSpace, inputSpace, inputSpace, inputSpace)
                        .setTitle(UIUtils.getContext().getString(R.string.edit_notes))
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                            KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getActivity());
                        })
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            if (!notesHolder.check()) {
                                Snackbar.make(BaseActivity.activity.getToolbar(), R.string.notes_name_is_empty, Snackbar.LENGTH_SHORT).show();
                            } else {
                                note.setTitle(notesHolder.getName());
                                note.setPaper(notesHolder.getPaper());
                                note.setNote(notesHolder.getNote());
                                mNotesPresenter.updateNotes(note);
                            }
                            KeyBoardUtils.closeKeyBord(notesHolder.et_notes_name, getActivity());
                        }).create().show();
            }
        });
        touchHelper = new ItemTouchHelper(new NotesCFragment.SimpleItemTouchHelperCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            if (isSortable) {
                for(int i = 0 ; i < mNotes.size() ; i++) {
                    System.out.println(mNotes.get(i).getTitle());
                    System.out.println(mNotes.get(i).getOrder());
                    mNotesPresenter.updateNotes(mNotes.get(i));
                }
                touchHelper.attachToRecyclerView(null);
            } else {
                touchHelper.attachToRecyclerView(mRecyclerView);
            }
            isSortable = !isSortable;
            mNotesAdapter.setSortable(isSortable);
            mNotesAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData(boolean isSavedNull) {
        onRefresh();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setToolbar(mToolbar);
        init();
    }

    private void init() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isSortable", isSortable);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        mNotesPresenter.loadNotes();
    }

    private void onLoadMore() {

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void refreshData(Object result) {
        if (result instanceof List) {
            mNotes.clear();
            mNotes.addAll((List<Notes>) result);
            mNotesAdapter.notifyDataSetChanged();
        }
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

    class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
        //保存被删除item信息，用于撤销操作
        BlockingQueue queue = new ArrayBlockingQueue(3);

        public SimpleItemTouchHelperCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (mNotes.isEmpty()) {
                return false;
            }
            //得到拖动ViewHolder的position
            int fromPosition = viewHolder.getAdapterPosition();
            //得到目标ViewHolder的position
            int toPosition = target.getAdapterPosition();
            if(toPosition==0){
                mNotes.get(fromPosition).setOrder(mNotes.get(toPosition).getOrder()+1);
            }else if(toPosition==mNotes.size()-1){
                mNotes.get(fromPosition).setOrder(mNotes.get(toPosition).getOrder()-1);
            }else if(fromPosition < toPosition){
                mNotes.get(fromPosition).setOrder((mNotes.get(toPosition).getOrder()+mNotes.get(toPosition+1).getOrder())/2);
            }else{
                mNotes.get(fromPosition).setOrder((mNotes.get(toPosition).getOrder()+mNotes.get(toPosition-1).getOrder())/2);
            }
            if (fromPosition < toPosition) {
                //分别把中间所有的item的位置重新交换
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mNotes, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mNotes, i, i - 1);
                }
            }
            mNotesAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (mNotes.isEmpty()) {
                return;
            }
            int position = viewHolder.getAdapterPosition();
            final Notes note = mNotes.get(position);
            note.setIndex(position);
            queue.add(note);
            mNotes.remove(position);
            mNotesAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (mNotes.isEmpty()) {
                return;
            }
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变Item的透明度
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void clearView(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            try {
                if (!queue.isEmpty()) {
                    Snackbar.make(mToolbar, R.string.delete_notes_success, Snackbar.LENGTH_LONG).setAction(R.string.repeal, v -> {
                        final Notes note = (Notes) queue.remove();
                        mNotes.add(note.getIndex(), note);
                        if (note.getIndex() == 0) {
                            mRecyclerView.smoothScrollToPosition(0);
                        }
                        mNotesAdapter.notifyDataSetChanged();
                    }).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {//回掉函数
                            super.onDismissed(snackbar, event);
                            if (event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                                final Notes note = (Notes) queue.remove();
                                mNotesPresenter.deleteNotes(note.getId() + "");
                            }
                        }
                    }).show();
                }
            }
            catch(Exception e){
                Log.d("xxx", "Exception = " + e);
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    }
}
