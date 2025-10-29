# Google Gen AI Java SDK Function Call 示例

这是一个使用Google Gen AI Java SDK进行函数调用的完整示例项目。

## 功能特性

本示例展示了以下功能：

1. **基本函数调用** - 展示如何定义和调用自定义函数
2. **聊天会话中的函数调用** - 在对话中动态调用函数
3. **错误处理** - 处理函数调用中的各种错误情况
4. **多种函数类型**：
   - 天气查询函数
   - 计算器函数
   - 数据库查询模拟函数
   - 时间获取函数

## 环境要求

- Java 8 或更高版本
- Maven 3.6 或更高版本
- Google API 密钥

## 快速开始

### 1. 获取API密钥

访问 [Google AI Studio](https://aistudio.google.com/app/apikey) 获取您的API密钥。

### 2. 设置环境变量

```bash
export GOOGLE_API_KEY=你的API密钥
```

### 3. 使用Maven运行

```bash
# 重命名pom文件
mv function-call-example-pom.xml pom.xml

# 编译项目
mvn clean compile

# 运行示例
mvn exec:java
```

### 4. 直接使用Java运行

```bash
# 编译
javac -cp ".:google-genai-1.23.0.jar" FunctionCallExample.java

# 运行
java -cp ".:google-genai-1.23.0.jar" FunctionCallExample
```

## 示例说明

### 基本函数调用示例

```java
// 定义函数
public static String getWeather(String city) {
    // 返回天气信息
}

// 配置工具
GenerateContentConfig config = GenerateContentConfig.builder()
    .tools(Tool.builder().functions(weatherMethod))
    .build();

// 调用AI
GenerateContentResponse response = client.models.generateContent(
    "gemini-2.5-flash", 
    "查询北京的天气", 
    config
);
```

### 聊天会话示例

```java
// 创建聊天会话
Chat chat = client.chats.create("gemini-2.5-flash", config);

// 发送消息
GenerateContentResponse response = chat.sendMessage("查询用户信息");
```

## 包含的函数

### 1. 天气查询函数
- **函数名**: `getWeather`
- **参数**: `city` (String) - 城市名称
- **返回**: 天气信息字符串

### 2. 计算器函数
- **函数名**: `calculate`
- **参数**: 
  - `a` (double) - 第一个数字
  - `b` (double) - 第二个数字
  - `operation` (String) - 运算类型
- **返回**: 计算结果字符串

### 3. 用户信息查询函数
- **函数名**: `getUserInfo`
- **参数**: `userId` (String) - 用户ID
- **返回**: 用户信息JSON字符串

### 4. 时间获取函数
- **函数名**: `getCurrentTime`
- **参数**: 无
- **返回**: 当前时间字符串

## 错误处理

示例包含以下错误处理场景：

1. **除零错误** - 当尝试除以零时
2. **用户不存在** - 当查询不存在的用户时
3. **不支持的运算** - 当使用不支持的运算类型时

## 注意事项

1. **参数名保留**: 确保在编译时使用 `-parameters` 参数，这样函数参数名才能被正确识别。

2. **函数必须是静态的**: 所有用于函数调用的方法都必须是 `public static` 的。

3. **API限制**: 请注意Google API的调用限制和配额。

4. **网络连接**: 确保网络连接正常，能够访问Google API。

## 扩展功能

您可以基于此示例添加更多功能：

- 数据库连接函数
- 文件操作函数
- 外部API调用函数
- 复杂的数据处理函数

## 故障排除

### 常见问题

1. **API密钥错误**
   ```
   错误：请设置环境变量 GOOGLE_API_KEY
   ```
   解决：确保正确设置了 `GOOGLE_API_KEY` 环境变量。

2. **编译错误**
   ```
   方法未找到错误
   ```
   解决：确保使用了 `-parameters` 编译参数。

3. **网络连接错误**
   ```
   运行错误: 连接超时
   ```
   解决：检查网络连接和防火墙设置。

## 许可证

本项目使用 Apache 2.0 许可证。

## 相关链接

- [Google Gen AI Java SDK 文档](https://github.com/googleapis/java-genai)
- [Gemini API 文档](https://ai.google.dev/gemini-api/docs)
- [Vertex AI 文档](https://cloud.google.com/vertex-ai/generative-ai/docs)