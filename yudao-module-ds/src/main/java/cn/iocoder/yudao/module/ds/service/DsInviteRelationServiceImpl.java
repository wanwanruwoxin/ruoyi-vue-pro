package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.mysql.DsInviteRelationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DsInviteRelationServiceImpl implements DsInviteRelationService {

    @Resource
    private DsInviteRelationMapper dsInviteRelationMapper;
}
