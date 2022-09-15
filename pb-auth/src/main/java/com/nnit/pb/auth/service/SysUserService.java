package com.nnit.pb.auth.service;

import com.nnit.pb.auth.pojo.SysUser;

public interface SysUserService {
	public SysUser findByUsername(String username);
	
	public SysUser findByWxUserId(String wxUserId);

}
