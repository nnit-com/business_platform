package com.nnit.pb.task.service;

import java.util.List;

import com.nnit.pb.task.pojo.SysUserTask;

public interface SysUserTaskService {
	public List<SysUserTask> findAllByUserid(Long userId);
	public SysUserTask save(SysUserTask sysUserTask);
	public SysUserTask getById(Long id);
}
