package com.lskycity.support.utils;

import android.text.SpannableString;
import android.text.TextUtils;

/**
 * Created by zhaofliu on 3/25/17.
 */

public class SpanUtils {

    public static CharSequence getSpanString(String text, String query, Object span) {
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(query)) {
            final int index = text.indexOf(query);
            if (index > -1) {
                final SpannableString newText = new SpannableString(text);
                newText.setSpan(span, index, index + query.length(), 0);
                return newText;
            }
        }
        return text;
    }

}
