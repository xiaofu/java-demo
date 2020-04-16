package com.siteparser.www.task;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Request;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.net.HttpHeaders;

public class Test {
	private static String USER_AGNET = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.100 Safari/533237.36";
	private static String STOCK_GRID_LIST_URL = "http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/%s/ajax/1/";
	private static String STOCK_DETAIL_URL1 = "http://quote.eastmoney.com/unify/r/1.%s";
	private static String STOCK_DETAIL_URL0 = "http://quote.eastmoney.com/unify/r/0.%s";
	private static String KLINES_MONTH1 = "http://pdfm.eastmoney.com/EM_UBG_PDTI_Fast/api/js?token=%s&rtntype=6&id=%s2&type=mk&authorityType=fa&cb=%s";
	private static String KLINES_MONTH2 = "http://pdfm.eastmoney.com/EM_UBG_PDTI_Fast/api/js?token=%s&rtntype=6&id=%s1&type=mk&authorityType=fa&cb=%s";
	private static String KLINIES_DAY2 = "http://pdfm.eastmoney.com//EM_UBG_PDTI_Fast/api/js?token=%s&rtntype=6&id=%s2&type=k&authorityType=&cb=%s";
	private static String KLINIES_DAY1 = "http://pdfm.eastmoney.com//EM_UBG_PDTI_Fast/api/js?token=%s&rtntype=6&id=%s1&type=k&authorityType=&cb=%s";
	private static String FINANCE_DEBT = "http://f10.eastmoney.com/NewFinanceAnalysis/zcfzbAjax?companyType=4&reportDateType=0&reportType=1&endDate=&code=%s";
	private static String GRID_LIST = "http://quote.eastmoney.com/center/gridlist.html";
	private static String DETAIL_URL = "http://stockpage.10jqka.com.cn/%s/";
	private static Set<String> STOCK_CODES = new CopyOnWriteArraySet<>();
	private static Map<String, Tuple> STOCK_KLINES = new ConcurrentHashMap<>();
	private static Map<String, JsonNode> STOCK_DELT = new ConcurrentHashMap<>();
	private static Set<String> ERROR_STOCK_CODES = new CopyOnWriteArraySet<>();
	static ObjectMapper mapper = new ObjectMapper();
	static Map<String, String> headers;
	// static Map<String, String> cookies;

	public static void main(String[] args) throws IOException {

		// 采集所STOCK CODE
		java.util.stream.IntStream.rangeClosed(1, 187).parallel().forEach(i -> {
			Stopwatch watch = Stopwatch.createStarted();
			Document doc = null;
			try {
				doc = Jsoup.connect(String.format(STOCK_GRID_LIST_URL, i)).userAgent(USER_AGNET).get();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			doc.getElementsByClass("m-pager-table").select("tbody").select("tr").parallelStream()
					.forEach(y -> STOCK_CODES.add(y.child(1).text()));
			watch.stop();
			System.out.println(i + " ,time:" + watch.elapsed(TimeUnit.MILLISECONDS));
		});
		System.out.println("counts:" + STOCK_CODES.size());
		CloseableHttpClient httpclient = HttpClients.createDefault();

		headers = new HashMap<String, String>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.5,en-US;q=0.3");
		headers.put("Cache-Control", "no-cache");
		// headers.put("Cookie","HAList=a-sz-300412-%u8FE6%u5357%u79D1%u6280%2Ca-sz-002255-%u6D77%u9646%u91CD%u5DE5%2Ca-sz-300155-%u5B89%u5C45%u5B9D%2Ca-sz-300059-%u4E1C%u65B9%u8D22%u5BCC%2Ca-sh-603009-%u5317%u7279%u79D1%u6280%2Ca-sz-300499-%u9AD8%u6F9C%u80A1%u4EFD%2Ca-sz-300805-%u7535%u58F0%u80A1%u4EFD%2Ca-sz-300654-%u4E16%u7EAA%u5929%u9E3F%2Ca-sz-300471-%u539A%u666E%u80A1%u4EFD%2Ca-sz-300168-%u4E07%u8FBE%u4FE1%u606F%2Ca-sz-002613-%u5317%u73BB%u80A1%u4EFD");
		// em-quote-version=topspeed; qgqp_b_id=b55497b55fc5ef8735916ddcddd1daec;
		// em_hq_fls=js; emhq_picfq=0; intellpositionL=1077.6px; intellpositionT=455px;
		// st_si=21411284722864; st_sn=88;
		// st_psi=20200217161731691-111000300841-3881877096; st_asi=delete;
		// cowCookie=true; st_pvi=31311639613615; st_sp=2019-07-05%2017%3A01%3A51;
		// st_inirUrl=http%3A%2F%2Fquote.eastmoney.com%2Fconcept%2Fsz300059.html");
		// 采集前复权，TODO:需要改为并行流
		// STOCK_CODES.clear();
		// STOCK_CODES.add("600619");
		STOCK_CODES.parallelStream().forEach(code -> {
			Document doc = null;
			try {
				Response response = null;
				Thread.sleep((long) (1000l * ThreadLocalRandom.current().nextFloat()));
				// fetch normal page
				response = Jsoup.connect(String.format(STOCK_DETAIL_URL1, code)).ignoreHttpErrors(true).timeout(1000*600)
						.referrer(GRID_LIST).headers(headers).execute();
				if (response.statusCode() != 200 || response.url().toString().endsWith("404.html")) {
					Thread.sleep((long) (1000l * ThreadLocalRandom.current().nextFloat()));
					response = Jsoup.connect(String.format(STOCK_DETAIL_URL0, code)).ignoreHttpErrors(true).timeout(1000*600)
							.userAgent(USER_AGNET).headers(headers).referrer(GRID_LIST).execute();
				}
				if (response.statusCode() != 200 || response.url().toString().endsWith("404.html")) {
					ERROR_STOCK_CODES.add(code);
					return;
				}
				doc = response.parse();

				// 总市值
				// Document detailDoc =Jsoup.connect(String.format(DETAIL_URL,
				// code)).userAgent(USER_AGNET).headers(headers).referrer("http://q.10jqka.com.cn/").get();
				// Document
				// totalDoc=Jsoup.connect(detailDoc.select("iframe").first().absUrl("src")).userAgent(USER_AGNET).headers(headers).referrer("http://q.10jqka.com.cn/").get();
				// String totalValue =totalDoc.getElementById("tvalue").text();
				String totalValue = "";
				// 获取财务分析URL
				String requestUrl = response.url().toString();

				// fectch 极速版 URL
				String jishuUrl = "";
				String caiwuUrl = "";
				try {
					caiwuUrl = doc.getElementsByClass("cells").select("a").parallelStream()
							.filter(y -> "财务分析".equalsIgnoreCase(y.text())).findFirst().get().absUrl("href");
					jishuUrl = doc.getElementsByClass("jsb").stream().findFirst().get().absUrl("href");
				} catch (NoSuchElementException e) {
					ERROR_STOCK_CODES.add(code);
					// 直接退出
					return;
				}
				String tokenUrl = "";
				while (true) {
					Response jishuResponse = Jsoup.connect(jishuUrl).userAgent(USER_AGNET).headers(headers).timeout(1000*600)
							.referrer(requestUrl).execute();
					if (jishuResponse.url().toString().endsWith("404.html"))
					{
						Thread.sleep((long) (1000l * ThreadLocalRandom.current().nextFloat()));
						continue;
					}
					// fetch token url

					tokenUrl = jishuResponse.parse().getElementById("chart-container").selectFirst("img").absUrl("src");
					break;

				}
				// parse url and get token
				URIBuilder uriBuilder = new URIBuilder(tokenUrl);
				String token = uriBuilder.getQueryParams().stream().filter(y -> y.getName().equalsIgnoreCase("token"))
						.findFirst().get().getValue();
				// fetch 前复权
				String qianfuquan = getKlines(httpclient, jishuUrl, code, token, KLINES_MONTH1, KLINES_MONTH2,
						"2015-08-31");

				// 获取2.3日的收盘价
				String showpan = getKlines(httpclient, jishuUrl, code, token, KLINIES_DAY2, KLINIES_DAY1, "2020-02-03");
				String showpan612 = getKlines(httpclient, jishuUrl, code, token, KLINIES_DAY2, KLINIES_DAY1,
						"2015-06-12");
				Tuple tuple = new Tuple();
				tuple.setQianfuquan(qianfuquan);
				tuple.setShoupan(showpan);
				tuple.setTotal(totalValue);
				tuple.setShowpan612(showpan612);
				STOCK_KLINES.put(code, tuple);
				// 采集资产负债表
				uriBuilder = new URIBuilder(caiwuUrl);
				String realCode = uriBuilder.getQueryParams().stream().filter(y -> y.getName().equalsIgnoreCase("code"))
						.findFirst().get().getValue();
				HttpGet httpGet = new HttpGet(String.format(FINANCE_DEBT, realCode));
				httpGet.addHeader("Referer", caiwuUrl);
				httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				httpGet.addHeader("Accept-Encoding", "gzip, deflate");
				CloseableHttpResponse debtDataResponse;
				debtDataResponse = httpclient.execute(httpGet);
				if (debtDataResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(debtDataResponse.getEntity());
					result = StringUtils
							.removeEnd(StringUtils.removeStart(StringEscapeUtils.unescapeJson(result), "\""), "\"");
					// System.out.println(result);
					ArrayNode debtData = (ArrayNode) mapper.readTree(result);
					STOCK_DELT.put(code, debtData.get(0));
				}

			} catch (Exception e) {
				throw new RuntimeException(code, e);
			}
		});
		// 合并数据输出成CSV
		StringBuffer buffer = new StringBuffer();
		STOCK_CODES.parallelStream().forEach(code -> {
			Tuple tuple = STOCK_KLINES.get(code);
			if (tuple == null)
				return;

			ObjectNode data = (ObjectNode) STOCK_DELT.get(code);

			data.put("2020-02-03收盘价", tuple.getShoupan());
			data.put("2015-06-12收盘价", tuple.getShowpan612());
			data.put("市值", tuple.getTotal());
			data.put("2015-08-31前复权", tuple.getQianfuquan());

			if (buffer.length() == 0) {
				synchronized (buffer) {
					if (buffer.length() == 0) {
						StringBuilder builder = new StringBuilder();
						// builder.append("code,").append("2020-02-03收盘价,").append("2020-06-12收盘价,").append("2018-08-31前复权,").append("市值");

						Iterator<String> iter = data.fieldNames();
						while (iter.hasNext()) {
							String field = iter.next();
							builder.append(field).append(",");
						}

						buffer.append(StringUtils.removeEnd(builder.toString(), ",") + "\r\n");
					}
				}
			}
			StringBuilder builder = new StringBuilder();

			data.forEach(item -> {
				builder.append(item.asText()).append(",");
			});

			// builder.append(code).append(",").append(tuple.getShoupan()).append(",").append(tuple.getShowpan612()).append(",").append(tuple.getQianfuquan()).append(",").append(tuple.getTotal());
			buffer.append(StringUtils.removeEnd(builder.toString(), ",") + "\r\n");

		});

		System.out.println("error_counts:" + ERROR_STOCK_CODES.size());
		for (String item : ERROR_STOCK_CODES) {
			System.out.println(item);
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("test.csv"), "gb2312");
		writer.append(buffer);
		writer.close();
	}

	private static String getKlines(CloseableHttpClient httpclient, String jishuUrl, String code, String token,
			String url2, String url1, String cmpDate) throws IOException, InterruptedException, URISyntaxException {
		String prefixTime = "jsonp" + Ticker.systemTicker().read();
		HttpGet httpGet = new HttpGet(String.format(url2, token, code, prefixTime));
		httpGet.addHeader("Referer", jishuUrl);
		for (Map.Entry<String, String> item : headers.entrySet()) {
			httpGet.addHeader(item.getKey(), item.getValue());
		}

		int counts = 0;
		while (true) {
			CloseableHttpResponse kresponse = httpclient.execute(httpGet);
			if (kresponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(kresponse.getEntity());
				// System.out.println(result);
				result = StringUtils.removeEnd(StringUtils.removeStart(result, prefixTime + "("), ")");
				try {
					JsonNode jsonData = mapper.readTree(result);
					ArrayNode datas = (ArrayNode) jsonData.get("data");
					for (JsonNode item : datas) {
						String[] klines = item.asText().split(",");
						if (klines[0].equalsIgnoreCase(cmpDate)) {
							return klines[2];
						}
					}
					break;
				} catch (JsonParseException e) {
					if (counts == 2)
						break;
					Thread.sleep((long) (500l * ThreadLocalRandom.current().nextFloat()));
					System.out.println(e);
					httpGet.setURI(new URI(String.format(url1, token, code, prefixTime)));
					counts++;
				}

			} else {
				throw new IOException("response staus is not 200");
			}
		}
		return "";
	}

	private static class Tuple {
		private String qianfuquan;
		private String total;
		private String shoupan;
		private String showpan612;

		public String getShowpan612() {
			return showpan612;
		}

		public void setShowpan612(String showpan612) {
			this.showpan612 = showpan612;
		}

		public String getQianfuquan() {
			return qianfuquan;
		}

		public void setQianfuquan(String qianfuquan) {
			this.qianfuquan = qianfuquan;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getShoupan() {
			return shoupan;
		}

		public void setShoupan(String shoupan) {
			this.shoupan = shoupan;
		}

	}

	public static String convertUnicode(String ori) {
		char aChar;
		int len = ori.length();
		StringBuilder outBuffer = new StringBuilder(len);
		for (int x = 0; x < len;) {
			aChar = ori.charAt(x++);
			if (aChar == '\\') {
				aChar = ori.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = ori.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);

		}
		return outBuffer.toString();
	}

}
