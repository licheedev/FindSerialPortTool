package com.licheedev.serialtool.model.message

import android.graphics.Color
import com.licheedev.hwutils.ByteUtil
import com.licheedev.serialtool.util.TimeUtil


class SendMessage(byteArray: ByteArray) : IMessage {
    override val message: String =
        "${TimeUtil.currentTime()}|发送\n${ByteUtil.bytes2HexStr(byteArray)}"
    override val color: Int = Color.parseColor("#F23434")
}