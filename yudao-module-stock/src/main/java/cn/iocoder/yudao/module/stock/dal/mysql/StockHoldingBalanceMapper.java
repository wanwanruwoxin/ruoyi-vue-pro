package cn.iocoder.yudao.module.stock.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.stock.dal.dataobject.StockHoldingBalance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockHoldingBalanceMapper extends BaseMapperX<StockHoldingBalance> {
}
