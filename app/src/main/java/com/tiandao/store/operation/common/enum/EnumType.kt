package com.tiandao.store.operation.common.enum

class EnumType {

     //订单状态  0=待支付,1=已支付,2=已完成,3=已取消,4=已退款
    object OrderStatus {
        const val  WAIT_PAY = 0 //待支付
        const val  PAYING = 1 //支付中
        const val  PAID = 2 //已支付
        const val  COMPLETE = 3 //已完成
        const val  CANCEL = 4  //已取消
        const val  REFUND = 5 //已退款
        const val AUDITING = 6 //审核中
    }

    //审核状态(0=未审核,1=审核通过,2=审核不通过)
    object AuditStatus{
        const val  WAIT_AUDIT = 0 //待审核
        const val  WAIT_PROGRESS = 1 //审核中
        const val  AUDIT_PASS = 2 //审核通过
        const val  AUDIT_FAIL = 3 //审核未通过
        const val  AUDIT_CANCEL = 4 //审核驳回

    }

    //val levels = arrayOf("微信", "支付宝", "现金","赠送","返还")
    object PayType{
        const val  WECHAT = "wechat" //微信
        const val  ALIPAY = "alipay" //支付宝
        const val  CASH = "cash" //现金
        const val  GIVE = "give" //赠送
        const val  REFUND = "refund" //返还
    }
}