package com.gitee.usl.function.tool;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilter;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.plugin.annotation.NotNull;

/**
 * 布隆过滤器工具类
 *
 * @author hongda.li
 */
@FunctionGroup
public class BloomFilterFunction {

    @Function("bloom")
    @Description("创建布隆过滤器")
    public BloomFilter bloom(@NotNull int size) {
        return new BitMapBloomFilter(size);
    }

    @Function("bloom_add")
    @Description("向布隆过滤器添加一个元素")
    public BloomFilter add(BloomFilter filter, String key) {
        filter.add(key);
        return filter;
    }

    @Function("bloom_contains")
    @Description("使用布隆过滤器判断元素是否存在")
    public boolean contains(BloomFilter filter, String key) {
        return filter.contains(key);
    }
}
