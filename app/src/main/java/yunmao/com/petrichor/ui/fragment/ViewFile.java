package yunmao.com.petrichor.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import yunmao.com.petrichor.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;



public class ViewFile extends Activity {
	
	private String fileNameString;
	private String bookname;
	private static final String gb2312 = "GB2312";
	private static final String utf8 = "UTF-8";
	private static final String defaultCode = gb2312;
	private TextView viewBookName;
	private FloatingActionButton actionButton;
	private MydatabaseHelper mydatabaseHelper;
	private Boolean isSave=false;
	public static String NAME[]=new String[100];
	public static String URL;
	private int i=0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_view);
		final ListAllFileActivity listAllFileActivity = new ListAllFileActivity();
		viewBookName = (TextView) findViewById(R.id.viewBookName);
//		FloatingActionButton addFileButton = (FloatingActionButton) findViewById(R.id.viewFab);



//		addFileButton.setOnClickListener(new collectBookName());

		try {
			Bundle bunde = this.getIntent().getExtras();
			fileNameString = bunde.getString("fileName");
			bookname = bunde.getString("bookname");
			viewBookName.setText(bookname);
			reCodeAndShow(defaultCode);
		} catch (Exception e) {}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.gb2312:
			reCodeAndShow(gb2312);
			break;
		case R.id.utf8:
			reCodeAndShow(utf8);
			break;
		case R.id.about:
			doAbout();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void reCodeAndShow(String code)
	{
		TextView tv = (TextView) findViewById(R.id.bookContent);
		String fileString = getStringFromFile(code);
		tv.setText(fileString);
	}
	
	public String getStringFromFile(String code)
	{
		try {
			StringBuffer sBuffer = new StringBuffer();
			FileInputStream fInputStream = new FileInputStream(fileNameString);
			
			InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, code);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if(!new File(fileNameString).exists())
			{
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			fInputStream.close();
			inputStreamReader.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void doAbout() {
		
		Dialog dialog = new AlertDialog.Builder(ViewFile.this)
		.setTitle(R.string.aboutTitle)
		.setMessage(R.string.aboutInfo)
		.setPositiveButton(R.string.aboutOK,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialoginterface, int i) {}
						} )
		.create();
		
		dialog.show();
	}
	class OpenFileAction implements View.OnClickListener
	{
        private View view;
		public void onClick(View v) {
            view = View.inflate(ViewFile.this,R.layout.home_content_frag,null);
			Intent intentMain = new Intent(view.getContext(), ListAllFileActivity.class);
			startActivityForResult(intentMain, 0);
		}
	}

	class collectBookName implements View.OnClickListener
	{
		public void onClick(View v) {
			mydatabaseHelper = new MydatabaseHelper(ViewFile.this,"book.db",null,1);
			mydatabaseHelper.getWritableDatabase();
			final SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();

			Cursor cursor = db.query("collect", null, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					String buf= cursor.getString((cursor.getColumnIndex("bookname")));
					if (buf.equals(viewBookName.getText().toString())){
						isSave=true;
						break;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();

			if (isSave){
				db.delete("collect","bookname=?", new String[]{viewBookName.getText().toString()});
				Toast.makeText(ViewFile.this,"取消成功",Toast.LENGTH_SHORT).show();
				isSave = false;

			}else {

				Cursor cs = db.query("localbook", null, null, null, null, null, null);
				if (cs.moveToFirst()) {
					do {
						String buf= cs.getString((cs.getColumnIndex("bookname")));
						if (buf.equals(viewBookName.getText().toString())){
							URL = cs.getString((cs.getColumnIndex("bookurl")));
							break;
						}
					} while (cs.moveToNext());
				}
				cs.close();


				ContentValues values = new ContentValues();
				values.put("bookname",viewBookName.getText().toString());
				values.put("bookurl",URL);
				db.insert("collect",null,values);
				Toast.makeText(ViewFile.this,"收藏成功",Toast.LENGTH_SHORT).show();

			}




		}
	}
	
}
