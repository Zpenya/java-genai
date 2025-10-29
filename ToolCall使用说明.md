# ToolCall Function Call 使用说明

## 🎯 概述

这个示例展示了如何使用 `ToolCall` 方式处理Google Gen AI Java SDK中的函数调用，这是更接近OpenAI格式的处理方式。

## 🔄 ToolCall流程

### 完整流程步骤

```
1. 定义函数声明 (FunctionDeclaration)
   ↓
2. 配置工具给模型
   ↓
3. 模型分析请求并返回ToolCall对象
   ↓
4. 解析ToolCall对象，提取函数名和参数
   ↓
5. 执行相应的函数
   ↓
6. 将函数结果返回给模型
   ↓
7. 模型基于函数结果生成最终答案
```

## 📋 核心代码解析

### 1. 定义函数声明

```java
// 定义函数参数schema
ImmutableMap<String, Object> weatherParams = ImmutableMap.of(
    "type", "object",
    "properties", ImmutableMap.of(
        "city", ImmutableMap.of("type", "string", "description", "城市名称")
    ),
    "required", ImmutableList.of("city")
);

// 创建函数声明
Tool weatherTool = Tool.builder()
    .functionDeclarations(
        FunctionDeclaration.builder()
            .name("getWeather")
            .description("查询指定城市的天气信息")
            .parametersJsonSchema(weatherParams)
            .build()
    )
    .build();
```

### 2. 配置工具

```java
GenerateContentConfig config = GenerateContentConfig.builder()
    .tools(weatherTool, stockTool, calcTool)
    .build();
```

### 3. 发送请求并获取ToolCall

```java
GenerateContentResponse response = client.models.generateContent(
    "gemini-2.5-flash", 
    userPrompt, 
    config
);

// 检查是否有ToolCall
if (response.functionCalls().isPresent()) {
    List<FunctionCall> functionCalls = response.functionCalls().get();
    // 处理ToolCall
}
```

### 4. 处理ToolCall

```java
public static String processToolCall(FunctionCall functionCall) {
    String functionName = functionCall.name();
    Map<String, Object> arguments = functionCall.args();
    
    System.out.println("🔧 [ToolCall处理] 函数名: " + functionName);
    System.out.println("🔧 [ToolCall处理] 参数: " + arguments);
    
    switch (functionName) {
        case "getWeather":
            String city = (String) arguments.get("city");
            return getWeather(city);
        case "getStockPrice":
            String symbol = (String) arguments.get("symbol");
            return getStockPrice(symbol);
        // ... 其他函数
    }
}
```

### 5. 将结果返回给模型

```java
// 构建包含函数结果的对话
List<Content> conversation = new ArrayList<>();

// 添加用户原始请求
conversation.add(Content.fromParts(Part.fromText(userPrompt)));

// 添加函数调用结果
for (String result : functionResults) {
    conversation.add(Content.fromParts(Part.fromText("函数执行结果: " + result)));
}

// 添加请求最终答案的提示
conversation.add(Content.fromParts(Part.fromText("请基于以上函数执行结果，生成一个完整的回答。")));

// 发送给模型生成最终答案
GenerateContentResponse finalResponse = client.models.generateContent(
    "gemini-2.5-flash", 
    conversation, 
    config
);
```

## 📁 示例文件说明

### 1. SimpleToolCallExample.java
- **简化版本**，专注于核心ToolCall流程
- 包含3个函数：天气查询、股票价格、数学计算
- 适合理解ToolCall的基本概念

### 2. ToolCallCompleteExample.java
- **完整版本**，包含更多功能
- 包含4个函数：用户查询、天气、股票、计算
- 包含详细的错误处理和日志
- 适合深入学习

## 🚀 快速开始

### 方法1: 使用脚本运行
```bash
# 设置API密钥
export GOOGLE_API_KEY=你的API密钥

# 运行脚本
./run_toolcall_example.sh
```

### 方法2: 直接编译运行
```bash
# 设置API密钥
export GOOGLE_API_KEY=你的API密钥

# 编译简化示例
javac -cp ".:google-genai-1.23.0.jar" SimpleToolCallExample.java

# 运行
java -cp ".:google-genai-1.23.0.jar" SimpleToolCallExample
```

## 📊 输出示例

```
🚀 使用ToolCall的简化示例
================================================
✅ API密钥已设置
✅ 步骤1: 客户端创建成功

🔧 步骤2: 定义函数声明
✅ 函数声明创建完成
✅ 步骤3: 工具配置完成

📤 步骤4: 发送请求给模型
用户请求: 请查询北京的天气，然后查询苹果公司的股票价格，最后计算15乘以8的结果

🔧 步骤5: 处理ToolCall
================================================
检测到 3 个函数调用

--- 处理函数调用 1 ---
🔧 [ToolCall处理] 函数名: getWeather
🔧 [ToolCall处理] 参数: {city=北京}
🌤️ [函数执行] 查询天气: 北京
✅ [函数执行] 天气查询完成: 晴天，温度25°C

--- 处理函数调用 2 ---
🔧 [ToolCall处理] 函数名: getStockPrice
🔧 [ToolCall处理] 参数: {symbol=AAPL}
📈 [函数执行] 查询股票价格: AAPL
✅ [函数执行] 股票价格查询完成: $175.5

--- 处理函数调用 3 ---
🔧 [ToolCall处理] 函数名: calculate
🔧 [ToolCall处理] 参数: {expression=15*8}
🧮 [函数执行] 计算表达式: 15*8
✅ [函数执行] 计算完成: 120.0

📤 步骤6: 将函数结果发送回模型
================================================

🎯 步骤7: 最终答案
================================================
AI最终答案: 根据查询结果：

1. 北京天气：晴天，温度25°C
2. 苹果公司(AAPL)股票价格：$175.5
3. 15乘以8的计算结果：120.0

所有请求都已成功处理完成。

✅ 完整ToolCall流程执行成功！

📝 流程总结:
1. ✅ 使用FunctionDeclaration定义函数
2. ✅ 模型返回ToolCall对象
3. ✅ 解析ToolCall并执行函数
4. ✅ 将函数结果返回给模型
5. ✅ 模型生成最终答案
```

## 🔧 关键区别

### ToolCall vs 反射方法

| 特性 | ToolCall方式 | 反射方法 |
|------|-------------|----------|
| 函数定义 | FunctionDeclaration | 直接定义Java方法 |
| 参数处理 | 手动解析arguments | 自动类型转换 |
| 错误处理 | 手动处理 | 自动异常处理 |
| 灵活性 | 更高，可以处理复杂参数 | 较低，受Java类型限制 |
| 调试 | 需要手动解析ToolCall | 自动处理 |

### ToolCall vs OpenAI格式

| 特性 | ToolCall | OpenAI |
|------|----------|--------|
| 函数定义 | FunctionDeclaration | functions数组 |
| 调用结果 | FunctionCall对象 | ToolCall对象 |
| 参数格式 | Map<String, Object> | JSON字符串 |
| 处理方式 | 手动解析 | 自动处理 |

## ⚠️ 注意事项

1. **函数声明格式**: 必须使用正确的JSON Schema格式
2. **参数类型**: 所有参数都是String类型，需要手动转换
3. **错误处理**: 需要手动处理各种错误情况
4. **ToolCall解析**: 需要手动解析FunctionCall对象
5. **结果返回**: 需要手动构建对话内容

## 🎨 自定义扩展

### 添加新函数

```java
// 1. 定义函数参数schema
ImmutableMap<String, Object> myFunctionParams = ImmutableMap.of(
    "type", "object",
    "properties", ImmutableMap.of(
        "param1", ImmutableMap.of("type", "string", "description", "参数1"),
        "param2", ImmutableMap.of("type", "string", "description", "参数2")
    ),
    "required", ImmutableList.of("param1", "param2")
);

// 2. 创建函数声明
Tool myFunctionTool = Tool.builder()
    .functionDeclarations(
        FunctionDeclaration.builder()
            .name("myFunction")
            .description("我的自定义函数")
            .parametersJsonSchema(myFunctionParams)
            .build()
    )
    .build();

// 3. 在processToolCall中添加处理逻辑
case "myFunction":
    String param1 = (String) arguments.get("param1");
    String param2 = (String) arguments.get("param2");
    return myFunction(param1, param2);
```

## 🔗 相关链接

- [Google Gen AI Java SDK](https://github.com/googleapis/java-genai)
- [Gemini API 文档](https://ai.google.dev/gemini-api/docs)
- [Function Calling 文档](https://ai.google.dev/gemini-api/docs/function-calling)

## 📞 支持

如果遇到问题，请检查：
1. 函数声明的JSON Schema格式是否正确
2. 参数类型转换是否正确
3. ToolCall解析逻辑是否正确
4. 错误处理是否完善

祝您使用愉快！🎉