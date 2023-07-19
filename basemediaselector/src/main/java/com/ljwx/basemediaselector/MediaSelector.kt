package com.ljwx.basemediaselector

import android.content.Context
import com.ljwx.basemediaselector.style.MediaSelectorRbWhiteStyle
import com.luck.picture.lib.basic.PictureSelectionModel
import com.luck.picture.lib.basic.PictureSelectionSystemModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorStyle
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File
import java.util.ArrayList

class MediaSelector internal constructor(builder: Builder) {

    private val mBuilder = builder

    /**
     * 选择类型
     */
    companion object {
        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2
        const val TYPE_AUDIO = 3
        const val TYPE_ALL = 0
    }

    /**
     * 自定义回调
     */
    private var mResultListener: ResultListener? = null

    /**
     * 已选的文件集合
     */
    private val mSelectedThirdData = ArrayList<LocalMedia>()
    private val mSelectedLocalData = ArrayList<MediaSelectorFile>()

    /**
     * 选择结果回调
     */
    private val mCallbackListener by lazy {
        object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>) {
                if (mBuilder.filterSelected) {
                    // 不能重复选择,则清除之前数据
                    mSelectedThirdData.clear()
                    mSelectedLocalData.clear()
                }
                // 更新数据
                mSelectedThirdData.addAll(result)
                result.forEach {
                    mSelectedLocalData.add(MediaSelectorFile(it))
                }
                // 根据选择排序
                sortPosition()
                mResultListener?.onResult(mSelectedLocalData)
            }

            override fun onCancel() {
                mResultListener?.onCancel()
            }
        }
    }

    class Builder(context: Context) {

        private val context = context
        private val mediaSelector = PictureSelector.create(context)

        internal var mNormal: PictureSelectionModel? = null
        internal var mSystem: PictureSelectionSystemModel? = null

        /**
         * 是否使用系统自带相册
         */
        private var useSystem = false

        /**
         * 图片加载器
         */
        internal var engine: ImageEngine? = null

        /**
         * 选择文件类型
         */
        private var mimeType = SelectMimeType.TYPE_IMAGE

        /**
         * 最大选择数量
         */
        private var maxSelect = 1

        /**
         * 是否压缩图片
         */
        private var compress: CompressFileEngine? = null

        /**
         * 是否显示拍照按钮
         */
        private var showCamera: Boolean = false

        internal var filterSelected = true

        /**
         * 是否使用系统相册
         */
        fun useSystem(use: Boolean): Builder {
            useSystem = use
            return this
        }

        /**
         * 图片加载器
         */
        fun setImageEngine(engine: ImageEngine): Builder {
            this.engine = engine
            return this
        }

        /**
         * 选择文件类型
         */
        fun selectType(type: Int): Builder {
            mimeType = type
            return this
        }

        /**
         * 最大选择数量
         */
        fun maxSelectNum(max: Int): Builder {
            maxSelect = max
            return this
        }

        /**
         * 已选集合
         */
        fun filterSelected(filter: Boolean): Builder {
            this.filterSelected = filter
            return this
        }

        /**
         * 是否显示相机
         */
        fun showCamera(show: Boolean): Builder {
            showCamera = show
            return this
        }

        /**
         * 是否使用压缩
         *
         * @param ignoreSize 默认100K以下的图片不压缩
         */
        fun useCompress(use: Boolean, ignoreSize: Int = 100): Builder {
            if (use) {
                compress = compress ?: CompressFileEngine { context, source, call ->
                    Luban.with(context).load(source).ignoreBy(ignoreSize)
                        .setCompressListener(object : OnNewCompressListener {
                            override fun onStart() {
                            }

                            override fun onSuccess(source: String?, compressFile: File?) {
                                call?.onCallback(source, compressFile?.absolutePath)
                            }

                            override fun onError(source: String?, e: Throwable?) {
                                call?.onCallback(source, null)
                            }

                        }).launch()
                }
            } else {
                compress = null
            }
            return this
        }

        fun build(): MediaSelector {
            if (useSystem) {
                // 系统自带配置
                mSystem = mediaSelector.openSystemGallery(mimeType)
                compress?.let {
                    mSystem?.setCompressEngine(compress)
                }
            } else {
                // 默认配置

                // 文件类型
                mNormal = mediaSelector.openGallery(mimeType)
                // 图片加载器
                if (engine == null) {
                    mNormal?.setImageEngine(PictureSelectorEngine.engine())
                } else {
                    mNormal?.setImageEngine(engine)
                }
                // 压缩引擎
                compress?.let {
                    mNormal?.setCompressEngine(compress)
                }
                // 最大选择数量
                mNormal?.setMaxSelectNum(maxSelect)
                // 是否显示相机
                mNormal?.isDisplayCamera(showCamera)
//                // 已选数据(启动的时候设置)
//                if (filterSelected) {
//                    mNormal?.setSelectedData(selectedData)
//                }
                // 底部显示选中数量
                val style = PictureSelectorStyle()
                style.selectMainStyle = MediaSelectorRbWhiteStyle().getStyle(context)
                mNormal?.setSelectorUIStyle(style)
            }
            return MediaSelector(this)
        }

    }

    /**
     * 移除选中文件
     */
    fun removeItem(media: MediaSelectorFile) {
        var item: LocalMedia? = null
        // 查找相同
        mSelectedThirdData?.forEach {
            if (media.id == it.id && media.fileName == it.fileName) {
                item = it
            }
        }
        // 内部移除
        item?.let {
            mSelectedThirdData?.remove(it)
        }
    }

    /**
     * 获取原始的selector
     */
    fun getOriginSelector(): PictureSelectionModel? {
        return mBuilder.mNormal
    }

    /**
     * 结果监听
     */
    fun setResultListener(listener: ResultListener): MediaSelector {
        mResultListener = listener
        return this
    }

    /**
     * 启动选择器
     */
    fun launch() {
        mResultListener?.let {
            // 第二次启动engine会报空,以后研究
            if (PictureSelectionConfig.imageEngine == null) {
                if (mBuilder.engine == null) {
                    mBuilder.mNormal?.setImageEngine(PictureSelectorEngine.engine())
                } else {
                    mBuilder.mNormal?.setImageEngine(mBuilder.engine)
                }
            }
            // 已选数据
            if (mBuilder.filterSelected) {
                mBuilder.mNormal?.setSelectedData(mSelectedThirdData)
            }
            mBuilder.mNormal?.forResult(mCallbackListener)
            mBuilder.mSystem?.forSystemResult(mCallbackListener)
        }
    }

    /**
     * 修改最大选中数量(一般相同文件可以多次上传时使用,最大可选减去已选)
     */
    fun changeMaxNumber(max: Int) {
        mBuilder.mNormal?.setMaxSelectNum(max)
    }

    /**
     * 选中结果转换为自己的数据结构
     */
    private fun transFilterSelected() {
        // 删除取消的
        val iterator = mSelectedLocalData.iterator()
        while (iterator.hasNext()) {
            var minus = true
            val local = iterator.next()
            mSelectedThirdData.forEach { third ->
                if (third.id == local.id && third.fileName == local.fileName) {
                    minus = false
                }
            }
            if (minus) {
                iterator.remove()
            }
        }
        // 添加新选的
        mSelectedThirdData.forEach { third ->
            var add = true
            mSelectedLocalData.forEach { local ->
                if (third.id == local.id && third.fileName == local.fileName) {
                    add = false
                }
            }
            if (add) {
                mSelectedLocalData.add(MediaSelectorFile(third))
            }
        }
    }

    /**
     * 选择排序
     */
    private fun sortPosition() {
        mSelectedThirdData.forEachIndexed { index, third ->
            mSelectedLocalData.forEach { local ->
                if (third.id == local.id && third.fileName == local.fileName) {
                    local.sortPosition = index
                }
            }
        }
    }

    /**
     * 选中结果监听器
     */
    abstract class ResultListener {
        abstract fun onResult(result: List<MediaSelectorFile>)
        fun onCancel() {}
    }


}