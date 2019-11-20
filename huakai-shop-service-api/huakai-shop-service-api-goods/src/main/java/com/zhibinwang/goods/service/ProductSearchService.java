package com.zhibinwang.goods.service;

import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.goods.output.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author zhibin.wang
 * @desc 产品搜索接口
 **/
@Api(tags = "商品搜索接口")
@Validated
public interface ProductSearchService {


    @ApiOperation(value = "搜索商品功能")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "搜索关键字")
    })
    @PostMapping("searchProduct")
    BaseResponse<List<ProductDto>> searchProduct(String name);
}
