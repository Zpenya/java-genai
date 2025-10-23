package com.google.genai;

import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Content;
import com.google.genai.ResponseStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Google Gen AI 调用工具类
 * 提供异步和同步的流式调用方法
 */
public class GenAiInvokeUtils {
    
    private static final Logger log = LoggerFactory.getLogger(GenAiInvokeUtils.class);
    
    /**
     * 异步流式调用 Google Gen AI
     * 
     * @param client Google Gen AI 客户端
     * @param model 模型名称
     * @param content 输入内容
     * @param config 生成配置
     * @param stream 是否流式输出
     * @param isCompleted 完成标志
     * @param callback 回调接口
     */
    public static void genAiInvoke(Client client, String model, String content, 
            GenerateContentConfig config, boolean stream, AtomicBoolean isCompleted, 
            GenAiInvokeCallback callback) {
        
        long startTime = System.currentTimeMillis();
        
        if (stream) {
            // 异步流式调用
            CompletableFuture<ResponseStream<GenerateContentResponse>> responseStreamFuture =
                client.async.models.generateContentStream(model, content, config);
            
            responseStreamFuture
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
            CompletableFuture<GenerateContentResponse> responseFuture =
                client.async.models.generateContent(model, content, config);
            
            responseFuture
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
     * 异步流式调用 Google Gen AI (使用 Content 对象)
     * 
     * @param client Google Gen AI 客户端
     * @param model 模型名称
     * @param content 输入内容对象
     * @param config 生成配置
     * @param stream 是否流式输出
     * @param isCompleted 完成标志
     * @param callback 回调接口
     */
    public static void genAiInvoke(Client client, String model, Content content, 
            GenerateContentConfig config, boolean stream, AtomicBoolean isCompleted, 
            GenAiInvokeCallback callback) {
        
        long startTime = System.currentTimeMillis();
        
        if (stream) {
            // 异步流式调用
            CompletableFuture<ResponseStream<GenerateContentResponse>> responseStreamFuture =
                client.async.models.generateContentStream(model, content, config);
            
            responseStreamFuture
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
            CompletableFuture<GenerateContentResponse> responseFuture =
                client.async.models.generateContent(model, content, config);
            
            responseFuture
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
     * 同步流式调用 Google Gen AI
     * 
     * @param client Google Gen AI 客户端
     * @param model 模型名称
     * @param content 输入内容
     * @param config 生成配置
     * @param stream 是否流式输出
     * @param isCompleted 完成标志
     * @param callback 回调接口
     */
    public static void genAiInvokeSync(Client client, String model, String content, 
            GenerateContentConfig config, boolean stream, AtomicBoolean isCompleted, 
            GenAiInvokeCallback callback) {
        
        long startTime = System.currentTimeMillis();
        
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
     * 同步流式调用 Google Gen AI (使用 Content 对象)
     * 
     * @param client Google Gen AI 客户端
     * @param model 模型名称
     * @param content 输入内容对象
     * @param config 生成配置
     * @param stream 是否流式输出
     * @param isCompleted 完成标志
     * @param callback 回调接口
     */
    public static void genAiInvokeSync(Client client, String model, Content content, 
            GenerateContentConfig config, boolean stream, AtomicBoolean isCompleted, 
            GenAiInvokeCallback callback) {
        
        long startTime = System.currentTimeMillis();
        
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
}