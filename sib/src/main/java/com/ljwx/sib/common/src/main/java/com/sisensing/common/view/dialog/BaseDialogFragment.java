package com.sisensing.common.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.ToastUtils;
import com.sisensing.common.R;


public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = "base_bottom_dialog";
    private static final float DEFAULT_DIM = 0.2f;
    private static Dialog dialog;
    private Local local = Local.BOTTOM;

    public enum Local {
        TOP, CENTER, BOTTOM
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (local == Local.BOTTOM) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
        } else if (local == Local.CENTER || local == Local.TOP) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CenterDialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialog = getDialog();
        if (dialog != null) {
            if (dialog.getWindow() != null) {
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            dialog.setCanceledOnTouchOutside(isCancel());
            dialog.setCancelable(isCancel());
        }
        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
    }

    /**
     * 获取布局资源文件
     *
     * @return 布局资源文件id值
     */
    @LayoutRes
    public abstract int getLayoutRes();

    /**
     * 绑定
     *
     * @param v view
     */
    public abstract void bindView(View v);

    /**
     * 设置是否可以cancel
     *
     * @return
     */
    protected abstract boolean isCancel();

    /**
     * 开始展示
     */
    @Override
    public void onStart() {
        super.onStart();
        if (dialog == null) {
            dialog = getDialog();
        }
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = getDimAmount();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            if (getHeight() > 0) {
                params.height = getHeight();
            } else {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            if (local == Local.TOP) {
                params.gravity = Gravity.TOP;
            } else if (local == Local.CENTER) {
                params.gravity = Gravity.CENTER;
            } else {
                params.gravity = Gravity.BOTTOM;
            }
            window.setAttributes(params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.listener();
        }
    }

    /**
     * 获取弹窗高度
     *
     * @return int类型值
     */
    public int getHeight() {
        return -1;
    }

    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            show(fragmentManager, getFragmentTag());
        } else {
            Log.e("Fragment错误", "需要设置setFragmentManager");
        }
    }

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public onLoadFinishListener mListener;

    public void setLoadFinishListener(onLoadFinishListener listener) {
        mListener = listener;
    }

    public interface onLoadFinishListener {
        void listener();
    }

}
