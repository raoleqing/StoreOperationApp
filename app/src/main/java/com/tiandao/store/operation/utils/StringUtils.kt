package com.tiandao.store.operation.utils

import com.tiandao.store.operation.common.enum.EnumType

object StringUtils {

    /**
     * 获取审核状态文本
     * **/
    fun getAuditStatusText(status: Int): String {
        return when(status){
            EnumType.AuditStatus.WAIT_AUDIT -> "待审核"
            EnumType.AuditStatus.WAIT_PROGRESS -> "审核中"
            EnumType.AuditStatus.AUDIT_PASS -> "通过"
            EnumType.AuditStatus.AUDIT_FAIL -> "拒绝"
            EnumType.AuditStatus.AUDIT_CANCEL -> "驳回"
            else -> "未知"
        }
    }

    /**
     * 获取订单状态文本
     * **/
    fun getOrderStatusText(status: Int): String {
        return when(status){
            EnumType.OrderStatus.WAIT_PAY -> "待支付"
            EnumType.OrderStatus.PAYING -> "支付中"
            EnumType.OrderStatus.PAID -> "已支付"
            EnumType.OrderStatus.COMPLETE -> "已完成"
            EnumType.OrderStatus.CANCEL -> "已取消"
            EnumType.OrderStatus.REFUND -> "已退款"
            EnumType.OrderStatus.AUDITING -> "审核中"
            else -> "未知"
        }
    }

    /**
     * 支付类型文本
     * **/
    fun getPayTypeText(type: String): String {
        return when(type){
            EnumType.PayType.WECHAT -> "微信"
            EnumType.PayType.ALIPAY -> "支付宝"
            EnumType.PayType.CASH -> "现金"
            EnumType.PayType.GIVE -> "赠送"
            EnumType.PayType.REFUND -> "返还"
            else -> "未知"
        }
    }

    fun getUnitText(type:  String): String {
        return when(type){
            "shop" -> "年"
            "sms" -> "条"
            else -> ""
        }
    }

    //1=比例提成,2=固定提成
    fun getDeductWay(type:  Int): String{
        return when(type){
            1 -> "比例提成"
            2 -> "固定提成"
            else -> ""
        }
    }

}