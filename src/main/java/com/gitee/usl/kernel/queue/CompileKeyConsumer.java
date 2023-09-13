package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.annotation.Order;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * USL 唯一键生成器 消费者
 * 采用 SHA512 摘要算法
 *
 * @author hongda.li
 */
@Order(CompileGeneratorConsumer.GENERATOR_ORDER - 100)
@SuppressWarnings("UnstableApiUsage")
public class CompileKeyConsumer implements CompileConsumer {
    private final Hasher hasher = Hashing.sha512().newHasher();

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) throws Exception {
        String content = event.getContent();

        String sha512Key = hasher.putString(content, StandardCharsets.UTF_8).hash().toString();

        event.setKey(sha512Key);
    }
}
