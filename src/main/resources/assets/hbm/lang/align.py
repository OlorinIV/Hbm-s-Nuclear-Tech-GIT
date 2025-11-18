#!/usr/bin/env python3
# -*- coding: utf-8 -*-

#By Deepseek
#翻译文件对齐脚本
#使用: 地址栏中输入cmd回车；cmd中运行 python align.py en_US.lang zh_CN.lang；需要 python 3.7+

import re
import sys
import os
from collections import OrderedDict

def parse_properties_file(filename):
    """解析属性文件，返回有序字典"""
    properties = OrderedDict()
    current_key = None
    current_value = []
    multi_line = False
    
    with open(filename, 'r', encoding='utf-8') as f:
        for line_num, line in enumerate(f, 1):
            line = line.rstrip('\r\n')
            
            # 处理多行值
            if multi_line:
                # 检查是否是多行值的结束
                if line.endswith('\\'):
                    current_value.append(line[:-1])
                else:
                    current_value.append(line)
                    properties[current_key] = '\n'.join(current_value)
                    current_key = None
                    current_value = []
                    multi_line = False
                continue
            
            # 跳过注释和空行
            if line.strip().startswith('#') or not line.strip():
                properties[f"__comment_{line_num}"] = line
                continue
            
            # 解析键值对
            if '=' in line:
                key, value = line.split('=', 1)
                key = key.strip()
                
                # 检查是否是多行值
                if value.endswith('\\'):
                    current_key = key
                    current_value = [value[:-1]]
                    multi_line = True
                else:
                    properties[key] = value
            else:
                # 无法解析的行，作为注释处理
                properties[f"__comment_{line_num}"] = line
    
    # 处理文件末尾的多行值
    if multi_line:
        properties[current_key] = '\n'.join(current_value)
    
    return properties

def align_translations(en_file, zh_file, output_file):
    """对齐翻译文件"""
    print(f"正在解析英文文件: {en_file}")
    en_properties = parse_properties_file(en_file)
    
    print(f"正在解析中文文件: {zh_file}")
    zh_properties = parse_properties_file(zh_file)
    
    print("正在对齐翻译...")
    with open(output_file, 'w', encoding='utf-8') as f:
        for key, en_value in en_properties.items():
            if key.startswith('__comment_'):
                # 写入注释或空行
                f.write(en_value + '\n')
            else:
                # 查找对应的中文翻译
                zh_value = zh_properties.get(key, '')
                f.write(f"{key}={zh_value}\n")
    
    # 统计信息
    en_keys = set(k for k in en_properties.keys() if not k.startswith('__comment_'))
    zh_keys = set(k for k in zh_properties.keys() if not k.startswith('__comment_'))
    
    missing_keys = en_keys - zh_keys
    extra_keys = zh_keys - en_keys
    
    print(f"\n处理完成! 输出文件: {output_file}")
    print(f"英文键数量: {len(en_keys)}")
    print(f"中文键数量: {len(zh_keys)}")
    print(f"缺失的翻译键: {len(missing_keys)}")
    print(f"多余的翻译键: {len(extra_keys)}")
    
    if missing_keys:
        print("\n缺失的键:")
        for key in sorted(missing_keys):
            print(f"  {key}")
    
    if extra_keys:
        print("\n多余的键 (英文文件中不存在):")
        for key in sorted(extra_keys):
            print(f"  {key}")

def main():
    if len(sys.argv) != 3:
        print("用法: python align_translations.py <英文文件> <中文文件>")
        print("示例: python align_translations.py en.properties zh.properties")
        sys.exit(1)
    
    en_file = sys.argv[1]
    zh_file = sys.argv[2]
    output_file = f"aligned_{os.path.basename(zh_file)}"
    
    if not os.path.exists(en_file):
        print(f"错误: 英文文件 {en_file} 不存在")
        sys.exit(1)
    
    if not os.path.exists(zh_file):
        print(f"错误: 中文文件 {zh_file} 不存在")
        sys.exit(1)
    
    align_translations(en_file, zh_file, output_file)

if __name__ == "__main__":
    main()
