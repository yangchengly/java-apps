package com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.avaya.aps.httpclient.VxmlHttpClientSimulator.SubmitAttributes;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.VxmlParser;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.logger.LoggerFactory;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu.Menu;

public class HttpAction extends AbstractAction implements Runnable {

	// private String baseUrl = "http://99.48.237.25:8081/Main/";
	private String baseUrl;
	private CloseableHttpClient httpClient = null;

	private int threadNum = 0;
	private int step = 0;
	public static int maxStep = 25;

	private Menu currentMenu;
	private Logger logger;

	public HttpAction(int threadNum) {
		this.threadNum = threadNum;
		logger = LoggerFactory.getLogger(String.valueOf(threadNum));
	}

	public HttpAction(int threadNum, String baseUrl) {
		this.baseUrl = baseUrl;
		this.threadNum = threadNum;
		logger = LoggerFactory.getLogger(String.valueOf(threadNum));
	}
	
	public void sessionTerminated(String url) {
		if (this.httpClient == null) {
			this.httpClient = HttpClients.createDefault();
		}
		
		System.out.println();
		step++;	
		System.out.println("Request:" + step);
		
		System.out.println("Session Terminated URL:" + url);
		HttpRequestBase httpRequest = this.getHttpRequestBase("get", url, null, threadNum);
		
		try {
			CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
			
			System.out.println("Result of Session Termination Operation: " + httpResponse.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void hierachicalActionPerformed(String method, String url, String namelist) {

		step++;		

		System.out.println();
		System.out.println("Request:" + step);
		System.out.println("method:" + method + ";url:" + url + ";namelist:" + namelist);

		if (this.httpClient == null) {
			this.httpClient = HttpClients.createDefault();
		}

		logger.info("Step:" + step);
		logger.info("method:" + method + ";url:" + url);

		List<NameValuePair> nvps = null;
		List<NameValuePair> list = null;

		if ("post".equalsIgnoreCase(method)) {
			nvps = this.getNameList(this.getModuleName(url), namelist, threadNum);

			if (nvps == null) {
				new RuntimeException("Didn't find related properties for the namelist").printStackTrace();
				return;
			}

			list = this.getMenuOptionAndUpdate(nvps);
			logger.info(this.convertToString(list));
			logger.info(this.convertToString(nvps));

			System.out.println(this.convertToString(list));
			System.out.println(this.convertToString(nvps));
		}

		HttpRequestBase httpRequest = this.getHttpRequestBase(method, url, list, threadNum);

		try {
			long startTime = System.currentTimeMillis();
			CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);

			String statusLine = httpResponse.getStatusLine().toString();
			System.out.println("statusLine:" + statusLine);
			System.out.println(httpResponse.toString());

			Header[] headers = httpResponse.getHeaders("TerminationURL");
			if (headers.length > 0) {
				System.out.println("Headers:" + headers[0].getName() + "|" + headers[0].getValue());
				if (step > maxStep) {
					this.sessionTerminated(headers[0].getValue());
					return;
				}
					
			}

			long endTime = System.currentTimeMillis();

			logger.info("statusLine:" + statusLine);
			logger.info("Responded time in milliseconds:" + (endTime - startTime));
			logger.info("");

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			logger.info("statusCode:" + statusCode);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream content = httpEntity.getContent();

			if (statusCode == 200) {
				if (httpEntity.isStreaming()) {

					try {
						List<SubmitAttributes> listAttrs = VxmlParser.getSubmitAttribute(content, currentMenu,
								threadNum, step);

						if (listAttrs.size() == 0) {
							System.out.println("Reoccurred!");
							EntityUtils.consume(httpEntity);
							httpResponse.close();

							Thread.sleep(200);
							this.hierachicalActionPerformed(method, url, namelist);
						}

						for (SubmitAttributes attrs : listAttrs) {

							url = baseUrl + attrs.getNext();

							namelist = attrs.getNamelist();
							method = attrs.getMethod();

							if (listAttrs.size() > 1 && !VxmlParser.parseMenuOption(url, namelist, attrs, method))
								continue;

							try {
								EntityUtils.consume(httpEntity);
								httpResponse.close();

								Thread.sleep(200);
								this.hierachicalActionPerformed(method, url, namelist);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception ex) {
						ex.printStackTrace();

					}
				}
			} else if (statusCode == 302) {
				System.out.println("Entered 302");

				method = "post";
				url = httpResponse.getLastHeader("Location").getValue();
				namelist = NameListConstants.SESSION_NAMELIST;

				System.out.println("Location:" + url);

				baseUrl = this.getBaseUrl(url);

				System.out.println("baseUrl:" + baseUrl);

				try {
					EntityUtils.consume(httpEntity);
					httpResponse.close();

					Thread.sleep(200);
					this.hierachicalActionPerformed(method, url, namelist);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		this.hierachicalActionPerformed("get", baseUrl + "/Start?session___sessionid=scccaepmpp02103-" + System.currentTimeMillis() + "-1&__VPapploggingurl=https%3a%2f%2f99%2e48%2e237%2e26%2faxis%2fservices%2fVPReport4&__VPapplog=%2faxis2%2fservices%2fVPAppLogService&__VPvpms=99%2e48%2e237%2e26&__VPappvars=%2faxis2%2fservices%2fVPAppVarsService&__VPVarAppDate=0&__VPVarAppURL=https%3a%2f%2f99%2e48%2e237%2e26%2faxis%2fservices%2fVPAppRuntimeVars&__VPVarGlobalDate=1465217273494&__VPbreadcrumb=disabled&__VPmaxbackuplogfiles=10&__VPlogname=%25default%25KO6D10DncVEVCIxt&__VPlogpassword=kUKOMTj8yJgXS97kRDWrxuKX9Myce8yxP3TV3%2bkeXY45ZjRSeuz2PtbO9Csyvaa%2f&__VPloglevel=Info&__VPappname=0%3aCMBMain", "");
	}
}
