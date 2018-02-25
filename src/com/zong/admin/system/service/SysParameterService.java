package com.zong.admin.system.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.admin.system.bean.SysParameter;
import com.zong.admin.system.dao.SysParameterMapper;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.dao.CommonMapper;
import com.zong.core.exception.ServiceException;

/**
 * @desc sysParameter业务实现类
 * @author zong
 * @date 2017年03月19日
 */
@Service
public class SysParameterService {
	@Autowired
	private SysParameterMapper sysParameterMapper;
	@Autowired
	private CommonMapper commonMapper;

	/**
	 * 新增sysParameter
	 * 
	 * @param sysParameter
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addSysParameter(SysParameter sysParameter) throws Exception {
		check(sysParameter);
		sysParameter.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		sysParameter.setCreateTime(new Date());
		sysParameterMapper.insert(sysParameter);
	}
	
	/**
	 * 校验param_key是否已存在
	 */
	private void check(SysParameter sysParameter) throws Exception {
		List<Result> datas = commonMapper.find("sys_parameter",
				new PageData("param_key", sysParameter.getParamKey()));
		if (!datas.isEmpty()) {
			boolean flag = false;
			if (sysParameter.getId() != null && !sysParameter.getId().equals("")) {
				Result data = datas.get(0);
				if (sysParameter.getId().equals(data.getString("id"))) {
					flag = true;
				}
			}
			if(!flag){
				throw new ServiceException("已存在参数键:"+sysParameter.getParamKey());
			}
		}
	}
	
	/**
	 * 删除sysParameter
	 * 
	 * @param sysParameter
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteSysParameter(SysParameter sysParameter) throws Exception {
		String[] ids = sysParameter.getId().split(",");
		for (String id : ids) {
			sysParameter.setId(id);
			sysParameterMapper.delete(sysParameter);
		}
	}
	
	/**
	 * 修改sysParameter
	 * 
	 * @param sysParameter
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editSysParameter(SysParameter sysParameter) throws Exception {
		check(sysParameter);
		sysParameterMapper.update(sysParameter);
	}

	/**
	 * 根据id查询sysParameter
	 * 
	 * @param sysParameter
	 * @return
	 */
	public SysParameter loadSysParameter(SysParameter sysParameter) {
		return sysParameterMapper.load(sysParameter);
	}

	/**
	 * 分页查询sysParameter
	 * 
	 * @param page
	 * @return
	 */
	public List<SysParameter> findSysParameterPage(Page page) {
		return sysParameterMapper.findSysParameterPage(page);
	}
}