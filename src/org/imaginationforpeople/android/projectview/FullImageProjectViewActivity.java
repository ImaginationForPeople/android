package org.imaginationforpeople.android.projectview;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.helper.DisplayHelper;
import org.imaginationforpeople.android.model.Picture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class FullImageProjectViewActivity extends Activity {
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.projectview_gallery_full);
		
		if(Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		Picture picture = extras.getParcelable("image");
		
		ImageView image = (ImageView) findViewById(R.id.projectview_gallery_fullimage);
		image.setImageBitmap(picture.getImageBitmap());
		
		int data = 4;
		
		TextView date = (TextView) findViewById(R.id.projectview_gallery_slider_content_date);
		if("".equals(picture.getCreated())) {
			date.setVisibility(View.GONE);
			data--;
		} else {
			date.setText(getString(R.string.projectview_gallery_slider_content_date, picture.getCreated()));
		}
		
		TextView description = (TextView) findViewById(R.id.projectview_gallery_slider_content_description);
		if("".equals(picture.getDesc())) {
			description.setVisibility(View.GONE);
			data--;
		} else {
			description.setText(getString(R.string.projectview_gallery_slider_content_description, picture.getDesc()));
		}
		
		TextView author = (TextView) findViewById(R.id.projectview_gallery_slider_content_author);
		if("".equals(picture.getAuthor())) {
			author.setVisibility(View.GONE);
			data--;
		} else {
			author.setText(getString(R.string.projectview_gallery_slider_content_author, picture.getAuthor()));
		}
		
		TextView source = (TextView) findViewById(R.id.projectview_gallery_slider_content_source);
		if("".equals(picture.getSource())) {
			source.setVisibility(View.GONE);
			data--;
		} else {
			source.setText(getString(R.string.projectview_gallery_slider_content_source, picture.getSource()));
		}
		
		SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.projectview_gallery_slider);
		if(data == 0) {
			drawer.setVisibility(View.GONE);
		} else {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayHelper.dpToPx(44 + data * 21));
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			drawer.setLayoutParams(params);
		}
		
		super.onCreate(savedInstanceState);
	}
}
