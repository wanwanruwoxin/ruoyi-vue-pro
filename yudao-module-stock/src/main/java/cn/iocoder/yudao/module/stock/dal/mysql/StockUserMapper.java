package cn.iocoder.yudao.module.stock.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.stock.dal.dataobject.StockUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockUserMapper extends BaseMapperX<StockUser> {

    default StockUser selectByPhone(String phone) {
        return selectOne(StockUser::getPhone, phone);
    }

    default StockUser selectByEmail(String email) {
        return selectOne(StockUser::getEmail, email);
    }
}
