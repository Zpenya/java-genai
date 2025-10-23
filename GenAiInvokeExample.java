package com.google.genai;

import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Google Gen AI 调用示例
 * 展示如何使用 GenAiInvokeUtils 进行异步和同步调用
 */
public class GenAiInvokeExample {
    
    private static final Logger log = LoggerFactory.getLogger(GenAiInvokeExample.class);
    
    public static void main(String[] args) {
        // 创建客户端
        Client client = new Client();
        
        // 示例1: 异步流式调用
        asyncStreamExample(client);
        
        // 示例2: 异步非流式调用
        asyncNonStreamExample(client);
        
        // 示例3: 同步流式调用
        syncStreamExample(client);
        
        // 示例4: 同步非流式调用
        syncNonStreamExample(client);
        
        // 示例5: 使用 Content 对象的异步流式调用
        asyncStreamWithContentExample(client);
    }
    
    /**
     * 异步流式调用示例
     */
    private static void asyncStreamExample(Client client) {
        log.info("=== 异步流式调用示例 ===");
        
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        GenAiInvokeCallback callback = new GenAiInvokeCallback() {
            @Override
            public void onSuccess(GenerateContentResponse response) {
                System.out.print(response.text());
            }
            
            @Override
            public void onUnStreamEvent(GenerateContentResponse response) {
                // 流式调用不会触发此方法
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
                log.info("流式响应完成");
                isCompleted.set(true);
            }
            
            @Override
            public void onChatFailedEvent(String errorMessage) {
                log.error("调用失败: {}", errorMessage);
                isCompleted.set(true);
            }
        };
        
        // 异步流式调用
        GenAiInvokeUtils.genAiInvoke(
            client, 
            "gemini-2.5-flash", 
            "请写一个关于人工智能的短故事，大约100字", 
            null, 
            true, 
            isCompleted, 
            callback
        );
        
        // 等待完成
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
     * 异步非流式调用示例
     */
    private static void asyncNonStreamExample(Client client) {
        log.info("\n=== 异步非流式调用示例 ===");
        
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        GenAiInvokeCallback callback = new GenAiInvokeCallback() {
            @Override
            public void onSuccess(GenerateContentResponse response) {
                // 非流式调用不会触发此方法
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
                log.info("非流式响应完成");
                isCompleted.set(true);
            }
            
            @Override
            public void onChatFailedEvent(String errorMessage) {
                log.error("调用失败: {}", errorMessage);
                isCompleted.set(true);
            }
        };
        
        // 异步非流式调用
        GenAiInvokeUtils.genAiInvoke(
            client, 
            "gemini-2.5-flash", 
            "请解释什么是机器学习", 
            null, 
            false, 
            isCompleted, 
            callback
        );
        
        // 等待完成
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
     * 同步流式调用示例
     */
    private static void syncStreamExample(Client client) {
        log.info("\n=== 同步流式调用示例 ===");
        
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        GenAiInvokeCallback callback = new GenAiInvokeCallback() {
            @Override
            public void onSuccess(GenerateContentResponse response) {
                System.out.print(response.text());
            }
            
            @Override
            public void onUnStreamEvent(GenerateContentResponse response) {
                // 流式调用不会触发此方法
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
                log.info("同步流式响应完成");
                isCompleted.set(true);
            }
            
            @Override
            public void onChatFailedEvent(String errorMessage) {
                log.error("调用失败: {}", errorMessage);
                isCompleted.set(true);
            }
        };
        
        // 同步流式调用
        GenAiInvokeUtils.genAiInvokeSync(
            client, 
            "gemini-2.5-flash", 
            "请写一首关于春天的诗", 
            null, 
            true, 
            isCompleted, 
            callback
        );
    }
    
    /**
     * 同步非流式调用示例
     */
    private static void syncNonStreamExample(Client client) {
        log.info("\n=== 同步非流式调用示例 ===");
        
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        GenAiInvokeCallback callback = new GenAiInvokeCallback() {
            @Override
            public void onSuccess(GenerateContentResponse response) {
                // 非流式调用不会触发此方法
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
                log.info("同步非流式响应完成");
                isCompleted.set(true);
            }
            
            @Override
            public void onChatFailedEvent(String errorMessage) {
                log.error("调用失败: {}", errorMessage);
                isCompleted.set(true);
            }
        };
        
        // 同步非流式调用
        GenAiInvokeUtils.genAiInvokeSync(
            client, 
            "gemini-2.5-flash", 
            "请简单介绍一下量子计算", 
            null, 
            false, 
            isCompleted, 
            callback
        );
    }
    
    /**
     * 使用 Content 对象的异步流式调用示例
     */
    private static void asyncStreamWithContentExample(Client client) {
        log.info("\n=== 使用 Content 对象的异步流式调用示例 ===");
        
        // 创建多模态内容
        Content content = Content.fromParts(
            Part.fromText("请描述这张图片中的内容"),
            Part.fromUri("gs://path/to/image.jpg", "image/jpeg")
        );
        
        // 创建生成配置
        GenerateContentConfig config = GenerateContentConfig.builder()
            .maxOutputTokens(1024)
            .temperature(0.7f)
            .build();
        
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        GenAiInvokeCallback callback = new GenAiInvokeCallback() {
            @Override
            public void onSuccess(GenerateContentResponse response) {
                System.out.print(response.text());
            }
            
            @Override
            public void onUnStreamEvent(GenerateContentResponse response) {
                // 流式调用不会触发此方法
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
                log.info("多模态流式响应完成");
                isCompleted.set(true);
            }
            
            @Override
            public void onChatFailedEvent(String errorMessage) {
                log.error("调用失败: {}", errorMessage);
                isCompleted.set(true);
            }
        };
        
        // 使用 Content 对象的异步流式调用
        GenAiInvokeUtils.genAiInvoke(
            client, 
            "gemini-2.5-flash", 
            content, 
            config, 
            true, 
            isCompleted, 
            callback
        );
        
        // 等待完成
        while (!isCompleted.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}