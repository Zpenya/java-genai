/*
 * 使用ToolCall的完整Function Call示例
 * 
 * 此示例展示了使用ToolCall处理函数调用的完整流程：
 * 1. 定义函数并配置给模型
 * 2. 模型返回ToolCall对象
 * 3. 解析ToolCall并执行相应函数
 * 4. 将函数结果返回给模型
 * 5. 模型生成最终答案
 * 
 * 使用方法：
 * 1. 设置环境变量: export GOOGLE_API_KEY=你的API密钥
 * 2. 编译: javac -cp ".:google-genai-1.23.0.jar" ToolCallCompleteExample.java
 * 3. 运行: java -cp ".:google-genai-1.23.0.jar" ToolCallCompleteExample
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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.*;

public class ToolCallCompleteExample {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // 模拟数据
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
     * 查询用户信息函数
     */
    public static String getUserInfo(String userId) {
        System.out.println("🔍 [函数执行] 查询用户信息: " + userId);
        
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
     * 查询天气信息函数
     */
    public static String getWeather(String city) {
        System.out.println("🌤️ [函数执行] 查询天气: " + city);
        
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
     * 查询股票价格函数
     */
    public static String getStockPrice(String symbol) {
        System.out.println("📈 [函数执行] 查询股票价格: " + symbol);
        
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
     * 计算数学表达式函数
     */
    public static String calculate(String expression) {
        System.out.println("🧮 [函数执行] 计算表达式: " + expression);
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        try {
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
        expression = expression.replaceAll("\\s+", "");
        
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
     * 处理ToolCall并执行相应函数
     */
    public static String processToolCall(FunctionCall functionCall) {
        String functionName = functionCall.name();
        Map<String, Object> arguments = functionCall.args();
        
        System.out.println("🔧 [ToolCall处理] 函数名: " + functionName);
        System.out.println("🔧 [ToolCall处理] 参数: " + arguments);
        
        try {
            switch (functionName) {
                case "getUserInfo":
                    String userId = (String) arguments.get("userId");
                    return getUserInfo(userId);
                    
                case "getWeather":
                    String city = (String) arguments.get("city");
                    return getWeather(city);
                    
                case "getStockPrice":
                    String symbol = (String) arguments.get("symbol");
                    return getStockPrice(symbol);
                    
                case "calculate":
                    String expression = (String) arguments.get("expression");
                    return calculate(expression);
                    
                default:
                    System.out.println("❌ [ToolCall处理] 未知函数: " + functionName);
                    return "{\"error\": \"未知函数\", \"functionName\": \"" + functionName + "\"}";
            }
        } catch (Exception e) {
            System.out.println("❌ [ToolCall处理] 函数执行错误: " + e.getMessage());
            return "{\"error\": \"函数执行错误\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }
    
    /**
     * 使用ToolCall的完整流程
     */
    public static void completeToolCallFlow() throws Exception {
        System.out.println("🚀 使用ToolCall的完整Function Call流程");
        System.out.println("================================================");
        
        // 步骤1: 创建客户端
        Client client = new Client();
        System.out.println("✅ 步骤1: 客户端创建成功");
        
        // 步骤2: 定义函数声明
        System.out.println("\n🔧 步骤2: 定义函数声明");
        
        // 用户信息查询函数
        ImmutableMap<String, Object> userInfoParams = ImmutableMap.of(
            "type", "object",
            "properties", ImmutableMap.of(
                "userId", ImmutableMap.of("type", "string", "description", "用户ID")
            ),
            "required", ImmutableList.of("userId")
        );
        
        // 天气查询函数
        ImmutableMap<String, Object> weatherParams = ImmutableMap.of(
            "type", "object",
            "properties", ImmutableMap.of(
                "city", ImmutableMap.of("type", "string", "description", "城市名称")
            ),
            "required", ImmutableList.of("city")
        );
        
        // 股票价格查询函数
        ImmutableMap<String, Object> stockParams = ImmutableMap.of(
            "type", "object",
            "properties", ImmutableMap.of(
                "symbol", ImmutableMap.of("type", "string", "description", "股票代码")
            ),
            "required", ImmutableList.of("symbol")
        );
        
        // 计算函数
        ImmutableMap<String, Object> calcParams = ImmutableMap.of(
            "type", "object",
            "properties", ImmutableMap.of(
                "expression", ImmutableMap.of("type", "string", "description", "数学表达式")
            ),
            "required", ImmutableList.of("expression")
        );
        
        // 创建函数声明
        Tool userInfoTool = Tool.builder()
            .functionDeclarations(
                com.google.genai.types.FunctionDeclaration.builder()
                    .name("getUserInfo")
                    .description("查询用户详细信息")
                    .parametersJsonSchema(userInfoParams)
                    .build()
            )
            .build();
        
        Tool weatherTool = Tool.builder()
            .functionDeclarations(
                com.google.genai.types.FunctionDeclaration.builder()
                    .name("getWeather")
                    .description("查询指定城市的天气信息")
                    .parametersJsonSchema(weatherParams)
                    .build()
            )
            .build();
        
        Tool stockTool = Tool.builder()
            .functionDeclarations(
                com.google.genai.types.FunctionDeclaration.builder()
                    .name("getStockPrice")
                    .description("查询股票价格信息")
                    .parametersJsonSchema(stockParams)
                    .build()
            )
            .build();
        
        Tool calcTool = Tool.builder()
            .functionDeclarations(
                com.google.genai.types.FunctionDeclaration.builder()
                    .name("calculate")
                    .description("计算数学表达式")
                    .parametersJsonSchema(calcParams)
                    .build()
            )
            .build();
        
        System.out.println("✅ 函数声明创建完成");
        
        // 步骤3: 配置工具
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(userInfoTool, weatherTool, stockTool, calcTool)
            .build();
        
        System.out.println("✅ 步骤3: 工具配置完成");
        
        // 步骤4: 发送请求
        String userPrompt = "请查询用户001的详细信息，然后查询他所在城市的天气，再查询苹果公司的股票价格，最后计算一下他的月薪除以30天是多少。";
        
        System.out.println("\n📤 步骤4: 发送请求给模型");
        System.out.println("用户请求: " + userPrompt);
        
        GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", userPrompt, config);
        
        // 步骤5: 处理ToolCall
        System.out.println("\n🔧 步骤5: 处理ToolCall");
        System.out.println("================================================");
        
        if (response.functionCalls().isPresent()) {
            List<FunctionCall> functionCalls = response.functionCalls().get();
            System.out.println("检测到 " + functionCalls.size() + " 个函数调用");
            
            // 执行每个函数调用
            List<Content> functionResults = new ArrayList<>();
            
            for (int i = 0; i < functionCalls.size(); i++) {
                FunctionCall functionCall = functionCalls.get(i);
                System.out.println("\n--- 处理函数调用 " + (i + 1) + " ---");
                
                // 执行函数
                String functionResult = processToolCall(functionCall);
                
                // 创建函数结果内容
                Content functionResultContent = Content.fromParts(
                    Part.fromText("函数 " + functionCall.name() + " 的执行结果: " + functionResult)
                );
                functionResults.add(functionResultContent);
            }
            
            // 步骤6: 将函数结果发送回模型
            System.out.println("\n📤 步骤6: 将函数结果发送回模型");
            System.out.println("================================================");
            
            // 构建包含函数结果的对话
            List<Content> conversation = new ArrayList<>();
            
            // 添加用户原始请求
            conversation.add(Content.fromParts(Part.fromText(userPrompt)));
            
            // 添加函数调用结果
            conversation.addAll(functionResults);
            
            // 添加请求最终答案的提示
            conversation.add(Content.fromParts(Part.fromText("请基于以上函数执行结果，生成一个完整的回答。")));
            
            // 发送给模型生成最终答案
            GenerateContentResponse finalResponse = client.models.generateContent(
                "gemini-2.5-flash", 
                conversation, 
                config
            );
            
            // 步骤7: 显示最终答案
            System.out.println("\n🎯 步骤7: 最终答案");
            System.out.println("================================================");
            System.out.println("AI最终答案: " + finalResponse.text());
            
        } else {
            System.out.println("❌ 没有检测到函数调用");
            System.out.println("模型直接回复: " + response.text());
        }
        
        System.out.println("\n✅ 完整ToolCall流程执行成功！");
    }
    
    /**
     * 简化的ToolCall示例
     */
    public static void simpleToolCallExample() throws Exception {
        System.out.println("\n\n🔄 简化ToolCall示例");
        System.out.println("================================================");
        
        Client client = new Client();
        
        // 定义简单的天气查询函数
        ImmutableMap<String, Object> weatherParams = ImmutableMap.of(
            "type", "object",
            "properties", ImmutableMap.of(
                "city", ImmutableMap.of("type", "string", "description", "城市名称")
            ),
            "required", ImmutableList.of("city")
        );
        
        Tool weatherTool = Tool.builder()
            .functionDeclarations(
                com.google.genai.types.FunctionDeclaration.builder()
                    .name("getWeather")
                    .description("查询指定城市的天气信息")
                    .parametersJsonSchema(weatherParams)
                    .build()
            )
            .build();
        
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(weatherTool)
            .build();
        
        // 发送请求
        String userPrompt = "请查询北京的天气";
        System.out.println("用户请求: " + userPrompt);
        
        GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", userPrompt, config);
        
        // 处理ToolCall
        if (response.functionCalls().isPresent()) {
            List<FunctionCall> functionCalls = response.functionCalls().get();
            
            for (FunctionCall functionCall : functionCalls) {
                System.out.println("\n🔧 处理函数调用: " + functionCall.name());
                System.out.println("参数: " + functionCall.args());
                
                // 执行函数
                String result = processToolCall(functionCall);
                System.out.println("函数执行结果: " + result);
                
                // 将结果发送回模型
                List<Content> conversation = ImmutableList.of(
                    Content.fromParts(Part.fromText(userPrompt)),
                    Content.fromParts(Part.fromText("函数执行结果: " + result)),
                    Content.fromParts(Part.fromText("请基于以上结果生成回答"))
                );
                
                GenerateContentResponse finalResponse = client.models.generateContent(
                    "gemini-2.5-flash", 
                    conversation, 
                    config
                );
                
                System.out.println("\n🎯 最终答案: " + finalResponse.text());
            }
        } else {
            System.out.println("模型直接回复: " + response.text());
        }
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        try {
            System.out.println("🎯 使用ToolCall的完整Function Call示例");
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
            completeToolCallFlow();
            
            // 运行简化示例
            simpleToolCallExample();
            
            System.out.println("\n🎉 所有ToolCall示例运行完成！");
            System.out.println("\n📊 总结：");
            System.out.println("- ✅ 使用FunctionDeclaration定义函数");
            System.out.println("- ✅ 模型返回ToolCall对象");
            System.out.println("- ✅ 解析ToolCall并执行函数");
            System.out.println("- ✅ 将函数结果返回给模型");
            System.out.println("- ✅ 模型生成最终答案");
            
        } catch (Exception e) {
            System.err.println("❌ 运行错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}