package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShareholderPoolRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DsShareholderPoolRecordMapper extends BaseMapperX<DsShareholderPoolRecord> {

    default List<DsShareholderPoolRecord> selectListBySettleMonthAndStatus(String settleMonth, String settleStatus) {
        return selectList(new LambdaQueryWrapperX<DsShareholderPoolRecord>()
                .eq(DsShareholderPoolRecord::getSettleMonth, settleMonth)
                .eq(DsShareholderPoolRecord::getSettleStatus, settleStatus));
    }
}
