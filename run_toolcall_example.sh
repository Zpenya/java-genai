#!/bin/bash

# ToolCall示例运行脚本

echo "🚀 ToolCall Function Call示例"
echo "================================================"

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到Java环境，请先安装Java 8或更高版本"
    exit 1
fi

# 检查API密钥
if [ -z "$GOOGLE_API_KEY" ]; then
    echo "❌ 错误: 请设置环境变量 GOOGLE_API_KEY"
    echo "   命令: export GOOGLE_API_KEY=你的API密钥"
    echo "   获取API密钥: https://aistudio.google.com/app/apikey"
    exit 1
fi

echo "✅ API密钥已设置"

# 选择示例
echo ""
echo "请选择要运行的示例:"
echo "1. 简化ToolCall示例 (SimpleToolCallExample)"
echo "2. 完整ToolCall示例 (ToolCallCompleteExample)"
echo "3. 两个都运行"
echo ""
read -p "请输入选择 (1-3): " choice

case $choice in
    1)
        run_simple_toolcall
        ;;
    2)
        run_complete_toolcall
        ;;
    3)
        run_simple_toolcall
        echo ""
        echo "================================"
        echo ""
        run_complete_toolcall
        ;;
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

function run_simple_toolcall() {
    echo ""
    echo "🏃 运行简化ToolCall示例..."
    
    # 检查SDK JAR文件
    if [ ! -f "google-genai-1.23.0.jar" ]; then
        echo "❌ 未找到 google-genai-1.23.0.jar"
        echo "   请下载SDK JAR文件或使用Maven"
        echo "   下载地址: https://mvnrepository.com/artifact/com.google.genai/google-genai/1.23.0"
        exit 1
    fi
    
    # 编译
    echo "📦 编译简化ToolCall示例..."
    javac -cp ".:google-genai-1.23.0.jar" SimpleToolCallExample.java
    
    if [ $? -eq 0 ]; then
        echo "✅ 编译成功"
        echo "🏃 运行简化ToolCall示例..."
        echo ""
        java -cp ".:google-genai-1.23.0.jar" SimpleToolCallExample
    else
        echo "❌ 编译失败"
        exit 1
    fi
}

function run_complete_toolcall() {
    echo ""
    echo "🏃 运行完整ToolCall示例..."
    
    # 检查SDK JAR文件
    if [ ! -f "google-genai-1.23.0.jar" ]; then
        echo "❌ 未找到 google-genai-1.23.0.jar"
        echo "   请下载SDK JAR文件或使用Maven"
        echo "   下载地址: https://mvnrepository.com/artifact/com.google.genai/google-genai/1.23.0"
        exit 1
    fi
    
    # 编译
    echo "📦 编译完整ToolCall示例..."
    javac -cp ".:google-genai-1.23.0.jar" ToolCallCompleteExample.java
    
    if [ $? -eq 0 ]; then
        echo "✅ 编译成功"
        echo "🏃 运行完整ToolCall示例..."
        echo ""
        java -cp ".:google-genai-1.23.0.jar" ToolCallCompleteExample
    else
        echo "❌ 编译失败"
        exit 1
    fi
}

echo ""
echo "✅ 脚本执行完成！"