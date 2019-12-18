package io.renren.modules.lamp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author linton
 * @email lintonhank@gmail.com
 * @date 2019-12-17 15:04:40
 */
@Data
@TableName("tb_lamp")
public class LampEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 产品编号
	 */
	private String num;
	/**
	 * 产品类别
	 */
	private Integer categoryid;
	/**
	 * 亮灭状态，0是灭，1是亮，默认0
	 */
	private Integer status;
	/**
	 * 亮度，默认50
	 */
	private Integer brightness;
	/**
	 * 设备是否在线，0离线，1在线，默认1
	 */
	private Integer online;
	/**
	 * 设备是否损坏，0没有，1损坏，默认0
	 */
	private Integer damage;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 创建时间
	 */
	private Date createtime;

}
