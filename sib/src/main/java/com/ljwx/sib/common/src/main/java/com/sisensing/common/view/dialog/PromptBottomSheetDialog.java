package com.sisensing.common.view.dialog;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sisensing.common.R;


public class PromptBottomSheetDialog {

    private final Context context;
    private BottomSheetDialog dialog;
    private TextView txt_title;
    private TextView tvContent;
    private TextView tvContent2;
    private final Display display;

    public PromptBottomSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public PromptBottomSheetDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_prompt_bottom_sheet, null);
        view.setMinimumWidth(display.getWidth());
        tvContent =  view.findViewById(R.id.tvContent);
        tvContent2 =  view.findViewById(R.id.tvContent2);
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSheetItemClickListener !=null){
                    onSheetItemClickListener.onClick(1);
                }
            }
        });
        tvContent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSheetItemClickListener !=null){
                    onSheetItemClickListener.onClick(2);
                }
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new BottomSheetDialog(context);
         dialog.setContentView(view);
        dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet).setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));

        return this;
    }

//    public PromptBottomSheetDialog setTitle(String title) {
//        txt_title.setText(title);
//        return this;
//    }

    public PromptBottomSheetDialog setContent(String title) {
        tvContent.setText(title);
        tvContent.setVisibility(View.VISIBLE);
        return this;
    }
    public PromptBottomSheetDialog setContent1(String title) {
        tvContent2.setText(title);
        tvContent2.setVisibility(View.VISIBLE);
        return this;
    }


    public PromptBottomSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public PromptBottomSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }
    public void dismiss() {
        dialog.dismiss();
    }

    private OnSheetItemClickListener onSheetItemClickListener;

    public OnSheetItemClickListener getOnSheetItemClickListener() {
        return onSheetItemClickListener;
    }

    public void setOnSheetItemClickListener(OnSheetItemClickListener onSheetItemClickListener) {
        this.onSheetItemClickListener = onSheetItemClickListener;
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }


}
