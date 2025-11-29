package com.tiandao.store.operation.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tiandao.store.operation.bean.LoginUser
import com.tiandao.store.operation.bean.RoleBo
import com.tiandao.store.operation.bean.User
import com.tiandao.store.operation.bean.UserBo
import com.tiandao.store.operation.ui.order.OrderActivity
import kotlin.let

object UserUtils {
    // 保存登录状态
    fun saveLoginState(context: Context, isLoggedIn: Boolean, user: User? = null) {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_logged_in", isLoggedIn)
            user?.let {
                putString("token", it.token)
            }
            apply()
        }
    }

    // 检查登录状态
    fun isLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("is_logged_in", false)
    }

    fun isAdmin(context: Context): Boolean {
        return getCurrentUser(context)?.let {
            it.loginUser.user.roles?.any { role ->
                role.roleId == 1L
            }
        } ?: false
    }

    fun saveUser(context: Context, user: User? = null){
        if (user == null) return
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            val userBo: UserBo = user.loginUser.user
            userBo.let {
                putLong("tenantId", userBo.tenantId)
                putLong("shopId", userBo.shopId)
                putLong("user_id", userBo.userId)
                putString("user_name", userBo.userName)
                putString("user_nickName", userBo.nickName)
                putString("user_avatar", userBo.avatar)
                putString("user_email", userBo.email)
                putString("user_sex", userBo.sex)
                putInt("user_status", userBo.status)
                putString("user_phonenumber", userBo.phonenumber)
                putInt("user_deptId", userBo.deptId)
                putInt("user_delFlag", userBo.delFlag)
                putString("user_remark", userBo.remark)
                putString("user_role", Gson().toJson(userBo.roles).toString())
                putString("joinDate", userBo.joinDate)
            }
            apply()
        }
    }

    // 获取当前用户信息
    fun getCurrentUser(context: Context): User? {
        if (!isLoggedIn(context)) return null

        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return User(
            loginUser = LoginUser(
                UserBo(
                    tenantId = sharedPref.getLong("tenantId", 0L),
                    shopId = sharedPref.getLong("shopId", 0L),
                    userId = sharedPref.getLong("user_id", 0L),
                    userName = sharedPref.getString("user_name", "") ?: "",
                    nickName = sharedPref.getString("user_nickName", "") ?: "",
                    deptId = sharedPref.getInt("user_deptId", 0),
                    email = sharedPref.getString("user_email", "") ?: "",
                    phonenumber = sharedPref.getString("user_phonenumber", "") ?: "",
                    sex = sharedPref.getString("user_sex", "")?: "",
                    avatar = sharedPref.getString("user_avatar", "") ?: "",
                    status = sharedPref.getInt("user_status", 0),
                    delFlag = sharedPref.getInt("user_delFlag", 0),
                    remark = sharedPref.getString("user_remark", "") ?: "",
                    roles = sharedPref.getString("user_role", "")?.let {
                        Gson().fromJson(it, object : TypeToken<List<RoleBo>>() {}.type)
                    } ?: emptyList(),
                    joinDate = sharedPref.getString("joinDate", "") ?: "",
                    )

            ),
            token = sharedPref.getString("token", "") ?: "",
        )
    }

    // 登出
    fun logout(context: Context) {
        saveLoginState(context, false)
        // 清除其他相关数据
        clearUserData(context)
    }

    private fun clearUserData(context: Context) {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("tenantId")
            remove("shopId")
            remove("token")
            remove("user_id")
            remove("user_name")
            remove("user_nickName")
            remove("user_avatar")
            remove("user_email")
            remove("user_sex")
            remove("user_status")
            remove("user_phonenumber")
            remove("user_deptId")
            remove("user_deptName")
            remove("user_delFlag")
            remove("user_remark")
            remove("joinDate")
            apply()
        }

    }

    fun getToken(context: Context): String {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""
        return token
    }

    fun getUserId(context: Context): Long {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("user_id", 0L)
        return userId
    }
    fun getUserName(context: Context): String {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "") ?: ""
        return userName
    }
    fun getUserNickName(context: Context): String {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userNickName = sharedPref.getString("user_nickName", "") ?: ""
        return userNickName
    }
    fun getTenantId(context: Context): Long {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val tenantId = sharedPref.getLong("tenantId", 0L)
        return tenantId
    }

    fun getPhoneNumber(context: Context): String {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val phoneNumber = sharedPref.getString("user_phonenumber", "") ?: ""
        return phoneNumber
    }




}