package org.imaginationforpeople.android2.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.ProjectViewAdapter;
import org.imaginationforpeople.android2.fragment.ProjectListFragment;
import org.imaginationforpeople.android2.handler.ProjectViewHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.sqlite.FavoriteSqlite;
import org.imaginationforpeople.android2.thread.ProjectViewThread;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class ProjectViewActivity extends FragmentActivity implements OnClickListener {
	public final static int USE_CAMERA = 0;
	public final static int SELECT_FILE = 1;

	private final static int CAPTURE_IMAGE_REQUEST_CODE = 1000;
	private final static int SELECT_FILE_REQUEST_CODE = 1001;
	private final static int UPLOAD_IMAGE_REQUEST_CODE = 1002;

	private boolean displayMenu = false;
	private boolean permitLoading = true;
	private boolean updateParent = false;
	private Intent shareIntent;
	private FavoriteSqlite db;
	private ProjectViewThread thread;
	private I4pProjectTranslation project;
	private Uri fileUri;

	@TargetApi(14)
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(displayMenu) {
			// Inflate menu only if it hasn't been done before
			if(menu.size() == 0) {
				// Inflating the menu
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.projectview, menu);

				// Creating share intent
				Intent prepareShareIntent = new Intent(Intent.ACTION_SEND);
				prepareShareIntent.putExtra(Intent.EXTRA_TEXT, UriHelper.getProjectUrl(project));
				prepareShareIntent.putExtra(Intent.EXTRA_SUBJECT, project.getTitle());
				prepareShareIntent.setType("text/plain");
				shareIntent = Intent.createChooser(prepareShareIntent, getResources().getText(R.string.projectview_menu_share_dialog));
			}

			// Defining favorite state
			MenuItem favoriteItem = menu.getItem(1);
			if(db.isFavorite(project))
				favoriteItem.setTitle(R.string.projectview_menu_favorites_remove);
			else
				favoriteItem.setTitle(R.string.projectview_menu_favorites_add);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if(getIntent().getData() != null) {
				Intent intent = new Intent(this, HomepageActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else
				finish();
			break;
		case R.id.projectview_addphoto:
			// Detecting if device has a camera
			if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.projectview_dialog_addphoto_title);
				builder.setItems(R.array.projectview_dialog_addphoto_items, this);
				builder.create().show();
			} else
				// We simulate a click on "by selecting a file"
				onClick(null, SELECT_FILE);
			break;
		case R.id.projectview_favorite:
			Toast t;
			if(db.isFavorite(project)) {
				db.removeFavorite(project);
				t = Toast.makeText(this, getResources().getString(R.string.projectview_toast_favorites_remove, project.getTitle()), Toast.LENGTH_SHORT);
			} else {
				db.addFavorite(project);
				t = Toast.makeText(this, getResources().getString(R.string.projectview_toast_favorites_add, project.getTitle()), Toast.LENGTH_SHORT);
			}
			t.show();
			break;
		case R.id.projectview_share:
			startActivity(shareIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Build.VERSION.SDK_INT < 11)
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		db = new FavoriteSqlite(this);

		if(Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);

		if(savedInstanceState != null && savedInstanceState.containsKey(DataHelper.PROJECT_VIEW_KEY)) {
			project = savedInstanceState.getParcelable(DataHelper.PROJECT_VIEW_KEY);
			displayProject();
		} else {
			loadProject();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(thread == null || !thread.isAlive())
			outState.putParcelable(DataHelper.PROJECT_VIEW_KEY, project);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		if(thread != null)
			thread.requestStop();
		super.onStop();
	}

	public void setProject(I4pProjectTranslation p) {
		project = p;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadProject() {
		if(permitLoading) {
			displayMenu = false;
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				invalidateOptionsMenu();

			setContentView(R.layout.loading);
			ProjectViewHandler handler = new ProjectViewHandler(this);

			Uri data = getIntent().getData();
			if(data != null) {
				List<String> path = data.getPathSegments();
				thread = new ProjectViewThread(handler, path.get(0), path.get(2));
			} else {
				Bundle extras = getIntent().getExtras();

				if(extras.containsKey("project_title"))
					setTitle(extras.getString("project_title"));

				if(extras.containsKey("project_id")) // Mostly used if we want a random project
					thread = new ProjectViewThread(handler, extras.getInt("project_id"));
				else
					thread = new ProjectViewThread(handler, extras.getString("project_lang"), extras.getString("project_slug"));
			}

			thread.start();
			permitLoading = false;
		}
	}

	@TargetApi(11)
	public void displayProject() {
		if(updateParent) {
			Intent resultData = new Intent();
			String slug = project.getSlug();
			resultData.putExtra(ProjectListFragment.PROJECT_RESULT_EXTRA_SLUG, slug);
			String url = project.getProject().getPictures().get(0).getThumbUrl();
			resultData.putExtra(ProjectListFragment.PROJECT_RESULT_EXTRA_IMG_URL, url);
			setResult(RESULT_OK, resultData);
			updateParent = false;
		}

		setContentView(R.layout.view_root);
		displayMenu = true;
		if(Build.VERSION.SDK_INT >= 11)
			invalidateOptionsMenu(); // Rebuild the menu

		setTitle(project.getTitle());

		ProjectViewAdapter adapter = new ProjectViewAdapter(getSupportFragmentManager(), project, getResources());

		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(adapter);

		PageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Intent intent;
		switch(which) {
		case USE_CAMERA:
			// We generate the name of the file that will be generated by camera
			File dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
			fileUri = Uri.parse("file://" + dirName.getPath() + "/i4p_" + project.getSlug() + "_" + timeStamp + ".jpg");

			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
			break;
		case SELECT_FILE:
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_FILE_REQUEST_CODE);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case CAPTURE_IMAGE_REQUEST_CODE:
			case SELECT_FILE_REQUEST_CODE:
				Intent intent = new Intent(this, UploadPhotoActivity.class);
				switch(requestCode) {
				case CAPTURE_IMAGE_REQUEST_CODE:
					intent.setData(fileUri);
					break;
				case SELECT_FILE_REQUEST_CODE:
					intent.setData(data.getData());
					break;
				}
				intent.putExtra(UploadPhotoActivity.PROJECT_NAME, project.getTitle());
				intent.putExtra(UploadPhotoActivity.PROJECT_LANG, project.getLanguageCode());
				intent.putExtra(UploadPhotoActivity.PROJECT_SLUG, project.getSlug());
				startActivityForResult(intent, UPLOAD_IMAGE_REQUEST_CODE);
				break;
			case UPLOAD_IMAGE_REQUEST_CODE:
				permitLoading = true;
				updateParent = true;
				loadProject();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
