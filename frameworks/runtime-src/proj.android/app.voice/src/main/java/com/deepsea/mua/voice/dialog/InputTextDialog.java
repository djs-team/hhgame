package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.KeyboardUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogInputTextBinding;

/**
 * Created by JUN on 2019/3/25
 * 直播间文字输入弹框
 */
public class InputTextDialog extends BaseDialog<DialogInputTextBinding> {

    public interface OnTextSendListener {
        void onTextSend(String msg);
    }

    private int mLastDiff = 0;
    private OnTextSendListener mOnTextSendListener;

    public InputTextDialog(Context context) {
        super(context);
        Window window = getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        initEditorAction();
        addOnLayoutChangeListener();
    }

    @Override
    protected float getDimAmount() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_input_text;
    }

    @Override
    protected void initListener() {
        mBinding.rootView.setOnClickListener(v -> dismiss());
    }

    public void setMsg(String initMsg) {
        mBinding.inputEdit.setText(initMsg);
    }

    private void initEditorAction() {
        mBinding.inputEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMsg();
                return true;
            }
            return false;
        });
        ViewBindUtils.RxClicks(mBinding.ivChatSend, o -> {
            sendMsg();
        });
    }

    private void sendMsg() {
        String input = mBinding.inputEdit.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            if (mOnTextSendListener != null) {
                mOnTextSendListener.onTextSend(input);
            }
        }
        mBinding.inputEdit.setText("");
        dismiss();
    }

    private void addOnLayoutChangeListener() {
        mBinding.rootView.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
            Rect r = new Rect();
            mBinding.rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int heightDiff = screenHeight - r.bottom;

            if (heightDiff <= 0 && mLastDiff > 0) {
                dismiss();
            }
            mLastDiff = heightDiff;
        });
    }

    public void setOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void show() {
        super.show();
        KeyboardUtils.showSoftInput(mBinding.inputEdit);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
    }
}
