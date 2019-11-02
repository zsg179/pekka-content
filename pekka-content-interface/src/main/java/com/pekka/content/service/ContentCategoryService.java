package com.pekka.content.service;

import java.util.List;

import com.pekka.common.pojo.EasyUITreeNote;
import com.pekka.common.pojo.PekkaResult;

/**
 * 内容分类接口
 * 
 * @author Lenovo-昱树临风
 *
 */
public interface ContentCategoryService {
	/**
	 * 获取内容分类列表
	 * 
	 * @param parentId
	 *            当前节点id，也是子节点的父id
	 * @return
	 */
	List<EasyUITreeNote> getContentCategoryList(long parentId);

	/**
	 * 新增节点
	 * 
	 * @param parentId
	 *            父节点id
	 * @param text
	 *            新增节点的名字
	 * @return
	 */
	PekkaResult addContentCategory(long parentId, String text);

	/**
	 * 重命名节点
	 * 
	 * @param id
	 *            节点id
	 * @param text
	 *            节点名字
	 * @return
	 */
	PekkaResult updateContentCategory(long id, String text);

	/**
	 * 删除节点
	 * 
	 * @param id
	 *            节点id
	 * @return
	 */
	PekkaResult deleteContentCategory(long id);

}
