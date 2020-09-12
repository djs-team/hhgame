package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogAbsBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/4/13
 */
public class AbsDialog extends BaseDialog<DialogAbsBinding> {

    public interface OnClickListener {
        void onClick(View view, AbsDialog dialog);
    }

    private int mTextColor = 0xFF666666;
    private final int mTextSize = 16;
    private final int mBodyHeight = 45;
    private int mLineColor = 0xFFEEEEEE;
    private final float mLineHeight = 0.5F;

    public AbsDialog(@NonNull Context context) {
        super(context);
    }

    public AbsDialog(@NonNull Context context, int textColor) {
        super(context);
        this.mTextColor = textColor;
    }

    public AbsDialog(@NonNull Context context, int textColor, int lineColor) {
        super(context);
        this.mTextColor = textColor;
        this.mLineColor = lineColor;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_abs;
    }

    @Override
    protected float getDimAmount() {
        return 0.5F;
    }

    public void setBackgroundRes(int res) {
        mBinding.container.setBackgroundResource(res);
    }

    public void removeAllBody() {
        mBinding.container.removeAllViews();
    }

    public AbsDialog addBody(String text, OnClickListener listener) {
        addBody(text, mTextColor, mTextSize, mBodyHeight, listener);
        return withLine(mLineColor, mLineHeight);
    }

    public AbsDialog addBodyNoLine(String text, OnClickListener listener) {
        return addBody(text, mTextColor, mTextSize, mBodyHeight, listener);
    }

    /**
     * 添加item
     *
     * @param text
     * @param lineHeight dp
     * @param listener
     * @return
     */
    public AbsDialog addBody(String text, int lineHeight, OnClickListener listener) {
        addBodyNoLine(text, listener);
        return withLine(mLineColor, lineHeight);
    }

    public AbsDialog addBody(String text, int lineColor, int lineHeight, OnClickListener listener) {
        addBodyNoLine(text, listener);
        return withLine(lineColor, lineHeight);
    }

    public AbsDialog addBody(int textColor, String text, OnClickListener listener) {
        return addBody(text, textColor, mTextSize, mBodyHeight, listener);
    }

    /**
     * 添加item
     *
     * @param text
     * @param textColor
     * @param textSize
     * @param height
     * @param listener
     * @return
     */
    public AbsDialog addBody(String text, int textColor, int textSize, int height, OnClickListener listener) {
        TextView body = new TextView(mContext);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ResUtils.dp2px(mContext, height));
        body.setLayoutParams(lp);
        body.setText(text);
        body.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        body.setTextColor(textColor);
        body.setGravity(Gravity.CENTER);
        body.setBackgroundColor(0xFFFFFFFF);
        ViewBindUtils.RxClicks(body, o -> {
            if (listener != null) {
                listener.onClick(body, this);
            }
            dismiss();
        });
        mBinding.container.addView(body);
        return this;
    }

    public AbsDialog withLine() {
        return withLine(mLineColor, mLineHeight);
    }

    public AbsDialog withLine(int lineHeight) {
        return withLine(mLineColor, lineHeight);
    }

    public AbsDialog withLine(int lineColor, float lineHeight) {
        View line = new View(mContext);
        int height = Math.max(ResUtils.dp2px(mContext, lineHeight), 1);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        line.setLayoutParams(lp);
        line.setBackgroundColor(lineColor);
        mBinding.container.addView(line);
        return this;
    }

}
