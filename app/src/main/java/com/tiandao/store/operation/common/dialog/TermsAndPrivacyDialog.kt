package com.tiandao.store.operation.common.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.tiandao.store.operation.R
import com.tiandao.store.operation.databinding.TermsAndPrivacyDialogBinding
import com.tiandao.store.operation.network.RetrofitClient
import com.tiandao.store.operation.ui.webview.WebViewActivity
import kotlin.jvm.java
import kotlin.let
import kotlin.text.indexOf

class TermsAndPrivacyDialog (context: Activity, private val listener: OnFilterAppliedListener) : AppCompatDialog(context) {

    private var binding: TermsAndPrivacyDialogBinding? = null

    interface OnFilterAppliedListener {
        fun determine(dialog: TermsAndPrivacyDialog)
        fun close(dialog: TermsAndPrivacyDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 关键设置：透明背景 + 白色内容布局
        //requestWindowFeature(Window.FEATURE_NO_TITLE) // 隐藏标题栏
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 透明背景

        binding = TermsAndPrivacyDialogBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)
            window?.let { item ->
                val params = item.attributes
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                params.gravity = Gravity.CENTER
                item.attributes = params
            }
        }
        binding?.tvMessage?.text = Html.fromHtml(context.getString(R.string.terms_and_privacy_text),
            Html.FROM_HTML_MODE_LEGACY
        )
        binding?.tvMessage?.movementMethod = LinkMovementMethod.getInstance()


        val spannable = SpannableStringBuilder(Html.fromHtml(context.getString(R.string.terms_and_privacy_text)))
        // 找到链接文本的位置
        val termsStart = spannable.toString().indexOf("《用户协议》")
        val termsEnd = termsStart + "《用户协议》".length
        val privacyStart = spannable.toString().indexOf("《隐私政策》")
        val privacyEnd = privacyStart + "《隐私政策》".length

        // 替换《用户协议》点击行为
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // 跳转到协议详情页（如 WebViewActivity）
                    val url = RetrofitClient.TERMS_URL
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra(WebViewActivity.EXTRA_URL, url)
                    context.startActivity(intent)
                }
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(context, R.color.colorPrimary) // 自定义链接颜色
                    ds.isUnderlineText = true // 保留下划线
                }
            },
            termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val url = RetrofitClient.PRIVACY_URL
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra(WebViewActivity.EXTRA_URL, url)
                    context.startActivity(intent)
                }
            },
            privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding?.tvMessage?.text = spannable
        binding?.tvMessage?.movementMethod = LinkMovementMethod.getInstance()

        binding?.buttonConfirm?.setOnClickListener {
            listener.determine(this)
        }

        binding?.buttonClose?.setOnClickListener {
            listener.close(this)
        }
    }

}