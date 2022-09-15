package com.nnit.pb.auth.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nnit.pb.auth.pojo.SysUser;


public interface SysUserDao extends JpaRepository<SysUser,Long> {
	
	public SysUser findByUsername(String username);
	public SysUser findByWxUserId(String wxUserId);

}