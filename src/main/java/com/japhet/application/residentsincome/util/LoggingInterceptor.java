package com.japhet.application.residentsincome.util;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Loggable
@Interceptor
public class LoggingInterceptor {

	@Inject
	private Logger LOG;

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		LOG.entering(ic.getTarget().getClass().getName(),
					ic.getMethod().getName());
		try {
			return ic.proceed();
		} finally {
			LOG.exiting(ic.getTarget().getClass().getName(),
						ic.getMethod().getName());
		}
	}

}
