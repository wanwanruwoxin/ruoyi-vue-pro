package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopPageReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsShopMapper extends BaseMapperX<DsShop> {

    default DsShop selectByUid(Long uid) {
        return selectOne(DsShop::getUid, uid);
    }

    default DsShop selectByBackendAdminUserId(Long backendAdminUserId) {
        return selectOne(DsShop::getBackendAdminUserId, backendAdminUserId);
    }

    default PageResult<DsShop> selectPage(DsShopPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DsShop>()
                .eqIfPresent(DsShop::getUid, reqVO.getUid())
                .likeIfPresent(DsShop::getShopName, reqVO.getShopName())
                .likeIfPresent(DsShop::getContactMobile, reqVO.getContactMobile())
                .eqIfPresent(DsShop::getStatus, reqVO.getStatus())
                .orderByAsc(DsShop::getSort)
                .orderByDesc(DsShop::getId));
    }
}
