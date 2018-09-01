/**
 * Copyright 2015 Cisco. All rights reserved.
 * OutlineUtils.java
 */
package com.lskycity.support.utils;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 *@author liuzhaofeng
 *@since Mar 9, 2015
 */
public class ViewOutlineUtils {
    private static float avatar_radius_Rate = 7f / 80f;

    @SuppressLint("NewApi")
    private static final ViewOutlineProvider AVATAR_OUTLINE_PROVIDER = new RoundRectOutlineProviderByRate(avatar_radius_Rate);

    @SuppressLint("NewApi")
    public static class RoundRectOutlineProvider extends ViewOutlineProvider {

        private float mRadius;

        public RoundRectOutlineProvider(float radius) {
            super();
            mRadius = radius;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mRadius);
        }

    }

    @SuppressLint("NewApi")
    public static class RoundRectOutlineProviderByRate extends ViewOutlineProvider {

        private float mRadiusRate;

        public RoundRectOutlineProviderByRate(float radiusRate) {
            super();
            mRadiusRate = radiusRate;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() * mRadiusRate);
        }

    }

    @SuppressLint("NewApi")
    public static class OvalOutlineProvider extends ViewOutlineProvider {

        public OvalOutlineProvider() {
            super();
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }

    }


}
