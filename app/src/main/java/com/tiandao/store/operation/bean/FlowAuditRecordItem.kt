package com.tiandao.store.operation.bean

import java.io.Serializable
import java.sql.Time

class FlowAuditRecordItem(
    var id: Long,
    var flowRecordId: Long,
    var auditItemId: Long,
    var auditItemName: String,
    var auditOpId: Long,
    var auditOpName: String,
    var auditStatus: Int,
    var auditRemark: String,
    var auditTime: String,
    var updateTime: String,
): Serializable