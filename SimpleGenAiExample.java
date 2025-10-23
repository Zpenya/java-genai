package com.google.genai;

import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 简化的 Google Gen AI 调用示例
 * 更接近原有 Doubao 代码的风格
 */
public class SimpleGenAiExample {
    
    private static final Logger log = LoggerFactory.getLogger(SimpleGenAiExample.class);
    
    public static void main(String[] args) {
        // 创建客户端
        Client client = new Client();
        
        // 模拟 ChatHandler 的完成标志
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        
        // 示例1: 异步流式调用 (类似 doubaoInvoke)
        log.info("=== 异步流式调用 ===");
        genAiInvoke(client, "gemini-2.5-flash", "请写一个关于编程的短故事", 
                   null, true, isCompleted, new SimpleCallback());
        
        // 等待完成
        waitForCompletion(isCompleted);
        
        // 重置完成标志
        isCompleted.set(false);
        
        // 示例2: 同步流式调用 (类似 doubaoInvokeSync)
        log.info("\n=== 同步流式调用 ===");
        genAiInvokeSync(client, "gemini-2.5-flash", "请解释什么是人工智能", 
                       null, true, isCompleted, new SimpleCallback());
    }
    
    /**
     * 异步流式调用 (对应原来的 doubaoInvoke)
     */
    public static void genAiInvoke(Client client, String model, String content, 
            GenerateContentConfig config, boolean stream, AtomicBoolean isCompleted, 
            GenAiInvokeCallback callback) {
        
        log.info("开始异步流式调用，模型: {}, 流式: {}", model, stream);
        
        if (stream) {
            // 异步流式调用
            client.async.models.generateContentStream(model, content, config)
                .thenAccept(responseStream -> {
                    try {
                        boolean isFirst = true;
                        for (GenerateContentResponse response : responseStream) {
                            if (isCompleted.get()) {
                                log.debug("Stream is completed, stopping processing");
                                break;
                            }
                            
                            if (isFirst) {
                                callback.onTTFTTime(System.currentTimeMillis());
                                isFirst = false;
                            }
                            
                            log.debug("GenAI Stream Response: {}", response.text());
                            callback.onSuccess(response);
                        }
                        
                        callback.onTotalTime(System.currentTimeMillis());
                        callback.onSendCompleted();
                        
                    } catch (Exception e) {
                        log.error("Error processing stream response", e);
                        callback.onTotalTime(System.currentTimeMillis());
                        callback.onChatFailedEvent("处理流式响应失败: " + e.getMessage());
                    } finally {
                        try {
                            responseStream.close();
                        } catch (Exception e) {
                            log.warn("Error closing response stream", e);
                        }
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Async stream request failed", throwable);
                    callback.onTotalTime(System.currentTimeMillis());
                    callback.onChatFailedEvent("异步流式请求失败: " + throwable.getMessage());
                    return null;
                });
                
        } else {
            // 异步非流式调用
            client.async.models.generateContent(model, content, config)
                .thenAccept(response -> {
                    try {
                        log.debug("GenAI Response: {}", response.text());
                        callback.onTTFTTime(System.currentTimeMillis());
                        callback.onUnStreamEvent(response);
                        callback.onTotalTime(System.currentTimeMillis());
                        callback.onSendCompleted();
                        
                    } catch (Exception e) {
                        log.error("Error processing response", e);
                        callback.onTotalTime(System.currentTimeMillis());
                        callback.onChatFailedEvent("处理响应失败: " + e.getMessage());
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Async request failed", throwable);
                    callback.onTotalTime(System.currentTimeMillis());
                    callback.onChatFailedEvent("异步请求失败: " + throwable.getMessage());
                    return null;
                });
        }
    }
    
    /**
     * 同步流式调用 (对应原来的 doubaoInvokeSync)
     */
    public static void genAiInvokeSync(Client client, String model, String content, 
            GenerateContentConfig config, boolean stream, AtomicBoolean isCompleted, 
            GenAiInvokeCallback callback) {
        
        log.info("开始同步流式调用，模型: {}, 流式: {}", model, stream);
        
        try {
            if (stream) {
                // 同步流式调用
                ResponseStream<GenerateContentResponse> responseStream = 
                    client.models.generateContentStream(model, content, config);
                
                try {
                    boolean isFirst = true;
                    for (GenerateContentResponse response : responseStream) {
                        if (isCompleted.get()) {
                            log.debug("Stream is completed, stopping processing");
                            break;
                        }
                        
                        if (isFirst) {
                            callback.onTTFTTime(System.currentTimeMillis());
                            isFirst = false;
                        }
                        
                        log.debug("GenAI Stream Response: {}", response.text());
                        callback.onSuccess(response);
                    }
                    
                    callback.onTotalTime(System.currentTimeMillis());
                    callback.onSendCompleted();
                    
                } finally {
                    try {
                        responseStream.close();
                    } catch (Exception e) {
                        log.warn("Error closing response stream", e);
                    }
                }
                
            } else {
                // 同步非流式调用
                GenerateContentResponse response = 
                    client.models.generateContent(model, content, config);
                
                log.debug("GenAI Response: {}", response.text());
                callback.onTTFTTime(System.currentTimeMillis());
                callback.onUnStreamEvent(response);
                callback.onTotalTime(System.currentTimeMillis());
                callback.onSendCompleted();
            }
            
        } catch (Exception e) {
            log.error("Sync request failed", e);
            callback.onTotalTime(System.currentTimeMillis());
            callback.onChatFailedEvent("同步请求失败: " + e.getMessage());
        }
    }
    
    /**
     * 等待完成
     */
    private static void waitForCompletion(AtomicBoolean isCompleted) {
        while (!isCompleted.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    /**
     * 简单的回调实现
     */
    private static class SimpleCallback implements GenAiInvokeCallback {
        @Override
        public void onSuccess(GenerateContentResponse response) {
            System.out.print(response.text());
        }
        
        @Override
        public void onUnStreamEvent(GenerateContentResponse response) {
            System.out.println("完整响应: " + response.text());
        }
        
        @Override
        public void onTTFTTime(long timestamp) {
            log.info("首次响应时间: {}ms", timestamp);
        }
        
        @Override
        public void onTotalTime(long timestamp) {
            log.info("总响应时间: {}ms", timestamp);
        }
        
        @Override
        public void onSendCompleted() {
            log.info("响应完成");
        }
        
        @Override
        public void onChatFailedEvent(String errorMessage) {
            log.error("调用失败: {}", errorMessage);
        }
    }
}