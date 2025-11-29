package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal

class PlatformRechargePlan(
    var id: Long,
    var name: String,
    var type: String,
    var amount: Int,
    var price: BigDecimal,
    var description: String,
    var status: Long,
    var level: Int,
): Serializable