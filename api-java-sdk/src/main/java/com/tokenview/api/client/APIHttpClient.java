package com.tokenview.api.client;

import com.tokenview.api.config.APIConfiguration;
import com.tokenview.api.constant.APIConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class APIHttpClient {
    private final APIConfiguration config;
    private final APICredentials credentials;


    public APIHttpClient(final APIConfiguration config, final APICredentials credentials) {
        this.config = config;
        this.credentials = credentials;
    }

    public OkHttpClient client() {
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(this.config.getConnectTimeout(), TimeUnit.SECONDS);
        clientBuilder.readTimeout(this.config.getReadTimeout(), TimeUnit.SECONDS);
        clientBuilder.writeTimeout(this.config.getWriteTimeout(), TimeUnit.SECONDS);
        clientBuilder.retryOnConnectionFailure(this.config.isRetryOnConnectionFailure());
//        clientBuilder.addInterceptor((Interceptor.Chain chain) -> {
//            final Request.Builder requestBuilder = chain.request().newBuilder();
//            final String timestamp = DateUtils.getUnixTime();
//            //打印首行时间戳
////            System.out.println("时间戳timestamp={" + timestamp + "}");
////              设置模拟盘请求头
////            String simulated = "1";
//            requestBuilder.headers(this.headers(chain.request(), timestamp));
//            final Request request = requestBuilder.build();
//            if (this.config.isPrint()) {
//                this.printRequest(request, timestamp);
//            }
//            return chain.proceed(request);
//        });
        return clientBuilder.build();
    }

    //返回请求路径url
    private String url(final Request request) {
        return request.url().toString();
    }
    //将请求方法转变为大写，并返回
    private String method(final Request request) {
        return request.method().toUpperCase();
    }
    //返回请求路径
    private String requestPath(final Request request) {
        String url = this.url(request);
        url = url.replace(this.config.getEndpoint(), APIConstants.EMPTY);
        String requestPath = url;
        if (requestPath.contains(APIConstants.QUESTION)) {
            requestPath = requestPath.substring(0, url.lastIndexOf(APIConstants.QUESTION));
        }
        if(this.config.getEndpoint().endsWith(APIConstants.SLASH)){
            requestPath = APIConstants.SLASH + requestPath;
        }
        return requestPath;
    }

    private String queryString(final Request request) {
        final String url = this.url(request);
        request.body();
        //请求参数为空字符串
        String queryString = APIConstants.EMPTY;
        //如果URL中包含？即存在参数的拼接
        if (url.contains(APIConstants.QUESTION)) {
            queryString = url.substring(url.lastIndexOf(APIConstants.QUESTION) + 1);
        }
        return queryString;
    }

    private String body(final Request request) throws IOException {
        final RequestBody requestBody = request.body();
        String body = APIConstants.EMPTY;
        if (requestBody != null) {
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            body = buffer.readString(APIConstants.UTF_8);
        }
        return body;
    }
}
