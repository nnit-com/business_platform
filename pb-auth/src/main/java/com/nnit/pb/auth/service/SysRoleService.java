package com.nnit.pb.auth.service;

import java.util.List;



public interface SysRoleService {
	public List<String> getRolesByUserid(Long userid);
}
