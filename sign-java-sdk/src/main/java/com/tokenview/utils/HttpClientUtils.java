package com.tokenview.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;

@Deprecated
@Slf4j
public final class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public static String get(final String url, final Map<String, String> params, final Map<String, String> headerMap) {
        HttpGet httpGet = null;
        try {
            URIBuilder ub = new URIBuilder(url);
            List<NameValuePair> pairs = Lists.newArrayList();

            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }

                ub.setParameters(pairs);
            }
            httpGet = new HttpGet(ub.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return execute(httpGet, headerMap);
    }

    public static String get(final String url, final Map<String, String> headerMap) {
        return get(url, null, headerMap);
    }

    public static JSONObject getReturnObject(final String url, final Map<String, String> headerMap) {
        String str = get(url, null, headerMap);
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(str);
        } catch (Exception e) {
            log.info("this resquest return not json object" + str);
            json = new JSONObject();
        }

        return json;
    }

    public static JSONArray getReturnArray(final String url, final Map<String, String> headerMap) {
        String str = get(url, null, headerMap);
        JSONArray json = null;
        try {
            json = JSONArray.parseArray(str);
        } catch (Exception e) {
            log.info("this resquest return not json array" + str);
            json = new JSONArray();
        }

        return json;
    }

    public static String post(final String url, final Map<String, String> params,
                              final Map<String, String> headerMap) {
        return post(url, params, headerMap, "utf-8");
    }

    public static String post(final String url, final Map<String, String> params) {
        return post(url, params, null, "utf-8");
    }

    public static String post(final String url, final Map<String, String> params, final Map<String, String> headerMap,
                              final String charset) {
        final HttpPost request = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> paramList = null;
            final Set<Map.Entry<String, String>> entrySet = params.entrySet();
            paramList = new ArrayList<>();
            for (final Iterator<Map.Entry<String, String>> it = entrySet.iterator(); it.hasNext(); ) {
                final Map.Entry<String, String> entry = it.next();
                final String key = entry.getKey();
                final Object value = entry.getValue();
                if (key != null && value != null) {
                    final NameValuePair nvp = new BasicNameValuePair(key, value.toString());
                    paramList.add(nvp);
                }
            }
            try {
                if (StringUtils.isEmpty(charset)) {
                    request.setEntity(new UrlEncodedFormEntity(paramList));
                } else {
                    request.setEntity(new UrlEncodedFormEntity(paramList, Charset.forName(charset)));
                }
            } catch (final Exception e) {
                logger.error("HttpClientUtils post", e);
                return null;
            }
        }
        return execute(request, headerMap);
    }

    public static String post(final String url, final HttpEntity entity, final Map<String, String> headerMap) {
        final HttpPost request = new HttpPost(url);
        request.setEntity(entity);
        return execute(request, headerMap);
    }

    public static String execute(final HttpRequestBase request, final Map<String, String> headerMap) {
        return execute(null, request, headerMap);
    }

    public static String execute(CloseableHttpClient httpclient, final HttpRequestBase request,
                                 final Map<String, String> headerMap) {
        return execute(null, request, headerMap, 30000, 30000);
    }

    public static String execute(final HttpRequestBase request,
                                 final Map<String, String> headerMap, int socketTimeout, int connectTimeout) {
        return execute(null, request, headerMap, socketTimeout, connectTimeout);
    }

    private static String execute(CloseableHttpClient httpclient, final HttpRequestBase request,
                                  final Map<String, String> headerMap, int socketTimeout, int connectTimeout) {
        final StringBuffer log = new StringBuffer(
            "HttpClientUtils execute  method:" + request.getMethod() + " url:" + request.getURI());

        boolean isClose = false;
        if (httpclient == null) {
            httpclient = HttpClients.createDefault();
            isClose = true;
        }
        InputStream resStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        String result = "";

        if (headerMap != null && headerMap.size() > 0) {
            final Iterator<String> iterator = headerMap.keySet().iterator();
            while (iterator.hasNext()) {
                final String key = iterator.next();
                request.setHeader(key, headerMap.get(key));
            }
        }

        try {
            final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(
                connectTimeout)
                .build();
            request.setConfig(requestConfig);
            final CloseableHttpResponse response = httpclient.execute(request);

            try {

                final HttpEntity entity = response.getEntity();

                if (entity != null) {
                    resStream = entity.getContent();
                    try {
                        inputStreamReader = new InputStreamReader(resStream);
                        br = new BufferedReader(inputStreamReader);
                        final StringBuffer resBuffer = new StringBuffer();
                        String resTemp = "";
                        while ((resTemp = br.readLine()) != null) {
                            resBuffer.append(resTemp);
                        }
                        result = resBuffer.toString();
                    } finally {
                        if (br != null) {
                            br.close();
                        }
                        if (inputStreamReader != null) {
                            inputStreamReader.close();
                        }
                        if (resStream != null) {
                            resStream.close();
                        }
                    }
                }
            } finally {
                response.close();
            }
        } catch (final Exception e) {
            request.abort();
            logger.error(log.toString(), e);
        } finally {
            request.abort();
            try {
                if (isClose) {
                    httpclient.close();
                }
            } catch (final IOException e) {
                logger.error(log.toString(), e);
            }
        }
        return result;
    }

    public static JSONObject post(String url, JSONObject params, Map<String, String> header) {
        try {

            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            } else {
                //设置header信息
                httpPost.setHeader("Content-type", "application/json;charset=utf-8");
                httpPost.setHeader("Accept", "application/json;charset=utf-8");
            }

            //
            StringEntity requestEntity = new StringEntity(
                params.toJSONString(),
                ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);

            log.debug("请求地址：" + url);
            log.debug("请求参数：" + params.toJSONString());

            return JSONObject.parseObject(send(httpPost, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String httpsGet(String url, Map<String, String> header) {
        try {
            //创建post方式请求对象
            HttpGet httpGet = new HttpGet(url);

            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //设置header信息
            httpGet.setHeader("Content-type", "application/json;charset=utf-8");
            httpGet.setHeader("Accept", "application/json;charset=utf-8");

            log.debug("请求地址：" + url);

            return send(httpGet, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLSv1.2");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] {trustManager}, null);
        return sc;
    }

    public static String send(HttpRequestBase requestBase, String encoding)
        throws KeyManagementException, NoSuchAlgorithmException, IOException {
        String body = "";
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext))
            .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).setSSLHostnameVerifier(
            new TrustAnyHostnameVerifier()).build();

        //CloseableHttpClient client = HttpClients.createDefault();

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(requestBase);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        client.close();
        connManager.close();
        return body;
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String put(final String url, final HttpEntity entity, final Map<String, String> headerMap) {
        final HttpPut request = new HttpPut(url);
        request.setEntity(entity);
        return execute(request, headerMap);
    }

    public static String put(final String url, final Map<String, String> headerMap) {
        final HttpPut request = new HttpPut(url);
        return execute(request, headerMap);
    }

}
