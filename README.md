# FindSerialPortTool
找哪个串口能用

TODO:加载命令列表只支持这种格式
```
AABBCCDDEEFF // 命令注释
```

配置打开串口过滤器
```
/** 设备过滤器 */
private val deviceFilter = object : DeviceFilter {

    override fun allow(device: String): Boolean {
        // 不允许打开usb的串口
        return !device.contains("usb", true)
    }

}
```

![1](https://raw.githubusercontent.com/licheedev/FindSerialPortTool/master/pics/1.png)