package com.nnit.pb.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnit.pb.auth.dao.SysUserDao;
import com.nnit.pb.auth.pojo.SysUser;
import com.nnit.pb.auth.service.SysUserService;
@Service
@Transactional(readOnly = true)
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	
	@Override
	public SysUser findByUsername(String username) {
		return sysUserDao.findByUsername(username);
	}

	@Override
	public SysUser findByWxUserId(String wxUserId) {
		return sysUserDao.findByWxUserId(wxUserId);
	}
}
