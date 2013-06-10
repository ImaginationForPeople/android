package org.imaginationforpeople.android2.activity;

import java.util.Locale;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.helper.DisplayHelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class UploadPhotoActivity extends Activity {
	public final static String PROJECT_LANG = "project_lang";
	public final static String PROJECT_SLUG = "project_slug";
	public final static String PROJECT_NAME = "project_name";
	
	private final static String AUTHOR_NAME = "author_name";
	private final static String SOURCE_NAME = "source_name";
	
	private SharedPreferences preferences;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data = getIntent().getExtras();

		setContentView(R.layout.uploadphoto);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);

		TextView project = (TextView) findViewById(R.id.uploadphoto_project);
		project.setText(data.getString(PROJECT_NAME));

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(getIntent().getData().getPath(), options);
		final int reqSize = DisplayHelper.dpToPx(65);
		int inSampleSize = 1;
		if(options.outHeight > reqSize || options.outWidth > reqSize) {
			int heightRatio = Math.round((float) options.outHeight / (float) reqSize);
			int widthRatio = Math.round((float) options.outWidth / (float) reqSize);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap photo = BitmapFactory.decodeFile(getIntent().getData().getPath(), options);
		ImageView thumb = (ImageView) findViewById(R.id.uploadphoto_thumb);
		thumb.setImageBitmap(photo);

		Spinner license = (Spinner) findViewById(R.id.uploadphoto_license);
		license.setSelection(1);
		
		preferences = getPreferences(Context.MODE_PRIVATE);
		if(preferences.contains(AUTHOR_NAME)) {
			EditText author = (EditText) findViewById(R.id.uploadphoto_author);
			author.setText(preferences.getString(AUTHOR_NAME, ""));
		}

		String sourceText;
		if(preferences.contains(SOURCE_NAME)) {
			sourceText = preferences.getString(SOURCE_NAME, "");
		} else {
			String manufacturer = Build.MANUFACTURER.toUpperCase(Locale.US);
			String model = Build.MODEL;
			sourceText = model.startsWith(manufacturer) ?
					model
				  : manufacturer + " " + model;
		}
		EditText source = (EditText) findViewById(R.id.uploadphoto_source);
		source.setText(sourceText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.uploadphoto, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.uploadphoto_menu_send:
			EditText author = (EditText) findViewById(R.id.uploadphoto_author);
			EditText source = (EditText) findViewById(R.id.uploadphoto_source);
			Editor edit = preferences.edit();
			edit.putString(AUTHOR_NAME, author.getText().toString().trim());
			edit.putString(SOURCE_NAME, source.getText().toString().trim());
			edit.commit();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
