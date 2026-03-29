package cn.iocoder.yudao.module.erp.ai;

import cn.iocoder.yudao.module.erp.controller.admin.stock.vo.warehouse.ErpWarehouseSaveReqVO;
import cn.iocoder.yudao.module.erp.service.stock.ErpWarehouseService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class AiTools {
    @Resource
    private ErpWarehouseService stockService;

    @Tool(description = "创建仓库", name = "createStock")
    public void createStock(@ToolParam(description = "创建仓库vo对象") ErpWarehouseSaveReqVO reqVO) {
        stockService.createWarehouse(reqVO);
    }

}
