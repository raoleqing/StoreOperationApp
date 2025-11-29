package com.tiandao.store.operation.bean

import java.io.Serializable

data class UserBo (
    val tenantId : Long,
    val shopId : Long,
    val userId : Long,
    val deptId : Int,
    val userName : String, //":"13875922087",
    val nickName : String?, //":"晴天",
    val email : String?, //":"",
    val phonenumber : String?, //":"13875922087",
    val sex : String, //":"男",
    val avatar : String?,
    val status : Int, //":"0",
    val delFlag : Int, //":"0",
    val remark : String?,
    val roles: List<RoleBo>?,
    val joinDate: String,
): Serializable
