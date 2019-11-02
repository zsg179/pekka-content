package com.pekka.content.service;

import java.util.List;

import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.pojo.TbContent;

/**
 * 内容服务接口
 * 
 * @author Lenovo-昱树临风
 *
 */
public interface ContentService {
	/**
	 * 获取内容列表
	 * 
	 * @param categoryId
	 *            分类id
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页的记录数
	 * @return
	 */
	EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows);

	/**
	 * 新增内容
	 * 
	 * @param tbContent
	 * @return
	 */
	PekkaResult saveContent(TbContent tbContent);

	/**
	 * 编辑内容
	 * 
	 * @param tbContent
	 * @return
	 */
	PekkaResult editContent(TbContent tbContent);

	/**
	 * 删除内容
	 * 
	 * @param tbContent
	 * @return
	 */
	PekkaResult deleteContent(Long[] ids);

	/**
	 * 根据分类id查询内容
	 * 
	 * @param cid
	 *            分类id
	 * @return
	 */
	List<TbContent> getContentByCid(Long cid);

}
