package com.sisensing.common.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.sisensing.common.R;
import com.sisensing.common.adapter.BfzAdapter;
import com.sisensing.common.entity.personalcenter.DictTypeEntity;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/6/4 14:31
 * @desc 并发症弹窗
 */
public class BfzOrZlfsPopup extends BottomPopupView {
    private Context mContext;
    private String finalBfzStr;
    private BfzOrZlfsListener mListener;
    private List<DictTypeEntity> mList;
    private int mType;

    public BfzOrZlfsPopup(@NonNull Context context, int type,BfzOrZlfsListener listener, List<DictTypeEntity> list) {
        super(context);
        this.mContext = context;
        this.mType = type;
        this.mListener = listener;
        this.mList = list;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_pop_bfz;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView mTvFinish = findViewById(R.id.tv_finish);
        mTvFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.getBfzOrZlfsTypeStr(mType,finalBfzStr);
            }
        });
        TextView mTvCancel = findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(v -> dismiss());
        TextView mTvTitle = findViewById(R.id.tv_title);


        RecyclerView mRyBfz = findViewById(R.id.ry_bfz);
        mRyBfz.setLayoutManager(new GridLayoutManager(mContext,2));

        BfzAdapter bfzAdapter = new BfzAdapter(R.layout.common_item_tnb_type, mList);
        mRyBfz.setAdapter(bfzAdapter);

        bfzAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

                if (position == 0){
                    for (int i = 0; i < mList.size(); i++) {
                        DictTypeEntity entity = mList.get(i);
                        if (i == position){
                            entity.setSelect(true);
                        }else {
                            entity.setSelect(false);
                        }
                    }
                }else {
                    mList.get(0).setSelect(false);
                    DictTypeEntity entity = mList.get(position);
                    if (entity.isSelect()){
                        entity.setSelect(false);
                    }else {
                        entity.setSelect(true);
                    }
                }
                bfzAdapter.notifyDataSetChanged();

                StringBuffer buffer = new StringBuffer();
                for (DictTypeEntity entity : mList) {
                    if (entity.isSelect()) {
                        buffer.append(entity.getDictValue());
                        buffer.append(",");
                    }
                }
                String bfzStr = buffer.toString();
                if (ObjectUtils.isNotEmpty(bfzStr)){
                    //去掉最后一个字符串
                    finalBfzStr = bfzStr.substring(0,bfzStr.length()-1);
                }
            }
        });
    }

    public interface BfzOrZlfsListener{
        /**
         *
         * @param type 1，并发症 2.治疗方式
         * @param str
         */
        void getBfzOrZlfsTypeStr(int type,String str);
    }
}
