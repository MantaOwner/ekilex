package eki.common.web;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import eki.common.data.AppData;

@ConditionalOnWebApplication
@Component
public class AppDataHolder {

	@Value("${info.app.name}")
	private String appName;

	@Value("${info.app.version}")
	private String appVersion;

	@Value("${server.servlet.session.timeout:30m}")
	private Duration sessionTimeout;

	private AppData appData = null;

	public AppData getAppData() {

		if (appData != null) {
			return appData;
		}

		boolean isTestEnv = false;
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			isTestEnv = StringUtils.containsIgnoreCase(hostname, "test");
		} catch (UnknownHostException e) {
		}

		long sessionTimeoutSec = sessionTimeout.getSeconds();

		appData = new AppData();
		appData.setTestEnv(isTestEnv);
		appData.setAppName(appName);
		appData.setAppVersion(appVersion);
		appData.setSessionTimeoutSec(sessionTimeoutSec);

		return appData;
	}

}
