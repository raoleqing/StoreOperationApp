package com.tiandao.store.operation.bean

import java.io.Serializable

class SysUserBo (
    var sysUser: SysUser,
    var roles: List<RoleBo>?,
): Serializable