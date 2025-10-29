#!/bin/bash

# Google Gen AI Java SDK Function Call 示例运行脚本

echo "🚀 Google Gen AI Java SDK Function Call 示例"
echo "=============================================="

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到Java环境，请先安装Java 8或更高版本"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "⚠️  警告: 未找到Maven，将尝试直接使用Java运行"
    USE_MAVEN=false
else
    USE_MAVEN=true
fi

# 检查API密钥
if [ -z "$GOOGLE_API_KEY" ]; then
    echo "❌ 错误: 请设置环境变量 GOOGLE_API_KEY"
    echo "   命令: export GOOGLE_API_KEY=你的API密钥"
    echo "   获取API密钥: https://aistudio.google.com/app/apikey"
    exit 1
fi

echo "✅ API密钥已设置"

# 选择运行模式
echo ""
echo "请选择要运行的示例:"
echo "1. 简单示例 (SimpleFunctionCallExample)"
echo "2. 完整示例 (FunctionCallExample)"
echo "3. 两个都运行"
echo ""
read -p "请输入选择 (1-3): " choice

case $choice in
    1)
        run_simple_example
        ;;
    2)
        run_full_example
        ;;
    3)
        run_simple_example
        echo ""
        echo "================================"
        echo ""
        run_full_example
        ;;
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

function run_simple_example() {
    echo ""
    echo "🏃 运行简单示例..."
    
    if [ "$USE_MAVEN" = true ]; then
        # 使用Maven运行
        if [ -f "pom.xml" ]; then
            mvn clean compile exec:java -Dexec.mainClass="SimpleFunctionCallExample"
        else
            echo "⚠️  未找到pom.xml，尝试直接编译运行"
            compile_and_run_simple
        fi
    else
        compile_and_run_simple
    fi
}

function run_full_example() {
    echo ""
    echo "🏃 运行完整示例..."
    
    if [ "$USE_MAVEN" = true ]; then
        # 使用Maven运行
        if [ -f "pom.xml" ]; then
            mvn clean compile exec:java -Dexec.mainClass="FunctionCallExample"
        else
            echo "⚠️  未找到pom.xml，尝试直接编译运行"
            compile_and_run_full
        fi
    else
        compile_and_run_full
    fi
}

function compile_and_run_simple() {
    echo "📦 编译简单示例..."
    
    # 检查SDK JAR文件
    if [ ! -f "google-genai-1.23.0.jar" ]; then
        echo "❌ 未找到 google-genai-1.23.0.jar"
        echo "   请下载SDK JAR文件或使用Maven"
        echo "   下载地址: https://mvnrepository.com/artifact/com.google.genai/google-genai/1.23.0"
        exit 1
    fi
    
    # 编译
    javac -cp ".:google-genai-1.23.0.jar" SimpleFunctionCallExample.java
    
    if [ $? -eq 0 ]; then
        echo "✅ 编译成功"
        echo "🏃 运行简单示例..."
        java -cp ".:google-genai-1.23.0.jar" SimpleFunctionCallExample
    else
        echo "❌ 编译失败"
        exit 1
    fi
}

function compile_and_run_full() {
    echo "📦 编译完整示例..."
    
    # 检查SDK JAR文件
    if [ ! -f "google-genai-1.23.0.jar" ]; then
        echo "❌ 未找到 google-genai-1.23.0.jar"
        echo "   请下载SDK JAR文件或使用Maven"
        echo "   下载地址: https://mvnrepository.com/artifact/com.google.genai/google-genai/1.23.0"
        exit 1
    fi
    
    # 编译
    javac -cp ".:google-genai-1.23.0.jar" FunctionCallExample.java
    
    if [ $? -eq 0 ]; then
        echo "✅ 编译成功"
        echo "🏃 运行完整示例..."
        java -cp ".:google-genai-1.23.0.jar" FunctionCallExample
    else
        echo "❌ 编译失败"
        exit 1
    fi
}

echo ""
echo "✅ 脚本执行完成！"