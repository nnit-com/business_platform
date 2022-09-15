package com.nnit.pb.auth.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nnit.pb.auth.pojo.SysRole;



public interface SysRoleDao extends JpaRepository<SysRole,Long> {
	
   @Query(value="SELECT name FROM sys_role where role_id in "
			+ "(SELECT role_id FROM sys_users_roles where user_id = :userid )", nativeQuery=true)
	public List<String> getRolesByUserid(@Param("userid") Long userid);
}