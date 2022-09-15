package com.nnit.pb.task.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nnit.pb.task.pojo.SysUserTask;


public interface SysUserTaskDao extends JpaRepository<SysUserTask,Long> {
	public List<SysUserTask> findAllByuserId(Long userId);

}