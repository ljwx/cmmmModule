package com.sisensing.common.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.blankj.utilcode.util.ObjectUtils;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.lxj.xpopup.core.BottomPopupView;
import com.sisensing.common.R;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/3/9 15:32
 * @desc 自定义底部弹出的滚轮选择器
 */
public class CustomWheelSelectPopup extends BottomPopupView {
    //数据
    private List<String> dataList;
    private WheelView wheelView;
    private OnItemSelectedListener mListener;
    private int currentItem = 0;
    private int selectItem;

    public CustomWheelSelectPopup(@NonNull Context context,List<String> datas,OnItemSelectedListener listener) {
        super(context);
        this.dataList = datas;
        this.mListener = listener;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        currentItem = selectItem;
    }

    public void autoMatchItem(String item) {
        for (int i = 0; i < dataList.size(); i++) {
            if (ObjectUtils.isNotEmpty(item) && item.equals(dataList.get(i))) {
                setSelectItem(i);
            }
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_popup_select_wheel;
    }
    @Override
    protected void onCreate() {
        super.onCreate();

        wheelView = findViewById(R.id.wheelView);
        wheelView.setCyclic(false);

        wheelView.setAdapter(new ArrayWheelAdapter(dataList));

        wheelView.setCurrentItem(selectItem);
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currentItem = index;
            }
        });


        TextView mTvFinish = findViewById(R.id.tv_finish);
        mTvFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener!=null){
                    mListener.onItemSelected(currentItem);
                }
            }
        });
        TextView mTvCancel = findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView mTvTitle = findViewById(R.id.tv_title);
    }

}
