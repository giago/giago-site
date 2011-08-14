package com.giago.www.server;

import com.giago.appengine.commons.dao.BaseDao;
import com.giago.appengine.commons.dao.jdo.BaseDaoImpl;
import com.giago.www.shared.ContactRequest;

public class ContactRequestJdo extends BaseDaoImpl<ContactRequest> implements BaseDao<ContactRequest> {

	public ContactRequestJdo() {
		super(ContactRequest.class);
	}

}
