package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.mysql.DsPointAccountMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DsPointAccountServiceImpl implements DsPointAccountService {

    @Resource
    private DsPointAccountMapper dsPointAccountMapper;
}
