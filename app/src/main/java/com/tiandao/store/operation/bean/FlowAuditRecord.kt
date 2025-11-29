package com.tiandao.store.operation.bean

import java.io.Serializable

class FlowAuditRecord (
    var id: Long,
    var flowId: Long,
    var flowName: String,
    var flowType: String,
    var bizId: Long,
    var auditItemId: Long,
    var auditItemName: String,
    var auditOpId: String,
    var auditOpName: String,
    var auditStatus: Int,
    var auditRemark: String,
    var auditTime: String,
    var createId: Long,
    var updateId: Long,
    var createTime: String,
    var flowAuditItemList: List<FlowAuditRecordItem>? = null,
): Serializable