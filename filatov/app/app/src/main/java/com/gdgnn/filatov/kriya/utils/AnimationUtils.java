package com.gdgnn.filatov.kriya.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

public class AnimationUtils {

    public static Animation createAlpha(float fromAlpha, float toAlpha, long duration) {
        Animation animation = new AlphaAnimation(fromAlpha, toAlpha);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(duration);

        return animation;
    }
}
