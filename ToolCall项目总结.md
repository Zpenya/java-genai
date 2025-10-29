# ToolCall Function Call 项目总结

## 🎯 项目概述

本项目提供了使用 `ToolCall` 方式处理Google Gen AI Java SDK中函数调用的完整示例，包括简化版本和完整版本，以及详细的文档说明。

## 📁 项目文件结构

```
/workspace/
├── ToolCallCompleteExample.java      # 完整ToolCall示例
├── SimpleToolCallExample.java        # 简化ToolCall示例
├── run_toolcall_example.sh          # 运行脚本
├── ToolCall使用说明.md               # 详细使用说明
└── ToolCall项目总结.md               # 项目总结（本文件）
```

## 🚀 快速开始

### 1. 环境准备
```bash
# 设置API密钥
export GOOGLE_API_KEY=你的API密钥

# 确保有Java环境
java -version
```

### 2. 运行示例
```bash
# 使用脚本运行（推荐）
./run_toolcall_example.sh

# 或直接编译运行
javac -cp ".:google-genai-1.23.0.jar" SimpleToolCallExample.java
java -cp ".:google-genai-1.23.0.jar" SimpleToolCallExample
```

## 📋 示例文件说明

### 1. SimpleToolCallExample.java
**简化版本，适合快速理解ToolCall概念**

**包含功能：**
- 天气查询函数
- 股票价格查询函数
- 数学计算函数
- 基本的ToolCall处理流程

**核心特点：**
- 代码简洁，易于理解
- 包含完整的流程注释
- 适合初学者学习

**运行效果：**
```
用户请求: 请查询北京的天气，然后查询苹果公司的股票价格，最后计算15乘以8的结果

检测到 3 个函数调用
- 天气查询: 北京 → 晴天，温度25°C
- 股票价格: AAPL → $175.5
- 数学计算: 15*8 → 120.0

AI最终答案: 根据查询结果，北京天气晴朗，苹果股票价格为$175.5，15乘以8等于120。
```

### 2. ToolCallCompleteExample.java
**完整版本，包含更多功能和错误处理**

**包含功能：**
- 用户信息查询函数
- 天气查询函数
- 股票价格查询函数
- 数学计算函数
- 详细的错误处理
- 完整的日志记录

**核心特点：**
- 功能完整，适合生产环境
- 包含详细的错误处理
- 支持复杂的业务逻辑
- 包含完整的日志记录

**运行效果：**
```
用户请求: 请查询用户001的详细信息，然后查询他所在城市的天气，再查询苹果公司的股票价格，最后计算一下他的月薪除以30天是多少。

检测到 4 个函数调用
- 用户查询: 001 → 张三，28岁，北京，月薪15000
- 天气查询: 北京 → 晴天，温度25°C，湿度60%
- 股票价格: AAPL → $175.5
- 数学计算: 15000/30 → 500.0

AI最终答案: 用户001（张三）的详细信息如下：28岁，邮箱zhangsan@example.com，居住在北京，月薪15000元。北京今天天气晴朗，温度25°C，湿度60%。苹果公司股票当前价格为$175.5。张三的日薪约为500元（15000÷30）。
```

## 🔄 ToolCall流程详解

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

### 关键代码片段

#### 1. 函数声明定义
```java
ImmutableMap<String, Object> weatherParams = ImmutableMap.of(
    "type", "object",
    "properties", ImmutableMap.of(
        "city", ImmutableMap.of("type", "string", "description", "城市名称")
    ),
    "required", ImmutableList.of("city")
);

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

#### 2. ToolCall处理
```java
public static String processToolCall(FunctionCall functionCall) {
    String functionName = functionCall.name();
    Map<String, Object> arguments = functionCall.args();
    
    switch (functionName) {
        case "getWeather":
            String city = (String) arguments.get("city");
            return getWeather(city);
        // ... 其他函数
    }
}
```

#### 3. 结果返回给模型
```java
List<Content> conversation = new ArrayList<>();
conversation.add(Content.fromParts(Part.fromText(userPrompt)));

for (String result : functionResults) {
    conversation.add(Content.fromParts(Part.fromText("函数执行结果: " + result)));
}

conversation.add(Content.fromParts(Part.fromText("请基于以上函数执行结果，生成一个完整的回答。")));

GenerateContentResponse finalResponse = client.models.generateContent(
    "gemini-2.5-flash", 
    conversation, 
    config
);
```

## 🎨 自定义扩展指南

### 添加新函数

1. **定义函数参数schema**
```java
ImmutableMap<String, Object> myFunctionParams = ImmutableMap.of(
    "type", "object",
    "properties", ImmutableMap.of(
        "param1", ImmutableMap.of("type", "string", "description", "参数1"),
        "param2", ImmutableMap.of("type", "string", "description", "参数2")
    ),
    "required", ImmutableList.of("param1", "param2")
);
```

2. **创建函数声明**
```java
Tool myFunctionTool = Tool.builder()
    .functionDeclarations(
        FunctionDeclaration.builder()
            .name("myFunction")
            .description("我的自定义函数")
            .parametersJsonSchema(myFunctionParams)
            .build()
    )
    .build();
```

3. **在processToolCall中添加处理逻辑**
```java
case "myFunction":
    String param1 = (String) arguments.get("param1");
    String param2 = (String) arguments.get("param2");
    return myFunction(param1, param2);
```

## 🔧 技术特点

### ToolCall vs 其他方式

| 特性 | ToolCall方式 | 反射方法 | OpenAI格式 |
|------|-------------|----------|------------|
| 函数定义 | FunctionDeclaration | 直接定义Java方法 | functions数组 |
| 参数处理 | 手动解析arguments | 自动类型转换 | 自动处理 |
| 错误处理 | 手动处理 | 自动异常处理 | 自动处理 |
| 灵活性 | 更高 | 较低 | 中等 |
| 调试 | 需要手动解析 | 自动处理 | 自动处理 |
| 学习曲线 | 较陡 | 平缓 | 平缓 |

### 优势
- ✅ 更接近OpenAI的ToolCall格式
- ✅ 更高的灵活性，可以处理复杂参数
- ✅ 更好的错误控制
- ✅ 适合复杂的业务逻辑

### 劣势
- ❌ 需要手动处理ToolCall解析
- ❌ 参数类型需要手动转换
- ❌ 错误处理需要手动实现
- ❌ 学习曲线较陡

## 📊 使用场景

### 适合使用ToolCall的场景
- 需要处理复杂参数结构
- 需要精确控制函数调用过程
- 需要自定义错误处理逻辑
- 需要与OpenAI格式兼容
- 需要处理动态函数调用

### 不适合使用ToolCall的场景
- 简单的函数调用需求
- 快速原型开发
- 不需要复杂参数处理
- 团队对ToolCall不熟悉

## 🚀 最佳实践

### 1. 函数设计
- 保持函数功能单一
- 使用清晰的函数名和描述
- 合理设计参数结构
- 包含必要的错误处理

### 2. 错误处理
- 验证输入参数
- 处理函数执行异常
- 提供有意义的错误信息
- 记录详细的错误日志

### 3. 性能优化
- 避免长时间运行的函数
- 使用异步处理（如需要）
- 缓存重复查询结果
- 合理设置超时时间

### 4. 代码组织
- 将函数声明集中管理
- 使用工厂模式创建工具
- 分离业务逻辑和ToolCall处理
- 编写单元测试

## 🔗 相关资源

- [Google Gen AI Java SDK](https://github.com/googleapis/java-genai)
- [Gemini API 文档](https://ai.google.dev/gemini-api/docs)
- [Function Calling 文档](https://ai.google.dev/gemini-api/docs/function-calling)
- [JSON Schema 规范](https://json-schema.org/)

## 📞 支持与反馈

如果您在使用过程中遇到问题，请检查：
1. 函数声明的JSON Schema格式是否正确
2. 参数类型转换是否正确
3. ToolCall解析逻辑是否正确
4. 错误处理是否完善

## 🎉 总结

本项目提供了完整的ToolCall Function Call解决方案，包括：
- 简化版本用于快速理解
- 完整版本用于生产环境
- 详细的文档说明
- 便捷的运行脚本
- 完整的自定义扩展指南

通过这个项目，您可以：
- 快速上手ToolCall的使用
- 理解ToolCall的完整流程
- 学习如何自定义函数
- 掌握错误处理技巧
- 应用到实际项目中

祝您使用愉快！🎉