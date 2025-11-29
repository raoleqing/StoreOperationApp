package com.tiandao.store.operation.common.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import com.tiandao.store.operation.databinding.AuditDialogBinding
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.common.enum.EnumType
import com.tiandao.store.operation.R
import kotlin.let
import kotlin.text.isEmpty

class AuditDialog (private val activity: Activity,private val showCancelFlag: Boolean,
                   private val listener: OnFilterAppliedListener) : AppCompatDialog(activity){

    private var binding: AuditDialogBinding? = null
    var auditStatus : Int = EnumType.AuditStatus.AUDIT_PASS

    interface OnFilterAppliedListener {
        fun determine(dialog: AuditDialog,auditStatus : Int,remark: String)
        fun close(dialog: AuditDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 透明背景
        binding = AuditDialogBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)
            window?.let { item ->
                val params = item.attributes
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                params.gravity = Gravity.BOTTOM
                item.attributes = params
            }

            it.rbCancel.visibility = if(showCancelFlag) View.VISIBLE else View.GONE
            it.ivClose.setOnClickListener {
                listener.close( this)
            }

            it.butSubmit.setOnClickListener { view  ->
                var remark  = it.etRemark.text.toString();
                if(auditStatus != EnumType.AuditStatus.AUDIT_PASS && remark.isEmpty()){
                    ToastUtils.showShort("请填写原因")
                    return@setOnClickListener
                }
                listener.determine(this, auditStatus,remark)
            }

            it.rgStatus.setOnCheckedChangeListener { _, checkedId ->
                auditStatus = when(checkedId){
                    R.id.rb_pass -> EnumType.AuditStatus.AUDIT_PASS
                    R.id.rb_fail -> EnumType.AuditStatus.AUDIT_FAIL
                    else -> EnumType.AuditStatus.AUDIT_CANCEL
                }
            }

        }

    }
}