package com.licheedev.serialtool.model.message

/**
 * 日志消息数据接口
 */
interface IMessage {
    
    /**
     * 消息文本
     *
     * @return
     */
    val message: String

    /**
     * 消息颜色
     *
     * @return
     */
    val color: Int
}