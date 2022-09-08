package com.japhet.application.residentsincome.util;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Auditable
@Interceptor
public class AuditInterceptor {

	@Inject
	private Logger LOG;

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		long begin = System.currentTimeMillis();
		try {
			return ic.proceed();
		} finally {
			LOG.info("Took " + (System.currentTimeMillis() - begin));
		}
	}

}
