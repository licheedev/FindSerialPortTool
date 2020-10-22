package com.licheedev.serialtool.model.message

import android.graphics.Color
import android.serialport.SerialPort
import com.licheedev.hwutils.ByteUtil
import com.licheedev.serialtool.util.TimeUtil


class OpenMessage(deviceList: List<SerialPort>) : IMessage {

    private val stringBuilder = StringBuilder()

    init {
        stringBuilder.append(TimeUtil.currentTime()).append("|打开串口")
        deviceList.forEach {
            stringBuilder.append("\n").append(it.device)
        }
    }


    override val message: String = stringBuilder.toString()
    override val color: Int = Color.parseColor("#158EE5")
}