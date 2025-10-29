/*
 * 简化的完整Function Call示例
 * 
 * 此示例展示了Function Call的核心流程：
 * 1. 定义函数 → 2. 模型调用 → 3. 函数执行 → 4. 返回结果 → 5. 最终答案
 * 
 * 使用方法：
 * 1. 设置环境变量: export GOOGLE_API_KEY=你的API密钥
 * 2. 编译: javac -cp ".:google-genai-1.23.0.jar" SimpleCompleteExample.java
 * 3. 运行: java -cp ".:google-genai-1.23.0.jar" SimpleCompleteExample
 */

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Tool;
import java.lang.reflect.Method;

public class SimpleCompleteExample {
    
    // 模拟数据
    private static final String[] cities = {"北京", "上海", "广州", "深圳"};
    private static final String[] weathers = {"晴天", "多云", "小雨", "阴天"};
    private static final int[] temperatures = {25, 22, 28, 30};
    
    /**
     * 步骤1: 定义函数 - 获取天气信息
     * @param city 城市名称
     * @return 天气信息
     */
    public static String getWeather(String city) {
        System.out.println("🌤️  [函数执行] 查询 " + city + " 的天气...");
        
        // 模拟API调用延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 模拟天气查询
        int index = Math.abs(city.hashCode()) % cities.length;
        String weather = weathers[index];
        int temp = temperatures[index];
        
        String result = city + "今天" + weather + "，温度" + temp + "°C";
        System.out.println("✅ [函数执行] 查询结果: " + result);
        
        return result;
    }
    
    /**
     * 步骤1: 定义函数 - 计算数学表达式
     * @param expression 数学表达式
     * @return 计算结果
     */
    public static String calculate(String expression) {
        System.out.println("🧮 [函数执行] 计算: " + expression);
        
        // 模拟计算延迟
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        try {
            // 简单的计算逻辑
            double result = evaluateSimpleExpression(expression);
            System.out.println("✅ [函数执行] 计算结果: " + result);
            return String.valueOf(result);
        } catch (Exception e) {
            System.out.println("❌ [函数执行] 计算错误: " + e.getMessage());
            return "计算错误: " + e.getMessage();
        }
    }
    
    /**
     * 简单的表达式计算
     */
    private static double evaluateSimpleExpression(String expression) {
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
     * 完整的Function Call流程
     */
    public static void main(String[] args) {
        try {
            System.out.println("🚀 完整Function Call流程示例");
            System.out.println("================================================");
            
            // 检查API密钥
            String apiKey = System.getenv("GOOGLE_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                System.err.println("❌ 请设置环境变量 GOOGLE_API_KEY");
                return;
            }
            
            // 步骤1: 创建客户端
            System.out.println("📱 步骤1: 创建客户端");
            Client client = new Client();
            System.out.println("✅ 客户端创建成功");
            
            // 步骤2: 获取函数方法的反射对象
            System.out.println("\n🔧 步骤2: 配置函数");
            Method weatherMethod = SimpleCompleteExample.class.getMethod("getWeather", String.class);
            Method calcMethod = SimpleCompleteExample.class.getMethod("calculate", String.class);
            System.out.println("✅ 函数方法获取成功");
            
            // 步骤3: 配置工具
            GenerateContentConfig config = GenerateContentConfig.builder()
                .tools(Tool.builder().functions(weatherMethod, calcMethod))
                .build();
            System.out.println("✅ 工具配置完成");
            
            // 步骤4: 发送请求给模型
            System.out.println("\n📤 步骤3: 发送请求给模型");
            String userPrompt = "请查询北京的天气，然后计算15乘以8的结果";
            System.out.println("用户请求: " + userPrompt);
            
            // 步骤5: 模型调用函数并生成响应
            System.out.println("\n🤖 步骤4: 模型处理请求");
            System.out.println("----------------------------------------");
            System.out.println("模型正在分析请求并决定调用哪些函数...");
            
            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", userPrompt, config);
            
            // 步骤6: 显示最终结果
            System.out.println("\n📋 步骤5: 显示最终结果");
            System.out.println("================================================");
            System.out.println("🎯 最终答案: " + response.text());
            
            // 显示函数调用历史
            System.out.println("\n📊 函数调用历史:");
            if (response.automaticFunctionCallingHistory().isPresent()) {
                System.out.println(response.automaticFunctionCallingHistory().get());
            } else {
                System.out.println("无函数调用历史");
            }
            
            // 显示函数调用详情
            System.out.println("\n🔍 函数调用详情:");
            if (response.functionCalls().isPresent()) {
                System.out.println(response.functionCalls().get());
            } else {
                System.out.println("无函数调用详情");
            }
            
            System.out.println("\n✅ 完整流程执行成功！");
            System.out.println("\n📝 流程总结:");
            System.out.println("1. ✅ 定义函数 (getWeather, calculate)");
            System.out.println("2. ✅ 配置工具给模型");
            System.out.println("3. ✅ 模型分析请求并调用函数");
            System.out.println("4. ✅ 函数执行并返回结果");
            System.out.println("5. ✅ 模型基于函数结果生成最终答案");
            
        } catch (NoSuchMethodException e) {
            System.err.println("❌ 方法未找到: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ 运行错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}