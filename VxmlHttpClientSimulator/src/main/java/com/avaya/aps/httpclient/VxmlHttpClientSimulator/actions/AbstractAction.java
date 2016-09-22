package com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import com.avaya.aps.httpclient.VxmlHttpClientSimulator.PropertiesUtil;

public abstract class AbstractAction {

	protected HttpGet getHttpGet(String url) {

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "*/*");
		httpGet.setHeader("Cache-Control", "max-age=3600");
		httpGet.setHeader("Cache-Control", "max-stale=0");
		httpGet.setHeader("User-Agent", "AvayaVXI/2.0");
		httpGet.setHeader("AVB-Version", "2.1.4");

		/*
		 * Disable auto redirect fuction of httpClient, the default value is
		 * true;
		 */
		RequestConfig requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(false).build();
		httpGet.setConfig(requestConfig);

		return httpGet;
	}

	protected HttpPost getHttpPost(String url) {

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "*/*");
		httpPost.setHeader("Cache-Control", "max-age=3600");
		httpPost.setHeader("Cache-Control", "max-stale=0");
		httpPost.setHeader("User-Agent", "AvayaVXI/2.0");
		httpPost.setHeader("AVB-Version", "2.1.4");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		RequestConfig requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(false).build();
		httpPost.setConfig(requestConfig);

		return httpPost;
	}

	protected String getBaseUrl(String url) {

		String baseUrlContext = url.substring(0, url.indexOf("?"));
		String baseUrl = baseUrlContext.substring(0,
				baseUrlContext.lastIndexOf("/") + 1);
		return baseUrl;
	}

	protected String getModuleName(String baseUrl) {
		return baseUrl.split("/")[3];
	}

	protected HttpRequestBase getHttpRequestBase(String method, String url,
			List<NameValuePair> nvps, int threadNum) {

		if (method != null && method.equalsIgnoreCase("post")) {
			HttpPost httpPost = this.getHttpPost(url);
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return httpPost;
		} else if (method != null && method.equalsIgnoreCase("get")) {
			return this.getHttpGet(url);
		}

		return null;
	}

	protected List<NameValuePair> getNameList(String module, String namelist,
			int threadNum) {
		String node = namelist.split(" ")[0].split("___")[0];

		/*
		 * As an session data is to be shared in each module for one call, but
		 * session should be shared between calls, so the ideal way is to create
		 * a unique session data for each call.
		 */
		if (node != null && node.equals("session"))
			return PropertiesUtil.getSession(threadNum);

		return PropertiesUtil.findNamelist(module, node);
	}

	public String convertToString(List<NameValuePair> nvps) {
		StringBuffer sb = new StringBuffer(200);

		for (NameValuePair nameValuePair : nvps) {
			sb.append(nameValuePair.getName());
			sb.append(":");
			sb.append(nameValuePair.getValue());
			sb.append(";");
		}
		return sb.toString();
	}

	public List<NameValuePair> getMenuOptionAndUpdate(List<NameValuePair> nvps) {
		/*
		 * In order to support defining more options in specified menu for more
		 * than one time.
		 */

		List<NameValuePair> list = new ArrayList<NameValuePair>();

		NameValuePair nvpToBeRemoved = null;
		NameValuePair nvpToBeAdded = null;

		for (NameValuePair nameValuePair : nvps) {

			String name = nameValuePair.getName();
			String value = nameValuePair.getValue();
			
			if(value == null){
				System.out.println(name);
				new RuntimeException("Didn't find value for the menu").printStackTrace();
				return null;
			}

			if (name.indexOf("___value") > 0 && name.startsWith("M")) {
				String[] values = value.split(",");

				if (values.length > 1) {
					list.add(new BasicNameValuePair(name, values[0]));

					nvpToBeRemoved = nameValuePair;
					nvpToBeAdded = new BasicNameValuePair(name,
							value.substring(value.indexOf(',') + 1));
				} 
				else {
					nvpToBeRemoved = nameValuePair;
					list.add(nameValuePair);
					nvpToBeAdded = new BasicNameValuePair(name,null);
				}

			} else {
				list.add(nameValuePair);
				
			}
		}

		if (nvpToBeRemoved != null) {
			nvps.remove(nvpToBeRemoved);
			
		}
		if(nvpToBeAdded !=null){
			nvps.add(nvpToBeAdded);
		}

		return list;
	}
}
