package com.ljwx.basedialog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.ljwx.basedialog.R;

public class CustomDialog extends Dialog {

    LinearLayoutCompat vRoot;

    Animation mAnimationEnter;
    Animation mAnimationExit;
    boolean mIsCanceledOnTouchOutside = false;

    public CustomDialog(Context context) {
        this(context, 0);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setWindowAnimations(0);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        vRoot = new LinearLayoutCompat(getContext());
        vRoot.setOrientation(LinearLayoutCompat.VERTICAL);
    }

    public void setWidthMatch() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.getDecorView().setLayoutParams(params);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setWidthHeightMatch() {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.getDecorView().setLayoutParams(params);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(attributes);
        }
    }

    public CustomDialog setDimAmount(float amount) {
        if (amount > 0) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setDimAmount(amount);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setDimAmount(0);
        }
        return this;
    }

    public CustomDialog setAnimation(Animation enter, Animation exit) {
        mAnimationEnter = enter;
        mAnimationExit = exit;
        return this;
    }

    public CustomDialog setView(View view) {
        setContentView(view);
        return this;
    }

    public CustomDialog setView(@LayoutRes int resId) {
        setContentView(resId);
        return this;
    }


    public CustomDialog setActions(View.OnClickListener listener, int... ids) {
        for (int id : ids) {
            View v = vRoot.findViewById(id);
            if (v != null) {
                v.setOnClickListener(listener);
            }
        }
        return this;
    }

    public CustomDialog setDismissActions(OnClickListener listener, int... ids) {
        setActions(new DismissAction(this, listener), ids);
        return this;
    }

    public CustomDialog useDefaultActions() {
        setActions(new DefaultAction(this), new int[]{R.id.btn_cancel, R.id.btn_close});
        return this;
    }

    public CustomDialog showIt() {
        show();
        return this;
    }

    public void dismissImmediately() {
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(vRoot, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        mIsCanceledOnTouchOutside = cancel;
    }

    @Override
    public void setContentView(View view) {
        vRoot.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        vRoot.addView(view, params);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, vRoot, false));
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (vRoot.getChildCount() == 0 || mAnimationEnter == null) {

        } else if (!isAnimating(mAnimationEnter)) {
            mAnimationEnter.reset();
            vRoot.startAnimation(mAnimationEnter);
        }
    }

    @Override
    public void dismiss() {
        if (vRoot.getChildCount() == 0 || mAnimationExit == null) {
            dismissImmediately();
        } else if (!isAnimating(mAnimationExit)) {
            mAnimationExit.reset();
            mAnimationExit.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dismissImmediately();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            vRoot.startAnimation(mAnimationExit);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAnimating(mAnimationEnter) || isAnimating(mAnimationExit)) {
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Rect rect = new Rect();
            vRoot.getHitRect(rect);
            if (!rect.contains((int) ev.getX(), (int) ev.getY())) {

                if (mIsCanceledOnTouchOutside) {
                    dismiss();
                    return true;
                }

            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onBackPressed() {
        if (isAnimating(mAnimationEnter) || isAnimating(mAnimationExit)) {
            return;
        }
        super.onBackPressed();
    }

    protected static Animation translate(float from, float to) {
        final int type = Animation.RELATIVE_TO_SELF;
        Animation animation = new TranslateAnimation(0, 0, 0, 0, type, from, type, to);
        animation.setDuration(250);
        return animation;
    }


    boolean isAnimating(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }

    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    static class DefaultAction implements View.OnClickListener {
        final Dialog dialog;

        public DefaultAction(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btn_cancel) {
                dialog.cancel();
            } else if (v.getId() == R.id.btn_close) {
                dialog.dismiss();
            }
        }
    }

    static class DismissAction implements View.OnClickListener {
        final Dialog dialog;
        final OnClickListener listener;

        public DismissAction(Dialog dialog, OnClickListener listener) {
            this.dialog = dialog;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(dialog, v.getId());
            }
            dialog.dismiss();
        }
    }


    public static class Builder {
        private final @NonNull
        Context mContext;
        private final @StyleRes
        int mTheme;

        private View mView;

        public Builder(@NonNull Context context) {
            this(context, 0);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            mContext = context;
            mTheme = themeResId;
        }

        public Builder setView(View view) {
            mView = view;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(mContext, mTheme);
            dialog.setView(mView);
            return dialog;
        }

        public CustomDialog show() {
            final CustomDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
