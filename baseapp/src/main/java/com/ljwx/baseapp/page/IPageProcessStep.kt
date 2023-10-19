package com.ljwx.baseapp.page

interface IPageProcessStep {

    fun commonProcessSteps()

    fun getFirstInitData()

    fun initUIView()

    fun setClickListener()

    fun getAsyncData()

}