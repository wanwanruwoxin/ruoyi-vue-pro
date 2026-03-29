package cn.iocoder.yudao.server;

import cn.iocoder.yudao.module.erp.ai.AiTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiToolsTest {
    @Autowired
    private AiTools aiTools;

    @Autowired
    private ChatClient zhipuChatClient;

    @Test
    public void test01() {
        String content = zhipuChatClient.prompt("""
                帮我创建一个demo04的仓库
                仓库编号：demo04
                仓库地址为：九江市
                仓库名称：demo04
               责人: ql
                仓库编号：demo04
               注：demo03
                状态：开启
                其他缺少的值，你自己先用默认值
                """).call().content();
        System.out.println(content);
    }
}
