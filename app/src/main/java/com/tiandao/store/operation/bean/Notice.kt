package com.tiandao.store.operation.bean

import java.io.Serializable

class Notice (
    val tenantId : Long,
    val noticeId: Long,
    val noticeTitle: String,
    val noticeType: Int,
    val status: Int,
    val noticeContent: String,
    val createBy: String,
    val createTime: String,
    val updateBy: String,
    val updateTime: String,
    val remark: String,
    ): Serializable