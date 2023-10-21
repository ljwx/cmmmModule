package com.ljwx.basetoolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

public class CenterTitleBar extends Toolbar {

    LinearLayout vCenteredBox;
    public TextView vCenteredTitle;
    TextView vCenteredSubtitle;

    CharSequence mTitle;
    int mCenteredTitleTextAppearance;
    int mCenteredTitleTextColor;

    CharSequence mSubtitle;
    int mCenteredSubtitleTextAppearance;
    int mCenteredSubtitleTextColor;

    public CenterTitleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CenterTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CenterTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (getId() == NO_ID) {
            setId(R.id.base_app_toolbar);
        }

        final TypedArray a = getContext().obtainStyledAttributes(attrs, androidx.appcompat.R.styleable.Toolbar, defStyle, 0);
        mCenteredTitleTextAppearance = a.getResourceId(androidx.appcompat.R.styleable.Toolbar_titleTextAppearance, 0);
        mCenteredSubtitleTextAppearance = a.getResourceId(androidx.appcompat.R.styleable.Toolbar_subtitleTextAppearance, 0);
        a.recycle();

        setTitleTextAppearance(getContext(), mCenteredTitleTextAppearance);
        setSubtitleTextAppearance(getContext(), mCenteredSubtitleTextAppearance);
    }

    private void ensureCenteredBox() {
        if (vCenteredBox == null) {
            vCenteredBox = new LinearLayout(getContext());
            vCenteredBox.setOrientation(LinearLayout.VERTICAL);
            addView(vCenteredBox, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        }
    }

    @Override
    public CharSequence getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (vCenteredTitle == null) {
                ensureCenteredBox();
                vCenteredTitle = new AppCompatTextView(getContext());
                vCenteredTitle.setSingleLine();
                vCenteredTitle.setEllipsize(TextUtils.TruncateAt.END);
                vCenteredTitle.setGravity(Gravity.CENTER);
                if (mCenteredTitleTextAppearance != 0) {
                    vCenteredTitle.setTextAppearance(getContext(), mCenteredTitleTextAppearance);
                }
                if (mCenteredTitleTextColor != 0) {
                    vCenteredTitle.setTextColor(mCenteredTitleTextColor);
                }
                vCenteredTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                vCenteredBox.addView(vCenteredTitle, 0);
            }
        }
        if (vCenteredTitle != null) {
            vCenteredTitle.setText(title);
        }
        mTitle = title;
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        mCenteredTitleTextColor = color;
        if (vCenteredTitle != null) {
            vCenteredTitle.setTextColor(color);
        }
    }

    @Override
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        mCenteredTitleTextAppearance = resId;
        if (vCenteredTitle != null) {
            vCenteredTitle.setTextAppearance(context, resId);
        }
    }

    @Override
    public CharSequence getSubtitle() {
        return mSubtitle;
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        if (!TextUtils.isEmpty(subtitle)) {
            if (vCenteredSubtitle == null) {
                ensureCenteredBox();
                vCenteredSubtitle = new AppCompatTextView(getContext());
                vCenteredSubtitle.setSingleLine();
                vCenteredSubtitle.setEllipsize(TextUtils.TruncateAt.END);
                vCenteredSubtitle.setGravity(Gravity.CENTER);
                vCenteredSubtitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                if (mCenteredSubtitleTextAppearance != 0) {
                    vCenteredSubtitle.setTextAppearance(getContext(), mCenteredSubtitleTextAppearance);
                }
                if (mCenteredSubtitleTextColor != 0) {
                    vCenteredSubtitle.setTextColor(mCenteredSubtitleTextColor);
                }
                vCenteredBox.addView(vCenteredSubtitle);
            }
        }
        if (vCenteredSubtitle != null) {
            vCenteredSubtitle.setText(subtitle);
        }
        mSubtitle = subtitle;
    }

    @Override
    public void setSubtitleTextColor(@ColorInt int color) {
        mCenteredSubtitleTextColor = color;
        if (vCenteredSubtitle != null) {
            vCenteredSubtitle.setTextColor(color);
        }
    }

    @Override
    public void setSubtitleTextAppearance(Context context, @StyleRes int resId) {
        mCenteredSubtitleTextAppearance = resId;
        if (vCenteredSubtitle != null) {
            vCenteredSubtitle.setTextAppearance(context, resId);
        }
    }
}
