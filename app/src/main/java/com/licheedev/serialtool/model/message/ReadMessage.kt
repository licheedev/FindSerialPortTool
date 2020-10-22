package com.licheedev.serialtool.model.message

import android.graphics.Color
import android.serialport.SerialPort
import com.licheedev.hwutils.ByteUtil
import com.licheedev.serialtool.util.TimeUtil


class ReadMessage(serialPort: SerialPort, byteArray: ByteArray) : IMessage {
    override val message: String =
        "${TimeUtil.currentTime()}|接收(${serialPort.device})\n${ByteUtil.bytes2HexStr(byteArray)}"
    override val color: Int = Color.parseColor("#48B424")
}