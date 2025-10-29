#!/bin/bash

# 完整Function Call示例运行脚本

echo "🚀 完整Function Call流程示例"
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
echo "1. 简化完整示例 (SimpleCompleteExample)"
echo "2. 详细完整示例 (CompleteFunctionCallExample)"
echo "3. 两个都运行"
echo ""
read -p "请输入选择 (1-3): " choice

case $choice in
    1)
        run_simple_example
        ;;
    2)
        run_complete_example
        ;;
    3)
        run_simple_example
        echo ""
        echo "================================"
        echo ""
        run_complete_example
        ;;
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

function run_simple_example() {
    echo ""
    echo "🏃 运行简化完整示例..."
    
    # 检查SDK JAR文件
    if [ ! -f "google-genai-1.23.0.jar" ]; then
        echo "❌ 未找到 google-genai-1.23.0.jar"
        echo "   请下载SDK JAR文件或使用Maven"
        echo "   下载地址: https://mvnrepository.com/artifact/com.google.genai/google-genai/1.23.0"
        exit 1
    fi
    
    # 编译
    echo "📦 编译简化示例..."
    javac -cp ".:google-genai-1.23.0.jar" SimpleCompleteExample.java
    
    if [ $? -eq 0 ]; then
        echo "✅ 编译成功"
        echo "🏃 运行简化示例..."
        echo ""
        java -cp ".:google-genai-1.23.0.jar" SimpleCompleteExample
    else
        echo "❌ 编译失败"
        exit 1
    fi
}

function run_complete_example() {
    echo ""
    echo "🏃 运行详细完整示例..."
    
    # 检查SDK JAR文件
    if [ ! -f "google-genai-1.23.0.jar" ]; then
        echo "❌ 未找到 google-genai-1.23.0.jar"
        echo "   请下载SDK JAR文件或使用Maven"
        echo "   下载地址: https://mvnrepository.com/artifact/com.google.genai/google-genai/1.23.0"
        exit 1
    fi
    
    # 编译
    echo "📦 编译详细示例..."
    javac -cp ".:google-genai-1.23.0.jar" CompleteFunctionCallExample.java
    
    if [ $? -eq 0 ]; then
        echo "✅ 编译成功"
        echo "🏃 运行详细示例..."
        echo ""
        java -cp ".:google-genai-1.23.0.jar" CompleteFunctionCallExample
    else
        echo "❌ 编译失败"
        exit 1
    fi
}

echo ""
echo "✅ 脚本执行完成！"