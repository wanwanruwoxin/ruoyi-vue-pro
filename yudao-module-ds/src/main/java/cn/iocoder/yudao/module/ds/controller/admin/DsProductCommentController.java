package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductComment;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductCommentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商商品评价")
@RestController
@RequestMapping("/ds/product-comment")
@Validated
public class DsProductCommentController {

    @Resource
    private DsProductCommentMapper dsProductCommentMapper;

    @GetMapping("/page")
    @Operation(summary = "商品评价分页")
    public CommonResult<PageResult<DsProductComment>> getPage(@Valid PageParam pageParam,
                                                              @RequestParam(value = "spuId", required = false) Long spuId,
                                                              @RequestParam(value = "userId", required = false) Long userId,
                                                              @RequestParam(value = "visible", required = false) Boolean visible,
                                                              @RequestParam(value = "scores", required = false) Integer scores) {
        return success(dsProductCommentMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsProductComment>()
                .eqIfPresent(DsProductComment::getSpuId, spuId)
                .eqIfPresent(DsProductComment::getUserId, userId)
                .eqIfPresent(DsProductComment::getVisible, visible)
                .eqIfPresent(DsProductComment::getScores, scores)
                .orderByDesc(DsProductComment::getId)));
    }

    @GetMapping("/get")
    @Operation(summary = "商品评价详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsProductComment> get(@RequestParam("id") Long id) {
        return success(dsProductCommentMapper.selectById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建商品评价")
    public CommonResult<Long> create(@Valid @RequestBody DsProductComment reqVO) {
        reqVO.setId(null);
        if (reqVO.getVisible() == null) {
            reqVO.setVisible(true);
        }
        if (reqVO.getReplyStatus() == null) {
            reqVO.setReplyStatus(false);
        }
        dsProductCommentMapper.insert(reqVO);
        return success(reqVO.getId());
    }

    @PutMapping("/update-visible")
    @Operation(summary = "更新评价显示状态")
    public CommonResult<Boolean> updateVisible(@Valid @RequestBody UpdateVisibleReqVO reqVO) {
        DsProductComment comment = dsProductCommentMapper.selectById(reqVO.getId());
        if (comment == null) {
            return success(false);
        }
        comment.setVisible(reqVO.getVisible());
        dsProductCommentMapper.updateById(comment);
        return success(true);
    }

    @PutMapping("/reply")
    @Operation(summary = "回复评价")
    public CommonResult<Boolean> reply(@Valid @RequestBody ReplyReqVO reqVO) {
        DsProductComment comment = dsProductCommentMapper.selectById(reqVO.getId());
        if (comment == null) {
            return success(false);
        }
        comment.setReplyStatus(true);
        comment.setReplyUserId(reqVO.getReplyUserId());
        comment.setReplyContent(reqVO.getReplyContent());
        comment.setReplyTime(LocalDateTime.now());
        dsProductCommentMapper.updateById(comment);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品评价")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsProductCommentMapper.deleteById(id);
        return success(true);
    }

    @Data
    public static class UpdateVisibleReqVO {
        @NotNull(message = "评价编号不能为空")
        private Long id;
        @NotNull(message = "可见状态不能为空")
        private Boolean visible;
    }

    @Data
    public static class ReplyReqVO {
        @NotNull(message = "评价编号不能为空")
        private Long id;
        private Long replyUserId;
        @NotBlank(message = "回复内容不能为空")
        private String replyContent;
    }
}
