package com.zhibinwang.goods.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "搜索商品信息")
public class ProductDto {

	/** 主键ID */
	@ApiModelProperty(value = "商品Id")
	private Integer id;
	/** 类型ID */
	@ApiModelProperty(value = "类别Id")
	private Integer categoryId;
	/** 名称 */
	@ApiModelProperty(value = "商品名称")
	private String name;
	/** 小标题 */
	@ApiModelProperty(value = "小标题")
	private String subtitle;
	/** 主图像 */
	@ApiModelProperty(value = "主图像")
	private String mainImage;
	/** 小标题图像 */
	@ApiModelProperty(value = "小标题图像")
	private String subImages;
	/** 描述 */
	@ApiModelProperty(value = "详细信息")
	private String detail;
	/** 商品规格 */
	@ApiModelProperty(value = "商品规格")
	private String attributeList;
	/** 价格 */
	@ApiModelProperty(value = "价格")
	private Double price;
	/** 库存 */
	@ApiModelProperty(value = "库存")
	private Integer stock;
	/** 状态 */
	@ApiModelProperty(value = "状态")
	private Integer status;
	/** 乐观锁 */
	@ApiModelProperty(value = "乐观锁")
	private Integer revision;
	/** 创建人 */
	@ApiModelProperty(value = "创建人")
	private String createdBy;
	/** 创建时间 */
	@ApiModelProperty(value = "创建时间")
	private Date createdTime;
	/** 更新人 */
	@ApiModelProperty(value = "更新人")
	private String updatedBy;
	/** 更新时间 */
	@ApiModelProperty(value = "更新时间")
	private Date updatedTime;
}
