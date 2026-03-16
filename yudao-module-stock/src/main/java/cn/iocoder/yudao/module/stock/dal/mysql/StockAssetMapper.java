package cn.iocoder.yudao.module.stock.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.stock.dal.dataobject.StockAsset;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockAssetMapper extends BaseMapperX<StockAsset> {
}
