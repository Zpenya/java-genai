# 完整的Function Call流程说明

## 🎯 概述

这个示例展示了Google Gen AI Java SDK中Function Call的完整流程，从函数定义到最终答案生成的每一个步骤。

## 📋 完整流程步骤

### 步骤1: 定义函数
```java
// 定义可调用的函数
public static String getWeather(String city) {
    // 函数实现
    return "天气信息";
}

public static String calculate(String expression) {
    // 函数实现
    return "计算结果";
}
```

### 步骤2: 获取函数方法的反射对象
```java
// 获取函数的反射对象
Method weatherMethod = MyClass.class.getMethod("getWeather", String.class);
Method calcMethod = MyClass.class.getMethod("calculate", String.class);
```

### 步骤3: 配置工具给模型
```java
// 创建工具配置
GenerateContentConfig config = GenerateContentConfig.builder()
    .tools(Tool.builder().functions(weatherMethod, calcMethod))
    .build();
```

### 步骤4: 发送请求给模型
```java
// 发送用户请求
String userPrompt = "请查询北京天气，然后计算15*8";
GenerateContentResponse response = client.models.generateContent(
    "gemini-2.5-flash", 
    userPrompt, 
    config
);
```

### 步骤5: 模型自动调用函数
- 模型分析用户请求
- 决定需要调用哪些函数
- 自动调用相应的函数
- 函数执行并返回结果

### 步骤6: 模型生成最终答案
- 模型接收函数执行结果
- 整合所有信息
- 生成包含函数结果的最终答案

## 🔄 详细执行流程

```
用户请求: "请查询北京天气，然后计算15*8"
    ↓
模型分析: 需要调用getWeather和calculate函数
    ↓
调用getWeather("北京")
    ↓
函数执行: 模拟API调用，返回天气信息
    ↓
调用calculate("15*8")
    ↓
函数执行: 模拟计算，返回120
    ↓
模型整合: 将天气信息和计算结果整合
    ↓
最终答案: "北京今天晴天，温度25°C。15乘以8等于120。"
```

## 📁 示例文件说明

### 1. SimpleCompleteExample.java
- **简化版本**，专注于核心流程
- 包含2个函数：天气查询和数学计算
- 适合初学者理解基本概念

### 2. CompleteFunctionCallExample.java
- **完整版本**，包含更多功能
- 包含5个函数：用户查询、天气、股票、计算、时间
- 包含错误处理和分步骤示例
- 适合深入学习

## 🚀 快速开始

### 方法1: 使用脚本运行
```bash
# 设置API密钥
export GOOGLE_API_KEY=你的API密钥

# 运行脚本
./run_complete_example.sh
```

### 方法2: 直接编译运行
```bash
# 设置API密钥
export GOOGLE_API_KEY=你的API密钥

# 编译简化示例
javac -cp ".:google-genai-1.23.0.jar" SimpleCompleteExample.java

# 运行
java -cp ".:google-genai-1.23.0.jar" SimpleCompleteExample
```

## 🔧 核心代码解析

### 函数定义
```java
public static String getWeather(String city) {
    System.out.println("🌤️ [函数执行] 查询 " + city + " 的天气...");
    
    // 模拟API调用延迟
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    
    // 模拟天气查询逻辑
    String result = city + "今天晴天，温度25°C";
    System.out.println("✅ [函数执行] 查询结果: " + result);
    
    return result;
}
```

### 工具配置
```java
// 获取函数方法的反射对象
Method weatherMethod = SimpleCompleteExample.class.getMethod("getWeather", String.class);
Method calcMethod = SimpleCompleteExample.class.getMethod("calculate", String.class);

// 配置工具
GenerateContentConfig config = GenerateContentConfig.builder()
    .tools(Tool.builder().functions(weatherMethod, calcMethod))
    .build();
```

### 发送请求
```java
// 发送请求给模型
GenerateContentResponse response = client.models.generateContent(
    "gemini-2.5-flash", 
    userPrompt, 
    config
);

// 获取最终答案
System.out.println("最终答案: " + response.text());
```

## 📊 输出示例

```
🚀 完整Function Call流程示例
================================================
📱 步骤1: 创建客户端
✅ 客户端创建成功

🔧 步骤2: 配置函数
✅ 函数方法获取成功
✅ 工具配置完成

📤 步骤3: 发送请求给模型
用户请求: 请查询北京的天气，然后计算15乘以8的结果

🤖 步骤4: 模型处理请求
----------------------------------------
模型正在分析请求并决定调用哪些函数...
🌤️  [函数执行] 查询 北京 的天气...
✅ [函数执行] 查询结果: 北京今天晴天，温度25°C
🧮 [函数执行] 计算: 15*8
✅ [函数执行] 计算结果: 120.0

📋 步骤5: 显示最终结果
================================================
🎯 最终答案: 根据查询结果，北京今天晴天，温度25°C。15乘以8的计算结果是120。

📊 函数调用历史:
[显示详细的函数调用历史]

🔍 函数调用详情:
[显示函数调用的详细信息]

✅ 完整流程执行成功！

📝 流程总结:
1. ✅ 定义函数 (getWeather, calculate)
2. ✅ 配置工具给模型
3. ✅ 模型分析请求并调用函数
4. ✅ 函数执行并返回结果
5. ✅ 模型基于函数结果生成最终答案
```

## ⚠️ 注意事项

1. **函数必须是静态的**: 所有用于Function Call的方法都必须是 `public static`
2. **编译参数**: 编译时必须使用 `-parameters` 参数
3. **API密钥**: 确保正确设置了 `GOOGLE_API_KEY` 环境变量
4. **网络连接**: 确保能访问Google API
5. **错误处理**: 函数中应该包含适当的错误处理

## 🎨 自定义扩展

您可以基于这些示例添加自己的函数：

```java
// 添加新函数
public static String myCustomFunction(String param) {
    System.out.println("🔧 [函数执行] 执行自定义函数: " + param);
    
    // 您的业务逻辑
    String result = "处理结果: " + param;
    
    System.out.println("✅ [函数执行] 自定义函数完成: " + result);
    return result;
}

// 在配置中添加
Method myMethod = MyClass.class.getMethod("myCustomFunction", String.class);
config = GenerateContentConfig.builder()
    .tools(Tool.builder().functions(myMethod))
    .build();
```

## 🔗 相关链接

- [Google Gen AI Java SDK](https://github.com/googleapis/java-genai)
- [Gemini API 文档](https://ai.google.dev/gemini-api/docs)
- [Function Calling 文档](https://ai.google.dev/gemini-api/docs/function-calling)

## 📞 支持

如果遇到问题，请检查：
1. Java版本（需要Java 8+）
2. API密钥是否正确设置
3. 网络连接是否正常
4. 编译参数是否正确
5. 函数定义是否符合要求

祝您使用愉快！🎉