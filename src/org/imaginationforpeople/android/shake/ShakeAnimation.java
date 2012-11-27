package org.imaginationforpeople.android.shake;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ShakeAnimation {
	public static final int ANIMATION_DURATION = 3000;
	
	private Context context;
	private View mShakeView;
	private ViewPropertyAnimator mShakeAnimator;
	private Handler mHideHandler = new Handler();
	
	private AnimationListener mAnimationListener;
	
	public interface AnimationListener {
		void onClick();
	}
	
	private void defaultConstructor(View v, AnimationListener a) {
		mShakeView = v;
		mAnimationListener = a;
		
		mShakeView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				hideAnimation();
				mAnimationListener.onClick();
			}
		});
	}
	
	public ShakeAnimation(Context c, View v, AnimationListener a) {
		defaultConstructor(v, a);
		context = c;
	}
	
	@TargetApi(12)
	public ShakeAnimation(View v, AnimationListener a) {
		defaultConstructor(v, a);
		mShakeAnimator = mShakeView.animate();
	}
	
	@TargetApi(14)
	public void showAnimation() {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, ANIMATION_DURATION);
		
		mShakeView.setVisibility(View.VISIBLE);
		if(Build.VERSION.SDK_INT >= 14)
			mShakeAnimator.cancel();
		if(Build.VERSION.SDK_INT >= 12)
			mShakeAnimator.alpha(1)
						.setDuration(mShakeView.getResources().getInteger(android.R.integer.config_shortAnimTime))
						.setListener(null);
		else {
			Animation fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
			mShakeView.startAnimation(fadeIn);
		}
	}
	
	@TargetApi(14)
	public void hideAnimation() {
		if(Build.VERSION.SDK_INT >= 14)
			mShakeAnimator.cancel();
		if(Build.VERSION.SDK_INT >= 12)
			mShakeAnimator.alpha(0)
						.setDuration(mShakeView.getResources().getInteger(android.R.integer.config_shortAnimTime))
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mShakeView.setVisibility(View.GONE);
							}
						});
		else {
			Animation fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
			fadeOut.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {
					mShakeView.setVisibility(View.GONE);
				}
			});
			mShakeView.startAnimation(fadeOut);
		}
	}
	
	private Runnable mHideRunnable = new Runnable() {
		public void run() {
			hideAnimation();
		}
	};
}
