package org.imaginationforpeople.android2.activity;

import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.Locale;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.handler.ProjectsUploadPhotoHandler;
import org.imaginationforpeople.android2.helper.DisplayHelper;
import org.imaginationforpeople.android2.model.Picture;
import org.imaginationforpeople.android2.thread.ProjectsUploadImageThread;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class UploadPhotoActivity extends SherlockActivity implements OnClickListener {
	public final static String PROJECT_LANG = "project_lang";
	public final static String PROJECT_SLUG = "project_slug";
	public final static String PROJECT_NAME = "project_name";

	private final static String AUTHOR_NAME = "author_name";
	private final static String SOURCE_NAME = "source_name";

	private SharedPreferences preferences;
	private boolean blockBack = false;
	private static ProjectsUploadImageThread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Uri intentData = getIntent().getData();

		if(thread != null && thread.isAlive()) {
			ProjectsUploadPhotoHandler handler = new ProjectsUploadPhotoHandler(this);
			thread.setHandler(handler);
			blockBack = true;

			setContentView(R.layout.uploadphoto);
			final RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.uploadphoto_view_loading);
			final ScrollView contentLayout = (ScrollView) findViewById(R.id.uploadphoto_view_form);
			loadingLayout.setVisibility(View.VISIBLE);
			contentLayout.setVisibility(View.GONE);
		} else {
			if(!intentData.getScheme().equalsIgnoreCase("file")
					&& !intentData.getScheme().equalsIgnoreCase("content")) {
				setContentView(R.layout.error_white);
				TextView error1 = (TextView) findViewById(R.id.error_text1);
				TextView error2 = (TextView) findViewById(R.id.error_text2);
				error1.setText(R.string.uploadphoto_error_unsupported_application_1);
				error2.setText(R.string.uploadphoto_error_unsupported_application_2);

				Log.e("error", "Unsupported scheme sent to activity");
			} else {
				Bitmap photo = null;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				try {
					AssetFileDescriptor fd = getContentResolver().openAssetFileDescriptor(intentData, "r");
					BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, options);
					calculateInSampleSize(options);
					photo = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, options);
				} catch (FileNotFoundException e) {
					Log.w("warn", "File not found when trying to open content: " + intentData.toString());
					e.printStackTrace();
				}

				if(photo != null) {
					Bundle data = getIntent().getExtras();
					setContentView(R.layout.uploadphoto);

					TextView project = (TextView) findViewById(R.id.uploadphoto_project);
					project.setText(data.getString(PROJECT_NAME));

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

					EditText description = (EditText) findViewById(R.id.uploadphoto_description);
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(description, 0);
				} else {
					setContentView(R.layout.error_white);

					TextView message1 = (TextView) findViewById(R.id.error_text1);
					message1.setText(R.string.uploadphoto_error_unknown_image_1);

					TextView message2 = (TextView) findViewById(R.id.error_text2);
					message2.setText(R.string.uploadphoto_error_unknown_image_2);
				}
			}
		}
	}

	private void calculateInSampleSize(BitmapFactory.Options options) {
		final int reqSize = DisplayHelper.dpToPx(65);
		int inSampleSize = 1;
		if(options.outHeight > reqSize || options.outWidth > reqSize) {
			int heightRatio = Math.round((float) options.outHeight / (float) reqSize);
			int widthRatio = Math.round((float) options.outWidth / (float) reqSize);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(!blockBack) {
			if(menu.size() == 0)
				getSupportMenuInflater().inflate(R.menu.uploadphoto, menu);

			return true;
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			doBackFunction();
			break;
		case R.id.uploadphoto_menu_send:
			blockBack = true;

			EditText author = (EditText) findViewById(R.id.uploadphoto_author);
			EditText source = (EditText) findViewById(R.id.uploadphoto_source);
			Editor edit = preferences.edit();
			edit.putString(AUTHOR_NAME, author.getText().toString().trim());
			edit.putString(SOURCE_NAME, source.getText().toString().trim());
			edit.commit();

			final RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.uploadphoto_view_loading);
			final ScrollView contentLayout = (ScrollView) findViewById(R.id.uploadphoto_view_form);

			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

			supportInvalidateOptionsMenu();

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
				loadingLayout.setVisibility(View.VISIBLE);
				loadingLayout.setAlpha(0);
				loadingLayout.animate().setDuration(1000).alpha(1);

				contentLayout.animate().setDuration(1000)
				.alpha(0)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						contentLayout.setVisibility(View.GONE);
					}
				});
			} else {
				loadingLayout.setVisibility(View.VISIBLE);
				contentLayout.setVisibility(View.GONE);
			}

			String[] licenses = getResources().getStringArray(R.array.uploadphoto_license_ids);

			EditText description = (EditText) findViewById(R.id.uploadphoto_description);
			Spinner license = (Spinner) findViewById(R.id.uploadphoto_license);

			Picture picture = new Picture();
			picture.setDesc(description.getText().toString().trim());
			picture.setLicense(String.valueOf(licenses[(int) license.getSelectedItemId()]));
			picture.setAuthor(author.getText().toString().trim());
			picture.setSource(source.getText().toString().trim());

			try {
				Uri intentData = getIntent().getData();

				AssetFileDescriptor asset = getContentResolver().openAssetFileDescriptor(intentData, "r");

				String mime = null;
				if(intentData.getScheme().equalsIgnoreCase("file")) {
					mime = URLConnection.guessContentTypeFromName(intentData.toString());
				} else if(intentData.getScheme().equalsIgnoreCase("content")) {
					mime = getContentResolver().getType(intentData);
				}

				Bundle extras = getIntent().getExtras();
				String slug = extras.getString(PROJECT_SLUG);
				String lang = extras.getString(PROJECT_LANG);

				ProjectsUploadPhotoHandler handler = new ProjectsUploadPhotoHandler(this);
				thread = new ProjectsUploadImageThread(handler, picture, mime, asset, slug, lang);
				thread.start();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		doBackFunction();
	}

	private void doBackFunction() {
		if(blockBack) {
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle(R.string.uploadphoto_cancel_title);
			dialog.setMessage(getResources().getString(R.string.uploadphoto_cancel_message));
			dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel), this);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(android.R.string.ok), this);
			dialog.show();
		} else {
			if(thread != null && thread.isAlive())
				thread.requestStop();
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			if(thread != null)
				thread.requestStop();
			finish();
			break;
		}
	}

	@Override
	protected void onStop() {
		if(isFinishing() && thread != null && thread.isAlive()) {
			thread.requestStop();
		}
		super.onStop();
	}

	public void freeBackButton() {
		blockBack = false;
	}
}
