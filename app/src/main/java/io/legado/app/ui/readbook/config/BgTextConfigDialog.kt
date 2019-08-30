package io.legado.app.ui.readbook.config

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import io.legado.app.R
import io.legado.app.constant.Bus
import io.legado.app.help.ReadBookConfig
import io.legado.app.ui.readbook.Help
import io.legado.app.utils.postEvent
import kotlinx.android.synthetic.main.dialog_read_bg_text.*
import okhttp3.internal.toHexString
import org.jetbrains.anko.sdk27.listeners.onClick

class BgTextConfigDialog : DialogFragment(), ColorPickerDialogListener {

    val selectTextColor = 121
    val selectBgColor = 122

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_read_bg_text, container)
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog?.window?.let {
            it.setBackgroundDrawableResource(R.color.transparent)
            it.decorView.setPadding(0, 0, 0, 0)
            val attr = it.attributes
            attr.dimAmount = 0.0f
            attr.gravity = Gravity.BOTTOM
            it.attributes = attr
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            Help.upSystemUiVisibility(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() = with(ReadBookConfig.getConfig()) {
        sw_dark_status_icon.isChecked = statusIconDark()
    }

    private fun initView() = with(ReadBookConfig.getConfig()) {
        tv_text_color.onClick {
            ColorPickerDialog.newBuilder()
                .setColor(textColor())
                .setShowAlphaSlider(false)
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setDialogId(selectTextColor)
                .show(requireActivity())
        }
        tv_bg_color.onClick {
            val bgColor =
                if (bgType() == 0) Color.parseColor(bgStr())
                else Color.parseColor("#015A86")
            ColorPickerDialog.newBuilder()
                .setColor(bgColor)
                .setShowAlphaSlider(false)
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setDialogId(selectTextColor)
                .show(requireActivity())
        }
        tv_default.onClick {

        }
    }

    override fun onColorSelected(dialogId: Int, color: Int) = with(ReadBookConfig.getConfig()) {
        when (dialogId) {
            selectTextColor -> {
                setTextColor(color)
                postEvent(Bus.UP_CONFIG, false)
            }
            selectBgColor -> {
                setBg(0, "#${color.toHexString()}")
                postEvent(Bus.UP_CONFIG, false)
            }
        }
    }

    override fun onDialogDismissed(dialogId: Int) {

    }
}