package com.pekka.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pekka.common.pojo.EasyUITreeNote;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.content.service.ContentCategoryService;
import com.pekka.mapper.TbContentCategoryMapper;
import com.pekka.pojo.TbContentCategory;
import com.pekka.pojo.TbContentCategoryExample;
import com.pekka.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNote> getContentCategoryList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		// 设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		// 执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNote> resultList = new ArrayList<>();
		// 遍历查询结果
		for (TbContentCategory tbContentCategory : list) {
			// 封装每个结果
			EasyUITreeNote easyUITreeNote = new EasyUITreeNote();
			easyUITreeNote.setId(tbContentCategory.getId());
			easyUITreeNote.setText(tbContentCategory.getName());
			easyUITreeNote.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			// 加入到返回list中
			resultList.add(easyUITreeNote);
		}
		return resultList;
	}

	@Override
	public PekkaResult addContentCategory(long parentId, String text) {
		TbContentCategory contentCategory = new TbContentCategory();
		// 补全pojo
		contentCategory.setName(text);
		contentCategory.setParentId(parentId);
		// 1(正常),2(删除)
		contentCategory.setStatus(1);
		// 设置排序，默认为1
		contentCategory.setSortOrder(1);
		// 是否为父节点
		contentCategory.setIsParent(false);
		// 创建时间
		contentCategory.setCreated(new Date());
		// 更新时间
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(contentCategory);
		// 检查父节点状态
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			// 父节点是子节点,更新为父节点
			parent.setIsParent(true);
			parent.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return PekkaResult.ok(contentCategory);
	}

	@Override
	public PekkaResult updateContentCategory(long id, String text) {
		// 先找到要更新的节点
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(text);
		contentCategory.setUpdated(new Date());
		// 执行更新
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return PekkaResult.ok(contentCategory);
	}

	@Override
	public PekkaResult deleteContentCategory(long id) {
		deleteContentCategoryFun1(id);

		deleteContentCategoryFun2(id);
		return PekkaResult.ok();

	}

	public void deleteContentCategoryFun1(long id) {
		// 查询是否有子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		int j = list.size();
		if (j > 0) {
			// 有子节点
			// 遍历每个子节点
			/*
			 * for (TbContentCategory contentCategory : list) { // 递归
			 * deleteContentCategoryFun(contentCategory.getId()); }
			 */
			for (int i = 0; i < j; i++) {
				// 递归
				deleteContentCategoryFun1(list.get(i).getId());
			}
		}
		// 没有子节点,更改状态为2即代表删除
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 1-正常,2-删除
		tbContentCategory.setStatus(2);
		tbContentCategory.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);

	}

	public void deleteContentCategoryFun2(long id) {
		// 检查父节点状态
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
		// 查看是否 还有子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parent.getId());
		// 并且子节点状态正常的 1-正常，2-删除
		criteria.andStatusEqualTo(1);
		List<TbContentCategory> list2 = contentCategoryMapper.selectByExample(example);
		// 没有子节点了
		if (list2.size() == 0) {
			// 改变父节点状态
			parent.setIsParent(false);
			parent.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKeySelective(parent);
		}
	}

}
