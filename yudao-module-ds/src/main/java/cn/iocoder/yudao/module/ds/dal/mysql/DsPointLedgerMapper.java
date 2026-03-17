package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointLedger;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DsPointLedgerMapper extends BaseMapperX<DsPointLedger> {

    default BigDecimal sumPointsByUidAndBizTypeBetween(Long uid, String bizType,
                                                        LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> result = selectMaps(new QueryWrapper<DsPointLedger>()
                .select("SUM(points) AS points_sum")
                .eq("uid", uid)
                .eq("biz_type", bizType)
                .ge("occurred_at", startTime)
                .lt("occurred_at", endTime));
        Map<String, Object> first = CollUtil.getFirst(result);
        if (first == null || first.get("points_sum") == null) {
            return BigDecimal.ZERO;
        }
        Object sumValue = first.get("points_sum");
        if (sumValue instanceof BigDecimal) {
            return (BigDecimal) sumValue;
        }
        return new BigDecimal(String.valueOf(sumValue));
    }
}
