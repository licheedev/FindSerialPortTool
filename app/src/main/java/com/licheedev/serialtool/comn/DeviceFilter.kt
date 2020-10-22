package com.licheedev.serialtool.comn

import java.io.File


interface DeviceFilter {
    
    /** 运行 */
    fun allow(device: String): Boolean


}