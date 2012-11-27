package org.imaginationforpeople.android.shake;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Handler;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class ShakeAnimation {
	public static final int ANIMATION_DURATION = 3000;
	
	private View mShakeView;
	private ViewPropertyAnimator mShakeAnimator;
	private Handler mHideHandler = new Handler();
	
	private AnimationListener mAnimationListener;
	
	public interface AnimationListener {
		void onClick();
	}
	
	@TargetApi(12)
	public ShakeAnimation(View v, AnimationListener a) {
		mShakeView = v;
		mShakeAnimator = mShakeView.animate();
		mAnimationListener = a;
		
		mShakeView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				hideAnimation();
				mAnimationListener.onClick();
			}
		});
	}
	
	@TargetApi(14)
	public void showAnimation() {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, ANIMATION_DURATION);
		
		mShakeView.setVisibility(View.VISIBLE);
		mShakeAnimator.cancel();
		mShakeAnimator.alpha(1)
					.setDuration(mShakeView.getResources().getInteger(android.R.integer.config_shortAnimTime))
					.setListener(null);
	}
	
	@TargetApi(14)
	public void hideAnimation() {
		mShakeAnimator.cancel();
		mShakeAnimator.alpha(0)
					.setDuration(mShakeView.getResources().getInteger(android.R.integer.config_shortAnimTime))
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mShakeView.setVisibility(View.GONE);
						}
					});
	}
	
	private Runnable mHideRunnable = new Runnable() {
		public void run() {
			hideAnimation();
		}
	};
}
