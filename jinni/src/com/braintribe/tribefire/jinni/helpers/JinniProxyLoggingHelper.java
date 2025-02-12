package com.braintribe.tribefire.jinni.helpers;

import com.braintribe.logging.Logger;

/**
 * @author peter.gazdik
 */
public class JinniProxyLoggingHelper {

	private static final Logger log = Logger.getLogger(JinniProxyLoggingHelper.class);

	private static int proxyProps = 0;

	public static void logProxyInfo() {
		logProxyPropertyIfPresent("http.proxyHost");
		logProxyPropertyIfPresent("http.proxyPort");
		logProxyPropertyIfPresent("https.proxyHost");
		logProxyPropertyIfPresent("https.proxyPort");
		logProxyPropertyIfPresent("socksProxyHost");
		logProxyPropertyIfPresent("socksProxyPort");

		if (proxyProps == 0)
			log.debug("Proxy not configured for this JVM.");
	}

	private static void logProxyPropertyIfPresent(String p) {
		String v = System.getProperty(p);
		if (v == null)
			return;
		log.info("JVM proxy config: " + p + " = " + v);
		proxyProps++;
	}

}
