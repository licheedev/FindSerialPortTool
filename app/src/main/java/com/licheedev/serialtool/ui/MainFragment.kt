package com.licheedev.serialtool.ui

//import com.licheedev.serialtool.comn.SerialPortManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.licheedev.hwutils.ByteUtil
import com.licheedev.myutils.LogPlus
import com.licheedev.myutils.SpUtil
import com.licheedev.serialtool.R
import com.licheedev.serialtool.comn.DeviceFilter
import com.licheedev.serialtool.comn.SerialPortManager
import com.licheedev.serialtool.databinding.FragmentMainBinding
import com.licheedev.serialtool.ui.base.binding.BaseBindingFragment
import com.licheedev.serialtool.ui.viewmodel.MainViewModel
import com.licheedev.serialtool.util.AllCapTransformationMethod
import com.licheedev.serialtool.util.constant.PreferenceKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {


    private val viewModel by viewModels<MainViewModel>()


    override fun bindingInflate(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding {
        return FragmentMainBinding::inflate
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.clickProxy = ClickProxy()


        binding.etData.transformationMethod = AllCapTransformationMethod(true)


        initSpinners()
        val lastSent = SpUtil.getDefault().getString(PreferenceKeys.LSAT_SENT, "")
        binding.etData.setText(lastSent)
    }


    override fun onDestroyView() {
        SerialPortManager.instance().close()
        super.onDestroyView()
    }

    private lateinit var mBaudrates: Array<String>
    private var mBaudrateIndex: Int = 0

    /**
     * 初始化下拉选项
     */
    private fun initSpinners() {

        // 波特率
        mBaudrates = resources.getStringArray(R.array.baudrates)
        mBaudrateIndex = SpUtil.getDefault().getInt(PreferenceKeys.BAUD_RATE, 0)
        val baudrateAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_default_item, mBaudrates)
        baudrateAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerBaudrate.adapter = baudrateAdapter
        binding.spinnerBaudrate.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBaudrateIndex = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 无视
            }
        }
        binding.spinnerBaudrate.setSelection(mBaudrateIndex)
    }


    /** 设备过滤器 */
    private val deviceFilter = object : DeviceFilter {

        override fun allow(device: String): Boolean {
            // 不允许打开usb的串口
            return !device.contains("usb", true)
        }

    }

    inner class ClickProxy {

        /**
         * 打开或关闭串口
         */
        fun switchSerialPort() {

            if (viewModel.opened.value!!) {
                viewModel.viewModelScope.launch {
                    showDelayWaitingDialog("正在关闭串口")
                    withContext(Dispatchers.IO) {
                        SerialPortManager.instance().close()
                    }
                    viewModel.opened.value = false
                    dismissDelayWaitingDialog()
                }
            } else {
                // 保存配置
                SpUtil.getDefault().saveInt(PreferenceKeys.BAUD_RATE, mBaudrateIndex)
                showDelayWaitingDialog("正在打开串口")
                viewModel.viewModelScope.launch {

                    val opened = withContext(Dispatchers.IO) {
                        val baudrate = mBaudrates[mBaudrateIndex].toInt()
                        SerialPortManager.instance().open(baudrate, deviceFilter)
                    }

                    dismissDelayWaitingDialog()

                    if (opened) {
                        showOneToast("成功打开串口")
                    } else {
                        showOneToast("打开串口失败")
                    }
                    viewModel.opened.value = opened
                }
            }

        }


        fun sendData() {
            val text = binding.etData.text.toString().trim()
            if (TextUtils.isEmpty(text) || text.length % 2 != 0) {
                showOneToast("无效数据")
                return
            }
            SpUtil.getDefault().saveString(PreferenceKeys.LSAT_SENT, text)
            SerialPortManager.instance().send(ByteUtil.hexStr2bytes(text))
        }

        fun loadCommands() {
            showOneToast("TODO")
        }

    }


}