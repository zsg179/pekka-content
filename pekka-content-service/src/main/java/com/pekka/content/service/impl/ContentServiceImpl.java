package com.pekka.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.content.service.ContentService;
import com.pekka.mapper.TbContentMapper;
import com.pekka.pojo.TbContent;
import com.pekka.pojo.TbContentExample;
import com.pekka.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Override
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 20;
		}
		// 设置分页
		PageHelper.startPage(page, rows);
		// 设置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		// 得到分页信息
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		// 封装返回值
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(pageInfo.getList());
		result.setTotal((int) pageInfo.getTotal());
		return result;
	}

	@Override
	public PekkaResult saveContent(TbContent tbContent) {
		// 补全pojo
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		// 执行插入
		contentMapper.insert(tbContent);
		return PekkaResult.ok();
	}

	@Override
	public PekkaResult editContent(TbContent tbContent) {
		tbContent.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(tbContent);
		return PekkaResult.ok();
	}

	@Override
	public PekkaResult deleteContent(Long[] ids) {
		for (Long id : ids) {
			contentMapper.deleteByPrimaryKey(id);
		}
		return PekkaResult.ok();
	}

	@Override
	public List<TbContent> getContentByCid(Long cid) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExample(example);
		return list;
	}

}
