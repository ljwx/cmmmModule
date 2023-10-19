package com.ljwx.basemodule;

import com.ljwx.baseactivity.BaseActivity;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

public class TestJa extends BaseActivity {

    public void test() {
        threadRun(2000, new Function2<CoroutineScope, Continuation<? super Unit>, Object>() {
            @Override
            public Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {



                return null;
            }
        });
    }

}
