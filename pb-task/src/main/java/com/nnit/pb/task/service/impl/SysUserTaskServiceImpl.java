package com.nnit.pb.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nnit.pb.task.dao.SysUserTaskDao;
import com.nnit.pb.task.pojo.SysUserTask;
import com.nnit.pb.task.service.SysUserTaskService;
@Service
@Transactional(readOnly = true)
public class SysUserTaskServiceImpl implements SysUserTaskService {
	@Autowired
	private SysUserTaskDao sysUserTaskDao;

	@Override
	public List<SysUserTask> findAllByUserid(Long userId) {
		return sysUserTaskDao.findAllByuserId(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SysUserTask save(SysUserTask sysUserTask) {
		return sysUserTaskDao.save(sysUserTask);
	}

	@Override
	public SysUserTask getById(Long id) {
		return sysUserTaskDao.findById(id).orElse(null);
	}
	


}
