package com.sisensing.common.utils

import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.sisensing.common.R
import com.sisensing.common.user.UserInfoUtils

object BgUnitUtils {

    private var unitList: Array<String>? = null

    private val SP_KEY_UNIT_OLD = "unit"
    private val SP_KEY_UNIT = "sp_key_unit"

    private val UNIT_TYPE_MOL = 1
    private val UNIT_TYPE_MG = 2

    @JvmStatic
    val UNIT_MOL_COMPAT = "mmol/L"

    @JvmStatic
    val UNIT_MG_COMPAT = "mg/dL"

    @JvmStatic
    fun getMolUnit(): String {
        getUnitList()
        return if (unitList!!.size > 1) {
            unitList!![0]
        } else {
            ""
        }
    }

    @JvmStatic
    fun getMgUnit(): String {
        getUnitList()
        return if (unitList!!.size > 1) {
            unitList!![1]
        } else {
            ""
        }
    }

    /**
     * 刷新资源
     */
    @JvmStatic
    fun refreshResourceUnit() {
        unitList = arrayOf(
            StringUtils.getString(R.string.mol_unit),
            StringUtils.getString(R.string.mg_dl)
        )
    }

    /**
     * 获取单位
     */
    @JvmStatic
    fun getUnitList(): List<String> {
        if (unitList == null) {
            unitList = arrayOf(
                StringUtils.getString(R.string.mol_unit),
                StringUtils.getString(R.string.mg_dl)
            )
        }
        return unitList!!.toList()
    }

    @JvmStatic
    fun isUserMol(): Boolean {
        return isUnitMol(getUserUnit())
    }

    @JvmStatic
    fun getUserUnitType(): Int {
        return if (isUserMol()) UNIT_TYPE_MOL else UNIT_TYPE_MG
    }

    @JvmStatic
    fun getUserUnit(): String {
        return getUserUnit(UserInfoUtils.getUserId())
    }

    /**
     *
     */
    @JvmStatic
    fun getUserUnit(userId: String?): String {
        val oldUnit = SPUtils.getInstance(userId).getString(SP_KEY_UNIT_OLD)
        val type = SPUtils.getInstance(userId).getInt(SP_KEY_UNIT, -1)
        if (type > 0) {
            if (type == 1) {
                return getMolUnit()
            } else {
                return getMgUnit()
            }
        }
        if (!oldUnit.isNullOrEmpty()) {
            if (oldUnit == UNIT_MOL_COMPAT) {
                return getMolUnit()
            } else {
                return getMgUnit()
            }
        }
        return getMolUnit()
    }

    @JvmStatic
    fun setUserUnit(userId: String?, isMol: Boolean) {
        SPUtils.getInstance(userId).put(SP_KEY_UNIT, if (isMol) UNIT_TYPE_MOL else UNIT_TYPE_MG)
        refreshResourceUnit()
    }

    @JvmStatic
    fun isUnitMol(unit: String?): Boolean {
        if (unit.isNullOrEmpty()) {
            return false
        }
        return unit == getMolUnit()
    }

    @JvmStatic
    fun isTypeMol(type: String?): Boolean {
        return (type != null && type == UNIT_TYPE_MOL.toString())
    }

    @JvmStatic
    fun isMg(unit: String?): Boolean {
        if (unit.isNullOrEmpty()) {
            return false
        }
        return unit == getMgUnit()
    }

    @JvmStatic
    fun isMolCompat(unit: String?): Boolean {
        if (unit.isNullOrEmpty()) {
            return false
        }
        if (unit == UNIT_MOL_COMPAT) {
            return true
        } else if (unit == UNIT_MG_COMPAT) {
            return false
        }
        if (unit == UNIT_TYPE_MOL.toString()) {
            return true
        } else if (unit == UNIT_TYPE_MG.toString()) {
            return false
        }
        return false
    }

    @JvmStatic
    fun getMolCompat(): String {
        return UNIT_MOL_COMPAT
    }

    @JvmStatic
    fun getMgCompat(): String {
        return UNIT_MG_COMPAT
    }

    fun getUnitFromType(type: Int?) :String{
        if (type == null) {
            return ""
        }
        return if (type == 1) getMolUnit() else getMgUnit()
    }

}