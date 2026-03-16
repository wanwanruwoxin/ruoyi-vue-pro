package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DsMembershipPlanMapper extends BaseMapperX<DsMembershipPlan> {

    default DsMembershipPlan selectByPlanCode(String planCode) {
        return selectOne(DsMembershipPlan::getPlanCode, planCode);
    }

    default List<DsMembershipPlan> selectListByStatus(Integer status) {
        return selectList(DsMembershipPlan::getStatus, status);
    }
}
