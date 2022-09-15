package com.nnit.pb.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import com.nnit.pb.auth.service.LdapService;
@Service
public class LdapServiceImpl implements LdapService {
    @Autowired
    private LdapTemplate ldapTemplate;
	@Override
	public boolean authUser(String account, String password) {
		ldapTemplate.setIgnorePartialResultException(true);   
		EqualsFilter filter = new EqualsFilter("sAMAccountName",account);
		return ldapTemplate.authenticate("", filter.toString(), password);
	}

}
