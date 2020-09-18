package com.deepsea.mua.stub.view;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by JUN on 2019/8/26
 */

public class GradientTextView extends TextView {
        public GradientTextView(Context context) {
            super(context);
        }

        public GradientTextView(Context context,
                                AttributeSet attrs) {
            super(context, attrs);
        }

        public GradientTextView(Context context,
                                AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onLayout(boolean changed,
                                int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            if (changed) {
                getPaint().setShader(new LinearGradient(
                        0, 0, getWidth(), 0,
                        StringToColor("#CE54C8"), StringToColor("#9344E5"),
                        Shader.TileMode.CLAMP));
            }
        }

        /**
         * #颜色转16进制颜色
         * @param str {String} 颜色
         * @return
         */
        private  int StringToColor(String str) {
            return 0xff000000 | Integer.parseInt(str.substring(2), 16);
        }
    }


