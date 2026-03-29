package cn.iocoder.yudao.module.erp.controller.admin.ai;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP AI 对话")
@RestController
@RequestMapping("/erp/ai")
@Validated
public class ErpAiChatController {

    @Resource(name = "zhipuChatClient")
    private ChatClient zhipuChatClient;

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "AI 对话")
    public Flux<CommonResult<ErpAiChatRespVO>> chat(@Valid @RequestBody ErpAiChatReqVO reqVO) {
        return zhipuChatClient.prompt(reqVO.getMessage())
                .stream()
                .content()
                .map(content -> success(new ErpAiChatRespVO().setContent(content)));
    }

    @Data
    public static class ErpAiChatReqVO {

        @NotBlank(message = "消息内容不能为空")
        private String message;
    }

    @Data
    public static class ErpAiChatRespVO {

        private String content;
    }

}
