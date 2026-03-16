package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipAccountMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DsMembershipAccountServiceImpl implements DsMembershipAccountService {

    @Resource
    private DsMembershipAccountMapper dsMembershipAccountMapper;
}
