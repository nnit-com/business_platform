package com.nnit.pb.auth.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;




import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.api.RemoteWXService;
import com.nnit.pb.api.exception.ResultException;
import com.nnit.pb.api.model.WXCorpInfo;
import com.nnit.pb.api.model.WXLogin;
import com.nnit.pb.auth.dao.SysRoleDao;
import com.nnit.pb.auth.dao.SysUserDao;
import com.nnit.pb.auth.pojo.SysRole;
import com.nnit.pb.auth.pojo.SysUser;
import com.nnit.pb.auth.service.LdapService;
import com.nnit.pb.auth.service.SysRoleService;
import com.nnit.pb.auth.service.SysUserService;
import com.nnit.pb.auth.vm.LoginVM;
import com.nnit.pb.auth.vm.WXLoginVM;
import com.nnit.pb.common.constant.ResultEnum;
import com.nnit.pb.common.constant.WXConstants;
import com.nnit.pb.common.manager.TokenManager;
import com.nnit.pb.common.vm.LoginUser;
import com.nnit.pb.common.vm.ResponseData;

@RestController 
public class TokenController {
	private static final Logger log = LoggerFactory.getLogger(TokenController.class);

	@Autowired
	private LdapService ldapService;
	
	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private RemoteWXService remoteWXService;
	
    @PostMapping("login")
	public ResponseData login(@RequestBody LoginVM loginVM){
    	boolean flag = ldapService.authUser(loginVM.getName(), loginVM.getPassword());
		if (flag == false){
    		throw new ResultException(ResultEnum.USER_AUTH_FAILED);
		}
		SysUser sysUser = sysUserService.findByUsername(loginVM.getName());
		if(sysUser == null){
    		throw new ResultException(ResultEnum.USER_NOT_EXIST);
		}
		
		return generateLoginData(sysUser);    	
	}
    
    @PostMapping("wxlogin")
	public ResponseData wxlogin(@RequestBody WXLoginVM wxLoginVM){
    	log.info(wxLoginVM.getWxCode());
    	JSONObject wxTokenresult = remoteWXService.getToken(new WXCorpInfo());
    	if(wxTokenresult.getIntValue(WXConstants.WX_ERROR_CODE) != 0){
    		throw new ResultException(wxTokenresult.getIntValue(WXConstants.WX_ERROR_CODE), wxTokenresult.getString(WXConstants.WX_ERROR_MEG));
    	}
    	
    	String accessToken = wxTokenresult.getString(WXConstants.WX_ACCESS_TOKEN);
     	WXLogin wxLogin = new WXLogin();   
     	wxLogin.setAccess_token(accessToken);
     	wxLogin.setJs_code(wxLoginVM.getWxCode());
     	JSONObject wxResult = remoteWXService.jscode2session(wxLogin);
     	if(wxResult.getIntValue(WXConstants.WX_ERROR_CODE) != 0){
    		throw new ResultException(wxResult.getIntValue(WXConstants.WX_ERROR_CODE), wxResult.getString(WXConstants.WX_ERROR_MEG));
    	}
     	
     	String wxUserId = wxResult.getString(WXConstants.WX_USER_ID);
		SysUser sysUser = sysUserService.findByWxUserId(wxUserId);
		if(sysUser == null){
    		throw new ResultException(ResultEnum.USER_NOT_EXIST);
		}
		
		return generateLoginData(sysUser);   	
	}
    
    private ResponseData generateLoginData(SysUser sysUser){
		List<String> roleList = sysRoleService.getRolesByUserid(sysUser.getId());
		if(roleList == null || roleList.isEmpty() ){
    		throw new ResultException(ResultEnum.ROLE_NOT_EXIST);
		}
		
		ResponseData responseData = ResponseData.ok();
		LoginUser apiLogin = new LoginUser();	
		apiLogin.setUserId(sysUser.getId());
		apiLogin.setUserCode(sysUser.getUsername());
		apiLogin.setUserRoles(roleList);
		responseData.putDataValue("token", tokenManager.ceateToken(apiLogin));
		return responseData; 
    }
//    @GetMapping("validate")
//	public ResponseData validate(String token){
//		ResponseData responseData = ResponseData.ok();
//		ApiLogin apiLogin = JWT.unsign(token, ApiLogin.class);
//		if(apiLogin != null){
//			responseData.putDataValue("name", apiLogin.getName());
//			responseData.putDataValue("userKey", apiLogin.getUserKey());
//		}else{
//			responseData = ResponseData.customerError();
//		}
//		return responseData;    	
//	}
    @GetMapping("logout")
	public String logout(){
		return "hello world1"; 
		
	}
//    @PostMapping("setUser")
//	public ResponseData setUser(){
//    	SysUser u1 = new SysUser();
//    	u1.setUsername("222");
//    	sysUserDao.save(u1);
//    	SysRole  u2 = new SysRole();
//    	u2.setName("333");
//    	sysRoleDao.save(u2);	
//    	ResponseData re = ResponseData.ok();
//    	re.putDataValue("1", u1);
//    	re.putDataValue("2", u2);
//
//		return re;   	
//	}
//    @GetMapping("getUser")
//	public ResponseData getUser(Long id){
//    	SysUser api = sysUserDao.findById(id).orElse(null);
//    	ResponseData re = ResponseData.ok();
//    	re.putDataValue("1", api);
//
//     	return re;
//	}
}
