package com.tiandao.store.operation.bean

import java.io.Serializable

class SysUser (
    var tenantId: Long,
    var shopId: Long,
    var userId: Long,
    var deptId: Long,
    var userName: String,
    var nickName: String,
    var email: String?,
    var phonenumber: String,
    var sex: String,
    var avatar: String?,
    var password: String,
    var status: String,
    var joinDate: String,
): Serializable