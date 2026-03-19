package cn.iocoder.yudao.module.ds.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@TableName("ds_product_comment")
@KeySequence("ds_product_comment_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsProductComment extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private Boolean anonymous;
    private Long orderId;
    private Long orderItemId;
    private Long spuId;
    private String spuName;
    private Long skuId;
    private String skuPicUrl;
    private String skuPropertiesJson;
    private Boolean visible;
    private Integer scores;
    private Integer descriptionScores;
    private Integer benefitScores;
    private String content;
    private String picUrls;
    private Boolean replyStatus;
    private Long replyUserId;
    private String replyContent;
    private LocalDateTime replyTime;
}
