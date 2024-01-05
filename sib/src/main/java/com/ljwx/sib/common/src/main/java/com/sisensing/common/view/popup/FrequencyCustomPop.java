package com.sisensing.common.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.contrarywind.listener.OnItemSelectedListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.sisensing.common.R;
import com.sisensing.common.entity.clcok.FrequencyCustomBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/5/19 16:17
 * @desc
 */
public class FrequencyCustomPop extends BottomPopupView {
    //数据
    private List<FrequencyCustomBean> dataList;
    private SelectListener mListener;
    public FrequencyCustomPop(@NonNull Context context,List<FrequencyCustomBean> datas,SelectListener listener) {
        super(context);
        this.dataList = datas;
        this.mListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_pop_frequency_custom;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView mTvFinish = findViewById(R.id.tv_finish);
        mTvFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.getSelectData(dataList);
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

        RecyclerView mRyWeek = findViewById(R.id.ry_week);
        mRyWeek.setLayoutManager(new LinearLayoutManager(getContext()));
        FrequencyCustomAdapter frequencyCustomAdapter = new FrequencyCustomAdapter(R.layout.common_item_frequency_custom,dataList);
        mRyWeek.setAdapter(frequencyCustomAdapter);


        frequencyCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                FrequencyCustomBean bean = dataList.get(position);
                boolean isSelect = bean.isSelect();
                if (isSelect){
                    bean.setSelect(false);
                }else {
                    bean.setSelect(true);
                }
                frequencyCustomAdapter.notifyDataSetChanged();
            }
        });
    }

    class FrequencyCustomAdapter extends BaseQuickAdapter<FrequencyCustomBean, BaseViewHolder>{


        public FrequencyCustomAdapter(int layoutResId, @Nullable List<FrequencyCustomBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, FrequencyCustomBean frequencyCustomBean) {
            baseViewHolder.setText(R.id.tv_text,frequencyCustomBean.getText());
            ImageView mIvSelect = baseViewHolder.getView(R.id.iv_select);
            if (frequencyCustomBean.isSelect()){
                mIvSelect.setVisibility(VISIBLE);
            }else {
                mIvSelect.setVisibility(GONE);
            }
        }
    }

    public interface SelectListener{
        void getSelectData(List<FrequencyCustomBean> list);
    }
}
