package cn.iocoder.yudao.module.erp.ai;

import cn.iocoder.yudao.module.erp.controller.admin.stock.vo.warehouse.ErpWarehouseSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.category.ErpProductCategorySaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.category.ErpProductCategoryRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.category.ErpProductCategoryListReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.unit.ErpProductUnitSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.unit.ErpProductUnitRespVO;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.product.ErpProductCategoryService;
import cn.iocoder.yudao.module.erp.service.product.ErpProductUnitService;
import cn.iocoder.yudao.module.erp.service.stock.ErpWarehouseService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@Service
public class AiTools {
    @Resource
    private ErpWarehouseService stockService;
    @Resource
    private ErpProductCategoryService productCategoryService;
    @Resource
    private ErpProductUnitService productUnitService;
    @Resource
    private ErpProductService productService;

    @Tool(description = "创建仓库", name = "createStock")
    public void createStock(@ToolParam(description = "创建仓库vo对象, id 字段可以为空，数据库设置了自增, status字段0表示开启,1表示关闭,默认开启") ErpWarehouseSaveReqVO reqVO) {
        stockService.createWarehouse(reqVO);
    }

    @Tool(description = "创建产品分类", name = "createProductCategory")
    public void createProductCategory(@ToolParam(description = "创建产品分类vo对象, id 字段可以为空，数据库设置了自增, status字段0表示开启,1表示关闭,默认开启") ErpProductCategorySaveReqVO reqVO) {
        productCategoryService.createProductCategory(reqVO);
    }

    @Tool(description = "创建产品单位", name = "createProductUnit")
    public void createProductUnit(@ToolParam(description = "创建产品单位vo对象, id 字段可以为空，数据库设置了自增, status字段0表示开启,1表示关闭,默认开启") ErpProductUnitSaveReqVO reqVO) {
        productUnitService.createProductUnit(reqVO);
    }

    @Tool(description = "查询可用的产品分类列表", name = "queryProductCategory")
    public List<ErpProductCategoryRespVO> queryProductCategory(
            @ToolParam(description = "分类名称关键字，可为空", required = false) String name) {
        ErpProductCategoryListReqVO listReqVO = new ErpProductCategoryListReqVO()
                .setStatus(CommonStatusEnum.ENABLE.getStatus())
                .setName(name);
        return convertList(productCategoryService.getProductCategoryList(listReqVO), category -> new ErpProductCategoryRespVO()
                .setId(category.getId())
                .setName(category.getName())
                .setParentId(category.getParentId()));
    }

    @Tool(description = "查询可用的产品单位列表", name = "queryProductUnit")
    public List<ErpProductUnitRespVO> queryProductUnit() {
        return convertList(productUnitService.getProductUnitListByStatus(CommonStatusEnum.ENABLE.getStatus()),
                unit -> new ErpProductUnitRespVO().setId(unit.getId()).setName(unit.getName()));
    }

    @Tool(description = "创建产品", name = "createProduct")
    public void createProduct(@ToolParam(description = "创建产品vo对象, id 字段可以为空，数据库设置了自增, status字段0表示开启,1表示关闭,默认开启") ProductSaveReqVO reqVO) {
        productService.createProduct(reqVO);
    }

}
