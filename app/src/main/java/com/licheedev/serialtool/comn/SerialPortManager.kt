package com.licheedev.serialtool.comn

import android.serialport.SerialPort
import android.serialport.SerialPortFinder
import com.licheedev.serialtool.model.message.OpenMessage
import com.licheedev.serialtool.model.message.ReadMessage
import com.licheedev.serialtool.model.message.SendMessage
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentLinkedQueue


class SerialPortManager private constructor() {

    private var serialThread: SerialWorkThread? = null
        @Synchronized
        get
        @Synchronized
        set

    /**
     * 【同步】打开串口
     * @param baudrate 波特率
     * @return
     */
    @Synchronized
    fun open(baudrate: Int, filter: DeviceFilter? = null): Boolean {
        close()

        val serialPortFinder = SerialPortFinder()
        // 设备
        val mDevices = serialPortFinder.allDevicesPath
        val openedDevices = mutableListOf<SerialPort>()
        mDevices.forEach {
            try {

                val serialPort = if (filter != null) {
                    if (filter.allow(it)) SerialPort(File(it), baudrate) else null
                } else {
                    SerialPort(File(it), baudrate)
                }

                if (serialPort != null) {
                    openedDevices.add(serialPort)
                }

            } catch (e: Exception) {
            }
        }

        val workThread = if (openedDevices.size > 0) {

            // 发送消息
            EventBus.getDefault().post(OpenMessage(openedDevices))


            SerialWorkThread(openedDevices, object : SerialWorkThread.Handler {
                override fun handleRead(serial: SerialPort, byteArray: ByteArray) {
                    // 发送消息
                    EventBus.getDefault().post(ReadMessage(serial, byteArray))
                }

                override fun handleWrite(toWrite: ByteBuffer): Boolean {
                    if (sendQueue.isEmpty()) {
                        return false
                    }
                    // 把要发送的数据写入缓冲区
                    val poll = sendQueue.poll()
                    if (poll != null) {
                        toWrite.put(poll)
                        // 发送消息
                        EventBus.getDefault().post(SendMessage(poll))
                    }
                    return true
                }
            }).apply {
                start()
            }
        } else {
            null
        }

        serialThread = workThread

        return workThread != null
    }


    /** 发送队列 */
    private val sendQueue = ConcurrentLinkedQueue<ByteArray>()

    fun send(byteArray: ByteArray) {
        sendQueue.add(byteArray)
    }


    /**
     * 关闭串口
     */
    @Synchronized
    fun close() {
        serialThread?.close()
        sendQueue.clear()
    }


    companion object {
        private const val TAG = "SerialPortManager"

        private object InstanceHolder {
            var sManager = SerialPortManager()
        }

        fun instance(): SerialPortManager {
            return InstanceHolder.sManager
        }
    }
}