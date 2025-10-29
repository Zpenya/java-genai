/*
 * 使用Google Gen AI Java SDK进行Function Call的完整示例
 * 
 * 此示例展示了如何使用Google Gen AI Java SDK进行函数调用，包括：
 * 1. 基本的函数调用
 * 2. 聊天会话中的函数调用
 * 3. 错误处理
 * 4. 多种类型的函数（天气查询、计算器、数据库模拟等）
 * 
 * 使用方法：
 * 1. 设置环境变量 GOOGLE_API_KEY=你的API密钥
 * 2. 编译并运行：javac -cp ".:google-genai-1.23.0.jar" FunctionCallExample.java
 * 3. 运行：java -cp ".:google-genai-1.23.0.jar" FunctionCallExample
 */

import com.google.genai.Client;
import com.google.genai.Chat;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Tool;
import java.lang.reflect.Method;
import java.util.*;

public class FunctionCallExample {
    
    // 模拟天气数据
    private static final Map<String, String> weatherData = new HashMap<>();
    static {
        weatherData.put("北京", "晴天，温度25°C");
        weatherData.put("上海", "多云，温度22°C");
        weatherData.put("广州", "小雨，温度28°C");
        weatherData.put("深圳", "晴天，温度30°C");
        weatherData.put("杭州", "阴天，温度20°C");
    }
    
    // 模拟用户数据库
    private static final Map<String, Map<String, Object>> userDatabase = new HashMap<>();
    static {
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "张三");
        user1.put("age", 25);
        user1.put("email", "zhangsan@example.com");
        user1.put("city", "北京");
        userDatabase.put("001", user1);
        
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "李四");
        user2.put("age", 30);
        user2.put("email", "lisi@example.com");
        user2.put("city", "上海");
        userDatabase.put("002", user2);
    }
    
    /**
     * 获取指定城市的天气信息
     * @param city 城市名称
     * @return 天气信息字符串
     */
    public static String getWeather(String city) {
        System.out.println("🌤️  正在查询 " + city + " 的天气...");
        
        // 模拟API调用延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String weather = weatherData.getOrDefault(city, "抱歉，暂时无法获取该城市的天气信息");
        System.out.println("✅ 天气查询完成: " + weather);
        return weather;
    }
    
    /**
     * 计算两个数字的基本运算
     * @param a 第一个数字
     * @param b 第二个数字
     * @param operation 运算类型 (add, subtract, multiply, divide)
     * @return 计算结果
     */
    public static String calculate(double a, double b, String operation) {
        System.out.println("🧮 正在计算 " + a + " " + operation + " " + b + "...");
        
        try {
            Thread.sleep(500); // 模拟计算延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        double result;
        switch (operation.toLowerCase()) {
            case "add":
            case "加法":
                result = a + b;
                break;
            case "subtract":
            case "减法":
                result = a - b;
                break;
            case "multiply":
            case "乘法":
                result = a * b;
                break;
            case "divide":
            case "除法":
                if (b == 0) {
                    return "错误：除数不能为零";
                }
                result = a / b;
                break;
            default:
                return "错误：不支持的运算类型";
        }
        
        String resultStr = String.format("%.2f", result);
        System.out.println("✅ 计算完成: " + resultStr);
        return resultStr;
    }
    
    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息JSON字符串
     */
    public static String getUserInfo(String userId) {
        System.out.println("👤 正在查询用户ID: " + userId + " 的信息...");
        
        try {
            Thread.sleep(800); // 模拟数据库查询延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Map<String, Object> user = userDatabase.get(userId);
        if (user == null) {
            System.out.println("❌ 用户不存在");
            return "{\"error\": \"用户不存在\"}";
        }
        
        // 将Map转换为JSON格式的字符串
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\": \"").append(userId).append("\", ");
        json.append("\"name\": \"").append(user.get("name")).append("\", ");
        json.append("\"age\": ").append(user.get("age")).append(", ");
        json.append("\"email\": \"").append(user.get("email")).append("\", ");
        json.append("\"city\": \"").append(user.get("city")).append("\"");
        json.append("}");
        
        System.out.println("✅ 用户信息查询完成");
        return json.toString();
    }
    
    /**
     * 获取当前时间（模拟系统函数）
     * @return 当前时间字符串
     */
    public static String getCurrentTime() {
        System.out.println("⏰ 正在获取当前时间...");
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String time = new Date().toString();
        System.out.println("✅ 当前时间: " + time);
        return time;
    }
    
    /**
     * 基本的函数调用示例
     */
    public static void basicFunctionCallExample() throws NoSuchMethodException {
        System.out.println("\n=== 基本函数调用示例 ===");
        
        Client client = new Client();
        
        // 获取函数方法的反射对象
        Method weatherMethod = FunctionCallExample.class.getMethod("getWeather", String.class);
        Method calcMethod = FunctionCallExample.class.getMethod("calculate", double.class, double.class, String.class);
        Method timeMethod = FunctionCallExample.class.getMethod("getCurrentTime");
        
        // 配置工具
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder().functions(weatherMethod, calcMethod, timeMethod))
            .build();
        
        // 发送请求
        String prompt = "请帮我查询北京的天气，然后计算15乘以8的结果，最后告诉我现在的时间。";
        GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, config);
        
        System.out.println("\n🤖 AI回复: " + response.text());
        System.out.println("\n📋 函数调用历史: " + response.automaticFunctionCallingHistory().orElse("无"));
    }
    
    /**
     * 聊天会话中的函数调用示例
     */
    public static void chatFunctionCallExample() throws NoSuchMethodException {
        System.out.println("\n=== 聊天会话函数调用示例 ===");
        
        Client client = new Client();
        
        // 获取函数方法
        Method weatherMethod = FunctionCallExample.class.getMethod("getWeather", String.class);
        Method userMethod = FunctionCallExample.class.getMethod("getUserInfo", String.class);
        Method calcMethod = FunctionCallExample.class.getMethod("calculate", double.class, double.class, String.class);
        
        // 配置工具
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder().functions(weatherMethod, userMethod, calcMethod))
            .build();
        
        // 创建聊天会话
        Chat chat = client.chats.create("gemini-2.5-flash", config);
        
        // 第一轮对话
        System.out.println("\n👤 用户: 你好，我想查询用户001的信息");
        GenerateContentResponse response1 = chat.sendMessage("你好，我想查询用户001的信息");
        System.out.println("🤖 AI: " + response1.text());
        
        // 第二轮对话
        System.out.println("\n👤 用户: 这个用户住在哪个城市？天气怎么样？");
        GenerateContentResponse response2 = chat.sendMessage("这个用户住在哪个城市？天气怎么样？");
        System.out.println("🤖 AI: " + response2.text());
        
        // 第三轮对话
        System.out.println("\n👤 用户: 帮我计算一下这个用户的年龄乘以2是多少");
        GenerateContentResponse response3 = chat.sendMessage("帮我计算一下这个用户的年龄乘以2是多少");
        System.out.println("🤖 AI: " + response3.text());
        
        // 显示完整聊天历史
        System.out.println("\n📜 完整聊天历史:");
        System.out.println(chat.getHistory(true));
    }
    
    /**
     * 错误处理示例
     */
    public static void errorHandlingExample() throws NoSuchMethodException {
        System.out.println("\n=== 错误处理示例 ===");
        
        Client client = new Client();
        
        Method calcMethod = FunctionCallExample.class.getMethod("calculate", double.class, double.class, String.class);
        Method userMethod = FunctionCallExample.class.getMethod("getUserInfo", String.class);
        
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder().functions(calcMethod, userMethod))
            .build();
        
        // 测试除零错误
        System.out.println("\n🧪 测试除零错误:");
        String prompt1 = "请计算10除以0的结果";
        GenerateContentResponse response1 = client.models.generateContent("gemini-2.5-flash", prompt1, config);
        System.out.println("🤖 AI: " + response1.text());
        
        // 测试不存在的用户
        System.out.println("\n🧪 测试查询不存在的用户:");
        String prompt2 = "请查询用户999的信息";
        GenerateContentResponse response2 = client.models.generateContent("gemini-2.5-flash", prompt2, config);
        System.out.println("🤖 AI: " + response2.text());
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Google Gen AI Java SDK Function Call 示例");
            System.out.println("================================================");
            
            // 检查API密钥
            String apiKey = System.getenv("GOOGLE_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                System.err.println("❌ 错误：请设置环境变量 GOOGLE_API_KEY");
                System.err.println("   例如：export GOOGLE_API_KEY=你的API密钥");
                return;
            }
            
            // 运行各种示例
            basicFunctionCallExample();
            chatFunctionCallExample();
            errorHandlingExample();
            
            System.out.println("\n✅ 所有示例运行完成！");
            
        } catch (NoSuchMethodException e) {
            System.err.println("❌ 方法未找到错误: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ 运行错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}