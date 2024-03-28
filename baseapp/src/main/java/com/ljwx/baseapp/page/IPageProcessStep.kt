package com.ljwx.baseapp.page

interface IPageProcessStep {

    fun commonProcessSteps()

    fun getFirstInitData()

    fun initUIView()

    fun observeData()

    fun setClickListener()

    fun getAsyncData(refresh: Boolean = true)

}