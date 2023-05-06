package com.ljwx.basemodule;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ljwx.baseapp.BaseViewModel;
import com.ljwx.basefragment.BaseMVVMFragment;
import com.ljwx.basemodule.databinding.FragmentJavaTestBinding;

public class TestJavaFragment extends BaseMVVMFragment<FragmentJavaTestBinding, BaseViewModel> {

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

    }
}
