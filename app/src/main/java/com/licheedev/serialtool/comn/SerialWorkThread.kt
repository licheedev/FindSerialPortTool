package com.licheedev.serialtool.comn

import android.os.Handler
import android.os.SystemClock
import android.serialport.SerialPort
import com.licheedev.hwutils.SystemClockEx
import com.licheedev.myutils.LogPlus
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 读写串口线程
 */
class SerialWorkThread(val serialPorts: List<SerialPort>, val handler: Handler) : Thread() {

    private val running = AtomicBoolean(true)
    private val received = ByteArray(2048)
    private val writeBuffer = ByteBuffer.allocate(2048)


    override fun run() {
        LogPlus.e("开始串口工作线程")
        while (running.get()) {
            // 处理读
            serialRead()
            if (!running.get()) {
                break
            }
            serialWrite()
            // 处理写
        }
        LogPlus.e("结束串口工作线程")
    }

    /** 读串口数据 */
    private fun serialRead() {

        serialPorts.forEach {

            if (!running.get()) {
                return
            }

            try {
                while (running.get() && it.inputStream.available() > 0) {
                    val read = it.inputStream.read(received)
                    if (read > 0) {
                        handler.handleRead(it, received.copyOfRange(0, read))
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun serialWrite() {
        writeBuffer.clear()
        if (handler.handleWrite(writeBuffer)) {
            writeBuffer.flip() // 切换到读模式
            val toWrite = ByteArray(writeBuffer.remaining())
            writeBuffer.get(toWrite)
            serialPorts.forEach {

                if (!running.get()) {
                    return
                }

                
                LogPlus.e("要写到${it.device}")
                
                try {
                    it.outputStream.write(toWrite)
                    it.outputStream.flush()
                } catch (e: Exception) {
                }

                LogPlus.e("写完了${it.device}")
                
                SystemClockEx.sleep(1L)
            }
        } else {
            SystemClockEx.sleep(1L)
        }
    }


    /**
     * 停止读线程
     */
    fun close() {
        running.set(false)
        this.interrupt()
        serialPorts.forEach {
            closeSerial(it)
        }
    }

    /** 关闭串口 */
    private fun closeSerial(serial: SerialPort) {
        try {
            serial.inputStream.close()
        } catch (e: Exception) {
        }
        try {
            serial.outputStream.close()
        } catch (e: Exception) {
        }
        try {
            serial.close()
        } catch (e: Exception) {
        }
    }

    interface Handler {
        fun handleRead(serial: SerialPort, byteArray: ByteArray)

        fun handleWrite(toWrite: ByteBuffer): Boolean
    }

}