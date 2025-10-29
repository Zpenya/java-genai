/*
 * 完整的Function Call示例
 * 
 * 此示例展示了完整的function call流程：
 * 1. 定义函数并配置给模型
 * 2. 模型调用函数
 * 3. 执行函数（模拟）
 * 4. 返回结果给模型
 * 5. 模型生成最终答案
 * 
 * 使用方法：
 * 1. 设置环境变量: export GOOGLE_API_KEY=你的API密钥
 * 2. 编译: javac -cp ".:google-genai-1.23.0.jar" CompleteFunctionCallExample.java
 * 3. 运行: java -cp ".:google-genai-1.23.0.jar" CompleteFunctionCallExample
 */

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Tool;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CompleteFunctionCallExample {
    
    // 模拟数据库
    private static final Map<String, Map<String, Object>> userDatabase = new HashMap<>();
    private static final Map<String, String> weatherData = new HashMap<>();
    private static final Map<String, Double> stockPrices = new HashMap<>();
    
    static {
        // 初始化用户数据
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "张三");
        user1.put("age", 28);
        user1.put("email", "zhangsan@example.com");
        user1.put("city", "北京");
        user1.put("salary", 15000);
        userDatabase.put("001", user1);
        
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "李四");
        user2.put("age", 32);
        user2.put("email", "lisi@example.com");
        user2.put("city", "上海");
        user2.put("salary", 18000);
        userDatabase.put("002", user2);
        
        // 初始化天气数据
        weatherData.put("北京", "晴天，温度25°C，湿度60%");
        weatherData.put("上海", "多云，温度22°C，湿度70%");
        weatherData.put("广州", "小雨，温度28°C，湿度80%");
        weatherData.put("深圳", "晴天，温度30°C，湿度55%");
        
        // 初始化股票价格
        stockPrices.put("AAPL", 175.50);
        stockPrices.put("GOOGL", 142.30);
        stockPrices.put("MSFT", 378.85);
        stockPrices.put("TSLA", 248.42);
    }
    
    /**
     * 查询用户信息
     * @param userId 用户ID
     * @return 用户信息JSON字符串
     */
    public static String getUserInfo(String userId) {
        System.out.println("🔍 [函数执行] 查询用户信息: " + userId);
        
        // 模拟API调用延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Map<String, Object> user = userDatabase.get(userId);
        if (user == null) {
            System.out.println("❌ [函数执行] 用户不存在");
            return "{\"error\": \"用户不存在\", \"userId\": \"" + userId + "\"}";
        }
        
        // 构建JSON响应
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"userId\": \"").append(userId).append("\", ");
        json.append("\"name\": \"").append(user.get("name")).append("\", ");
        json.append("\"age\": ").append(user.get("age")).append(", ");
        json.append("\"email\": \"").append(user.get("email")).append("\", ");
        json.append("\"city\": \"").append(user.get("city")).append("\", ");
        json.append("\"salary\": ").append(user.get("salary"));
        json.append("}");
        
        System.out.println("✅ [函数执行] 用户信息查询完成");
        return json.toString();
    }
    
    /**
     * 查询天气信息
     * @param city 城市名称
     * @return 天气信息
     */
    public static String getWeather(String city) {
        System.out.println("🌤️ [函数执行] 查询天气: " + city);
        
        // 模拟API调用延迟
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String weather = weatherData.getOrDefault(city, "抱歉，暂时无法获取该城市的天气信息");
        System.out.println("✅ [函数执行] 天气查询完成: " + weather);
        return weather;
    }
    
    /**
     * 查询股票价格
     * @param symbol 股票代码
     * @return 股票价格信息
     */
    public static String getStockPrice(String symbol) {
        System.out.println("📈 [函数执行] 查询股票价格: " + symbol);
        
        // 模拟API调用延迟
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Double price = stockPrices.get(symbol.toUpperCase());
        if (price == null) {
            System.out.println("❌ [函数执行] 股票代码不存在");
            return "{\"error\": \"股票代码不存在\", \"symbol\": \"" + symbol + "\"}";
        }
        
        String result = "{\"symbol\": \"" + symbol.toUpperCase() + "\", \"price\": " + price + ", \"currency\": \"USD\"}";
        System.out.println("✅ [函数执行] 股票价格查询完成: $" + price);
        return result;
    }
    
    /**
     * 计算数学表达式
     * @param expression 数学表达式
     * @return 计算结果
     */
    public static String calculate(String expression) {
        System.out.println("🧮 [函数执行] 计算表达式: " + expression);
        
        // 模拟计算延迟
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        try {
            // 简单的数学表达式计算（仅支持基本运算）
            double result = evaluateExpression(expression);
            System.out.println("✅ [函数执行] 计算完成: " + result);
            return String.format("{\"expression\": \"%s\", \"result\": %.2f}", expression, result);
        } catch (Exception e) {
            System.out.println("❌ [函数执行] 计算错误: " + e.getMessage());
            return "{\"error\": \"计算错误\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }
    
    /**
     * 简单的表达式计算器
     */
    private static double evaluateExpression(String expression) {
        // 移除空格
        expression = expression.replaceAll("\\s+", "");
        
        // 简单的四则运算计算
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            return Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-");
            return Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            double divisor = Double.parseDouble(parts[1]);
            if (divisor == 0) {
                throw new ArithmeticException("除数不能为零");
            }
            return Double.parseDouble(parts[0]) / divisor;
        } else {
            return Double.parseDouble(expression);
        }
    }
    
    /**
     * 获取当前时间
     * @return 当前时间字符串
     */
    public static String getCurrentTime() {
        System.out.println("⏰ [函数执行] 获取当前时间");
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String time = new Date().toString();
        System.out.println("✅ [函数执行] 时间获取完成: " + time);
        return "{\"currentTime\": \"" + time + "\"}";
    }
    
    /**
     * 完整的Function Call流程示例
     */
    public static void completeFunctionCallFlow() throws NoSuchMethodException {
        System.out.println("🚀 开始完整的Function Call流程示例");
        System.out.println("================================================");
        
        // 1. 创建客户端
        Client client = new Client();
        System.out.println("✅ 步骤1: 客户端创建成功");
        
        // 2. 获取函数方法的反射对象
        Method getUserInfoMethod = CompleteFunctionCallExample.class.getMethod("getUserInfo", String.class);
        Method getWeatherMethod = CompleteFunctionCallExample.class.getMethod("getWeather", String.class);
        Method getStockPriceMethod = CompleteFunctionCallExample.class.getMethod("getStockPrice", String.class);
        Method calculateMethod = CompleteFunctionCallExample.class.getMethod("calculate", String.class);
        Method getTimeMethod = CompleteFunctionCallExample.class.getMethod("getCurrentTime");
        
        System.out.println("✅ 步骤2: 函数方法反射获取成功");
        
        // 3. 配置工具
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder().functions(
                getUserInfoMethod, 
                getWeatherMethod, 
                getStockPriceMethod, 
                calculateMethod, 
                getTimeMethod
            ))
            .build();
        
        System.out.println("✅ 步骤3: 工具配置完成");
        
        // 4. 发送请求给模型
        String userPrompt = "请帮我查询用户001的详细信息，然后查询他所在城市的天气，再查询苹果公司的股票价格，最后计算一下他的月薪除以30天是多少，并告诉我现在的时间。";
        
        System.out.println("\n📝 步骤4: 发送用户请求给模型");
        System.out.println("用户请求: " + userPrompt);
        
        GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", userPrompt, config);
        
        // 5. 显示模型响应
        System.out.println("\n🤖 步骤5: 模型生成最终答案");
        System.out.println("================================================");
        System.out.println("AI最终答案: " + response.text());
        
        // 6. 显示函数调用历史
        System.out.println("\n📋 步骤6: 函数调用历史");
        System.out.println("================================================");
        if (response.automaticFunctionCallingHistory().isPresent()) {
            System.out.println("函数调用历史: " + response.automaticFunctionCallingHistory().get());
        } else {
            System.out.println("无函数调用历史");
        }
        
        // 7. 显示函数调用详情
        System.out.println("\n🔧 步骤7: 函数调用详情");
        System.out.println("================================================");
        if (response.functionCalls().isPresent()) {
            System.out.println("函数调用详情: " + response.functionCalls().get());
        } else {
            System.out.println("无函数调用详情");
        }
    }
    
    /**
     * 分步骤的Function Call示例
     */
    public static void stepByStepExample() throws NoSuchMethodException {
        System.out.println("\n\n🔄 分步骤Function Call示例");
        System.out.println("================================================");
        
        Client client = new Client();
        
        // 配置工具
        Method getUserInfoMethod = CompleteFunctionCallExample.class.getMethod("getUserInfo", String.class);
        Method getWeatherMethod = CompleteFunctionCallExample.class.getMethod("getWeather", String.class);
        
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder().functions(getUserInfoMethod, getWeatherMethod))
            .build();
        
        // 第一轮：查询用户信息
        System.out.println("\n--- 第一轮：查询用户信息 ---");
        String prompt1 = "请查询用户001的信息";
        GenerateContentResponse response1 = client.models.generateContent("gemini-2.5-flash", prompt1, config);
        System.out.println("AI回复: " + response1.text());
        
        // 第二轮：基于用户信息查询天气
        System.out.println("\n--- 第二轮：查询天气 ---");
        String prompt2 = "这个用户住在哪个城市？请查询该城市的天气";
        GenerateContentResponse response2 = client.models.generateContent("gemini-2.5-flash", prompt2, config);
        System.out.println("AI回复: " + response2.text());
        
        // 第三轮：综合分析
        System.out.println("\n--- 第三轮：综合分析 ---");
        String prompt3 = "请综合分析用户001的信息和天气情况，给出生活建议";
        GenerateContentResponse response3 = client.models.generateContent("gemini-2.5-flash", prompt3, config);
        System.out.println("AI回复: " + response3.text());
    }
    
    /**
     * 错误处理示例
     */
    public static void errorHandlingExample() throws NoSuchMethodException {
        System.out.println("\n\n⚠️ 错误处理示例");
        System.out.println("================================================");
        
        Client client = new Client();
        
        Method getUserInfoMethod = CompleteFunctionCallExample.class.getMethod("getUserInfo", String.class);
        Method calculateMethod = CompleteFunctionCallExample.class.getMethod("calculate", String.class);
        
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(Tool.builder().functions(getUserInfoMethod, calculateMethod))
            .build();
        
        // 测试查询不存在的用户
        System.out.println("\n--- 测试1：查询不存在的用户 ---");
        String prompt1 = "请查询用户999的信息";
        GenerateContentResponse response1 = client.models.generateContent("gemini-2.5-flash", prompt1, config);
        System.out.println("AI回复: " + response1.text());
        
        // 测试除零错误
        System.out.println("\n--- 测试2：除零错误 ---");
        String prompt2 = "请计算10除以0的结果";
        GenerateContentResponse response2 = client.models.generateContent("gemini-2.5-flash", prompt2, config);
        System.out.println("AI回复: " + response2.text());
        
        // 测试无效表达式
        System.out.println("\n--- 测试3：无效表达式 ---");
        String prompt3 = "请计算无效的数学表达式";
        GenerateContentResponse response3 = client.models.generateContent("gemini-2.5-flash", prompt3, config);
        System.out.println("AI回复: " + response3.text());
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        try {
            System.out.println("🎯 Google Gen AI Java SDK 完整Function Call示例");
            System.out.println("================================================");
            
            // 检查API密钥
            String apiKey = System.getenv("GOOGLE_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                System.err.println("❌ 错误：请设置环境变量 GOOGLE_API_KEY");
                System.err.println("   例如：export GOOGLE_API_KEY=你的API密钥");
                return;
            }
            
            System.out.println("✅ API密钥已设置");
            
            // 运行完整流程示例
            completeFunctionCallFlow();
            
            // 运行分步骤示例
            stepByStepExample();
            
            // 运行错误处理示例
            errorHandlingExample();
            
            System.out.println("\n🎉 所有示例运行完成！");
            System.out.println("\n📊 总结：");
            System.out.println("- ✅ 函数定义和配置");
            System.out.println("- ✅ 模型调用函数");
            System.out.println("- ✅ 函数执行（模拟）");
            System.out.println("- ✅ 结果返回给模型");
            System.out.println("- ✅ 模型生成最终答案");
            System.out.println("- ✅ 错误处理");
            
        } catch (NoSuchMethodException e) {
            System.err.println("❌ 方法未找到错误: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ 运行错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}