package org.imaginationforpeople.android2.projectview;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.helper.DisplayHelper;
import org.imaginationforpeople.android2.model.Picture;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class FullImageProjectViewFragment extends Fragment {
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Picture picture = getArguments().getParcelable("picture");
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.projectview_gallery_full_fragment, null);
		
		ImageView image = (ImageView) layout.findViewById(R.id.projectview_gallery_fullimage);
		image.setImageBitmap(picture.getImageBitmap());
		
		int data = 4;
		
		TextView date = (TextView) layout.findViewById(R.id.projectview_gallery_slider_content_date);
		if("".equals(picture.getCreated())) {
			date.setVisibility(View.GONE);
			data--;
		} else {
			date.setText(getString(R.string.projectview_gallery_slider_content_date, picture.getCreated()));
		}
		
		TextView description = (TextView) layout.findViewById(R.id.projectview_gallery_slider_content_description);
		if("".equals(picture.getDesc())) {
			description.setVisibility(View.GONE);
			data--;
		} else {
			description.setText(getString(R.string.projectview_gallery_slider_content_description, picture.getDesc()));
		}
		
		TextView author = (TextView) layout.findViewById(R.id.projectview_gallery_slider_content_author);
		if("".equals(picture.getAuthor())) {
			author.setVisibility(View.GONE);
			data--;
		} else {
			author.setText(getString(R.string.projectview_gallery_slider_content_author, picture.getAuthor()));
		}
		
		TextView source = (TextView) layout.findViewById(R.id.projectview_gallery_slider_content_source);
		if("".equals(picture.getSource())) {
			source.setVisibility(View.GONE);
			data--;
		} else {
			source.setText(getString(R.string.projectview_gallery_slider_content_source, picture.getSource()));
		}
		
		SlidingDrawer drawer = (SlidingDrawer) layout.findViewById(R.id.projectview_gallery_slider);
		if(data == 0) {
			drawer.setVisibility(View.GONE);
		} else {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayHelper.dpToPx(44 + data * 21));
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			drawer.setLayoutParams(params);
		}
		
		return layout;
	}
}
