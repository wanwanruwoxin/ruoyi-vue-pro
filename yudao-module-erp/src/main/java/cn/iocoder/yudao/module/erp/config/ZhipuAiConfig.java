package cn.iocoder.yudao.module.erp.config;

import cn.iocoder.yudao.module.erp.ai.AiTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZhipuAiConfig {
    @Resource
    private AiTools aiTools;

    @Bean
    public ChatClient zhipuChatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultOptions(ZhiPuAiChatOptions.builder()
                        .thinking(ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                        .build())
                .defaultTools(aiTools)
                .build();
    }
}
