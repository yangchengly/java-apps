package com.avaya.aps.httpclient.VxmlHttpClientSimulator.fullcoverage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.avaya.aps.httpclient.VxmlHttpClientSimulator.PropertiesUtil;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.SubmitAttributes;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.VxmlParser;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions.AbstractAction;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions.NameListConstants;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu.Menu;

public class CascadeHttpThread extends AbstractAction implements Runnable {

	private String baseUrl = "http://99.48.237.25:8081/Main/";
	private CloseableHttpClient httpClient = null;

	private int threadNum = 0;
	private int step = 0;
	private static int threadCount = 0;

	private Menu currentMenu;

	private String method;
	private String url;
	private String namelist;
	private List<NameValuePair> nvps = null;
	
	/*
	 * The variable is going to be used to reserve call trace.  
	 */
//	StringBuffer sb = new StringBuffer(100);

	public CascadeHttpThread(int threadNum) {
		this.threadNum = threadNum;
	}

	public CascadeHttpThread(String method, String url, String namelist, List<NameValuePair> nvps) {
		threadCount++;
		threadNum = threadCount;
		
		this.method = method;
		this.url = url;
		this.namelist = namelist;
		this.nvps = nvps;
	}

	public void hierachicalActionPerformed(String method, String url,
			String namelist) {

		System.out.println();
		System.out.println("threadCount:" + threadCount + ";threadNum:" + threadNum + ";Step:" + step);
		System.out.println("method:" + method + ";url:" + url + ";namelist:"
				+ namelist);
//		
//		Stack<String> stk = new Stack<String>();

		step++;
		if (step > 20)
			return;		

		if (this.httpClient == null) {
			this.httpClient = HttpClients.createDefault();
		}
		
		if ("post".equalsIgnoreCase(method)) {
			if (this.nvps == null)
				this.nvps = this.getNameList(
						this.getModuleName(url), namelist, 0);	
			
			System.out.println(this.convertToString(nvps));
		}

		HttpRequestBase httpRequest = this.getHttpRequestBase(method, url,
				this.nvps, 0);
		
		nvps = null;

		try {
			CloseableHttpResponse httpResponse = httpClient
					.execute(httpRequest);

			String statusLine = httpResponse.getStatusLine().toString();

			System.out.println("statusLine:" + statusLine);
			HttpEntity httpEntity = httpResponse.getEntity();

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode == 200) {
				if (httpEntity.isStreaming()) {
					try {
						List<SubmitAttributes> listAttrs = VxmlParser
								.getSubmitAttribute(httpEntity.getContent(),
										currentMenu,threadNum, step);

						if (listAttrs.size() == 0) {
							System.out.println("Reoccurred!");
							EntityUtils.consume(httpEntity);
							httpResponse.close();

							Thread.sleep(2000);
							this.hierachicalActionPerformed(method, url,
									namelist);
						} else if (listAttrs.size() == 1) {
							url = baseUrl + listAttrs.get(0).getNext();

							namelist = listAttrs.get(0).getNamelist();
							method = listAttrs.get(0).getMethod();

							try {
								EntityUtils.consume(httpEntity);
								httpResponse.close();

								Thread.sleep(2000);

								this.hierachicalActionPerformed(method, url,
										namelist);

							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							for (int i = 1; i < listAttrs.size(); i++) {
								
								SubmitAttributes attrs = listAttrs.get(i);
								url = baseUrl + attrs.getNext();

								namelist = attrs.getNamelist();
								method = attrs.getMethod();
								
								List<NameValuePair> l = attrs.getList();

								try {
									EntityUtils.consume(httpEntity);
									httpResponse.close();

									Thread.sleep(2000);

									new Thread(new CascadeHttpThread(method, url,
											namelist, l)).start();

								} catch (IOException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
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

					Thread.sleep(2000);
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
		this.hierachicalActionPerformed(this.method, this.url, this.namelist);
	}

	public static void main(String[] args) {

		int ani = 3000;
		String ucidPrefix = "0000100671";
		long ucid = 1467265209;

		PropertiesUtil.loadAllProperties("conf");

		List<NameValuePair> sesNameList = PropertiesUtil.findNamelist("Main",
				"session");

		for (int i = 0; i < 1; i++) {

			ani += 1;
			ucid += 1;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			for (int j = 0; j < sesNameList.size(); j++) {
				String key = sesNameList.get(j).getName();

				if (key.equalsIgnoreCase("session___ani")) {
					nvps.add(new BasicNameValuePair("session___ani", String
							.valueOf(ani)));
				} else if (key.equalsIgnoreCase("session___sessionlabel")) {
					nvps.add(new BasicNameValuePair("session___sessionlabel",
							ucidPrefix + ucid));
				} else if (key.equalsIgnoreCase("session___ucid")) {
					nvps.add(new BasicNameValuePair("session___ucid",
							ucidPrefix + ucid));
				} else {
					nvps.add(sesNameList.get(j));
				}
			}
			PropertiesUtil.putSession(i, nvps);

			new Thread(new CascadeHttpThread("get",
					"http://99.48.237.25:8081/Main/Start", "", null)).start();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
