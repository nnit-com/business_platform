package com.nnit.pb.auth.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnit.pb.auth.dao.SysRoleDao;
import com.nnit.pb.auth.service.SysRoleService;
@Service
@Transactional(readOnly = true)
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;

	@Override
	public List<String> getRolesByUserid(Long userid) {
		return sysRoleDao.getRolesByUserid(userid);
	}
	

}
