package yunmao.com.petrichor.holder;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import yunmao.com.petrichor.R;

/**
 * Created by msi on 2018/3/5.
 */

public class NotesEditorHolder {  private Context mContext;
    private View mContentView;
    private TextInputLayout til_notes;
    public EditText et_notes_name;
    private EditText et_notes_paper;
    private EditText et_notes_note;
    private EditText et_notes_page;

    public NotesEditorHolder(Context context) {
        this(context, "", "","",0);
    }

    public NotesEditorHolder(Context context, String name, String paper,String note,int page) {
        mContext = context;
        initView(name, paper,note,page);
        initEvent();
    }

    private void initView(String name, String paper,String note,int page) {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.item_add_note, null);
        til_notes = (TextInputLayout) mContentView.findViewById(R.id.til_notes);
        et_notes_name = (EditText) mContentView.findViewById(R.id.et_notes_name);
        et_notes_paper = (EditText) mContentView.findViewById(R.id.et_notes_paper);
        et_notes_note = (EditText) mContentView.findViewById(R.id.et_notes_note);
        et_notes_page = (EditText) mContentView.findViewById(R.id.et_notes_page);
        et_notes_name.setText(name);
        et_notes_paper.setText(paper);
        et_notes_note.setText(note);
        et_notes_page.setText(page+"");
    }

    private void initEvent() {
    }

    public View getContentView() {
        return mContentView;
    }

    public boolean check() {
        if (TextUtils.isEmpty(et_notes_name.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    public String getName() {
        return et_notes_name.getText().toString();
    }

    public String getPaper() {
        return et_notes_paper.getText().toString();
    }
    public String getNote() {
        return et_notes_note.getText().toString();
    }
    public int getPage() {
        String temp=et_notes_page.getText().toString();
        int a;
        try {
            a = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            a=0;
        }
        return a;
    }
}
