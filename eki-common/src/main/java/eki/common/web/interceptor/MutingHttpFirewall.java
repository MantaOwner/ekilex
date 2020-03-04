package eki.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import eki.common.exception.MutingRequestRejectedException;

public class MutingHttpFirewall extends StrictHttpFirewall {

	private static Logger logger = LoggerFactory.getLogger(MutingHttpFirewall.class);

	public MutingHttpFirewall() {
		super();
	}

	@Override
	public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException {
		try {
			return super.getFirewalledRequest(request);
		} catch (RequestRejectedException e) {
			String servletPath = request.getServletPath();
			String exceptionMessage = e.getMessage();
			String remoteHost = request.getRemoteHost();
			String userAgent = request.getHeader("User-Agent");
			logger.warn("Hacking attempt? \"{}\" <<< {} - {}", servletPath, remoteHost, userAgent);
			throw new MutingRequestRejectedException(exceptionMessage);
		}
	}

}
