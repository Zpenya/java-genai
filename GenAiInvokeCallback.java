package com.google.genai;

import com.google.genai.types.GenerateContentResponse;

/**
 * Google Gen AI 调用回调接口
 * 适配原有的 DoubaoInvokeCallback 接口设计
 */
public interface GenAiInvokeCallback {
    
    /**
     * 流式响应成功回调
     * @param response 生成内容响应
     */
    void onSuccess(GenerateContentResponse response);
    
    /**
     * 非流式响应回调
     * @param response 生成内容响应
     */
    void onUnStreamEvent(GenerateContentResponse response);
    
    /**
     * 首次响应时间回调 (Time To First Token)
     * @param timestamp 时间戳
     */
    void onTTFTTime(long timestamp);
    
    /**
     * 总响应时间回调
     * @param timestamp 时间戳
     */
    void onTotalTime(long timestamp);
    
    /**
     * 发送完成回调
     */
    void onSendCompleted();
    
    /**
     * 聊天失败事件回调
     * @param errorMessage 错误信息
     */
    void onChatFailedEvent(String errorMessage);
}