package com.zong.admin.system.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.admin.system.bean.SysMenu;
import com.zong.admin.system.bean.SysRole;
import com.zong.admin.system.bean.SysRoleMenu;
import com.zong.admin.system.dao.SysRoleMapper;
import com.zong.admin.system.dao.SysRoleMenuMapper;
import com.zong.admin.system.dao.SysUserRoleMapper;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

/**
 * @desc sysRole业务实现类
 * @author zong
 * @date 2017年03月19日
 */
@Service
public class SysRoleService {
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysRoleMenuMapper roleMenuMapper;
	@Autowired
	private SysUserRoleMapper userRoleMapper;

	/**
	 * 新增sysRole
	 * 
	 * @param sysRole
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addSysRole(SysRole sysRole) throws Exception {
		sysRole.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		sysRole.setCreateTime(new Date());
		sysRoleMapper.insert(sysRole);
	}
	
	/**
	 * 删除sysRole
	 * 
	 * @param sysRole
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteSysRole(SysRole sysRole) throws Exception {
		String[] ids = sysRole.getId().split(",");
		for (String id : ids) {
			sysRole.setId(id);
			sysRoleMapper.delete(sysRole);
			//删除roleMenu
			roleMenuMapper.deleteByRole(sysRole.getId());
			//删除userRole
			userRoleMapper.deleteByRole(sysRole.getId());
		}
	}
	
	/**
	 * 修改sysRole
	 * 
	 * @param sysRole
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editSysRole(SysRole sysRole) throws Exception {
		sysRoleMapper.update(sysRole);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void saveRoleMenu(SysRole sysRole){
		//先删除roleMenu再添加
		roleMenuMapper.deleteByRole(sysRole.getId());
		List<SysMenu> menus = sysRole.getMenus();
		if(menus==null){
			return;
		}
		for (SysMenu sysMenu : menus) {
			SysRoleMenu roleMenu = new SysRoleMenu();
			roleMenu.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
			roleMenu.setMenuId(sysMenu.getId());
			roleMenu.setRoleId(sysRole.getId());
			roleMenuMapper.insert(roleMenu);
		}
	}

	/**
	 * 根据id查询sysRole
	 * 
	 * @param sysRole
	 * @return
	 */
	public SysRole loadSysRole(SysRole sysRole) {
		return sysRoleMapper.load(sysRole);
	}

	/**
	 * 分页查询sysRole
	 * 
	 * @param page
	 * @return
	 */
	public List<SysRole> findSysRolePage(Page page) {
		return sysRoleMapper.findSysRolePage(page);
	}
	
	/**
	 * 查询全部sysRole
	 * 
	 * @param pageData
	 * @return
	 */
	public List<SysRole> findSysRole(PageData pageData) {
		return sysRoleMapper.findSysRole(pageData);
	}
}