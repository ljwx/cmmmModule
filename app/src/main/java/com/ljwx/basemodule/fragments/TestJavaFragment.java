package com.ljwx.basemodule.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ljwx.basefragment.BaseBindingFragment;
import com.ljwx.basemodule.BuildConfig;
import com.ljwx.basemodule.R;
import com.ljwx.basemodule.databinding.FragmentJavaTestBinding;
import com.ljwx.baseswitchenv.ActivityEnvExtensionKt;
import com.ljwx.baseswitchenv.AppEnvItem;
import com.ljwx.baseswitchenv.ShakeSelectAppEnv;

public class TestJavaFragment extends BaseBindingFragment<FragmentJavaTestBinding> {

    public TestJavaFragment(int layoutResID) {
        super(R.layout.fragment_java_test);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000l);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.text.setText("修改");
                    }
                });
            }
        }).start();

        ActivityEnvExtensionKt.registerShakeEnv(requireActivity(), new ShakeSelectAppEnv.EnvCallback() {
            @Override
            public void selected(@NonNull AppEnvItem item) {

            }
        });

    }

    @Override
    public void setPopupLoadingLayout(int layout) {

    }
}
