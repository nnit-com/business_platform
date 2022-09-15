package com.nnit.pb.auth.service;

public interface LdapService {
	boolean authUser(String account, String password);
}
