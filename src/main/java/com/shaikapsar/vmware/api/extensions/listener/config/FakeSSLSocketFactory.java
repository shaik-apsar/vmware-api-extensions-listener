/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;

import com.shaikapsar.vmware.api.extensions.listener.exception.ApplicationException;


/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class FakeSSLSocketFactory {
	
	private FakeSSLSocketFactory() {
	}

	public static SSLSocketFactory getInstance() {
		try {
			return new SSLSocketFactory(new TrustStrategy() {
				public boolean isTrusted(final X509Certificate[] chain,
						final String authType) throws CertificateException {
					return true;
				}

			}, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (KeyManagementException e) {
			throw new ApplicationException(e);
		} catch (UnrecoverableKeyException e) {
			throw new ApplicationException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ApplicationException(e);
		} catch (KeyStoreException e) {
			throw new ApplicationException(e);
		}
	}

}
