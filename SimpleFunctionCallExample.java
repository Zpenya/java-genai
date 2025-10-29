/*
 * 简单的Google Gen AI Java SDK Function Call示例
 * 
 * 这是一个入门级的示例，展示了最基本的函数调用功能
 * 
 * 使用方法：
 * 1. 设置环境变量: export GOOGLE_API_KEY=你的API密钥
 * 2. 编译: javac -cp ".:google-genai-1.23.0.jar" SimpleFunctionCallExample.java
 * 3. 运行: java -cp ".:google-genai-1.23.0.jar" SimpleFunctionCallExample
 */

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Tool;
import java.lang.reflect.Method;

public class SimpleFunctionCallExample {
    
    /**
     * 简单的加法函数
     * @param a 第一个数字
     * @param b 第二个数字
     * @return 两数之和
     */
    public static int add(int a, int b) {
        System.out.println("🧮 计算 " + a + " + " + b + " = " + (a + b));
        return a + b;
    }
    
    /**
     * 获取问候语
     * @param name 姓名
     * @return 问候语
     */
    public static String getGreeting(String name) {
        String greeting = "你好, " + name + "! 很高兴见到你!";
        System.out.println("👋 生成问候语: " + greeting);
        return greeting;
    }
    
    /**
     * 获取当前时间（简化版）
     * @return 当前时间字符串
     */
    public static String getTime() {
        String time = "现在是 " + new java.util.Date().toString();
        System.out.println("⏰ 获取时间: " + time);
        return time;
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("🚀 简单Function Call示例开始");
            System.out.println("================================");
            
            // 检查API密钥
            String apiKey = System.getenv("GOOGLE_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                System.err.println("❌ 请设置环境变量 GOOGLE_API_KEY");
                System.err.println("   命令: export GOOGLE_API_KEY=你的API密钥");
                return;
            }
            
            // 创建客户端
            Client client = new Client();
            System.out.println("✅ 客户端创建成功");
            
            // 获取函数方法的反射对象
            Method addMethod = SimpleFunctionCallExample.class.getMethod("add", int.class, int.class);
            Method greetingMethod = SimpleFunctionCallExample.class.getMethod("getGreeting", String.class);
            Method timeMethod = SimpleFunctionCallExample.class.getMethod("getTime");
            
            // 配置工具
            GenerateContentConfig config = GenerateContentConfig.builder()
                .tools(Tool.builder().functions(addMethod, greetingMethod, timeMethod))
                .build();
            
            System.out.println("✅ 工具配置完成");
            
            // 测试1: 基本计算
            System.out.println("\n--- 测试1: 基本计算 ---");
            String prompt1 = "请帮我计算 15 + 27 的结果";
            GenerateContentResponse response1 = client.models.generateContent("gemini-2.5-flash", prompt1, config);
            System.out.println("🤖 AI回复: " + response1.text());
            
            // 测试2: 问候语
            System.out.println("\n--- 测试2: 问候语 ---");
            String prompt2 = "请向张三问好";
            GenerateContentResponse response2 = client.models.generateContent("gemini-2.5-flash", prompt2, config);
            System.out.println("🤖 AI回复: " + response2.text());
            
            // 测试3: 综合测试
            System.out.println("\n--- 测试3: 综合测试 ---");
            String prompt3 = "请向李四问好，然后计算 100 + 200，最后告诉我现在的时间";
            GenerateContentResponse response3 = client.models.generateContent("gemini-2.5-flash", prompt3, config);
            System.out.println("🤖 AI回复: " + response3.text());
            
            // 显示函数调用历史
            System.out.println("\n📋 最后一次调用的函数历史:");
            System.out.println(response3.automaticFunctionCallingHistory().orElse("无函数调用"));
            
            System.out.println("\n✅ 示例运行完成！");
            
        } catch (NoSuchMethodException e) {
            System.err.println("❌ 方法未找到: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ 运行错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}