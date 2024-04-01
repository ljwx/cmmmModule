package com.ljwx.basemodule.oldcompat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CompatFragment extends Fragment {

    CompatViewModel vm = new ViewModelProvider(this).get(CompatViewModel.class);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
}
