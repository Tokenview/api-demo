package com.tokenview.api.client;

import com.alibaba.fastjson.JSON;
import com.tokenview.api.config.APIConfiguration;
import com.tokenview.api.constant.APIConstants;
import com.tokenview.api.exception.APIException;
import com.tokenview.api.result.HttpResult;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.io.IOException;


/**
 * TokenView API Client
 *
 * @author Moilk
 * @version 1.0.0
 * @date 2021/1/20 11:13
 */
public class APIClient {

    private final APIConfiguration config;
    private final APICredentials credentials;
    private final OkHttpClient client;
    private final Retrofit retrofit;
    private final ApiHttp apiHttp;
    /**
     * Initialize the apis client
     */
    public APIClient(final APIConfiguration config) {
        if (config == null || StringUtils.isEmpty(config.getEndpoint())) {
            throw new RuntimeException("The APIClient params can't be empty.");
        }
        this.config = config;
        this.credentials = new APICredentials(config);
        this.client = new APIHttpClient(config, this.credentials).client();
        this.retrofit = new APIRetrofit(config, this.client).retrofit();
        this.apiHttp = new ApiHttp(config, this.client);
    }

    /**
     * Initialize the retrofit operation service
     */
    public <T> T createService(final Class<T> service) {
        return this.retrofit.create(service);
    }

    public ApiHttp getApiHttp() {
        return this.apiHttp;
    }

    /**
     * Synchronous send request
     */
    //解析
    public <T> T executeSync(final Call<T> call){
        try {

            final Response<T> response = call.execute();

            //获取状态码
            final int status = response.code();
            //获取错误信息
            final String message = new StringBuilder().append(response.code()).append(" / ").append(response.message()).toString();
            //响应成功
            if (response.isSuccessful()) {
                return response.body();
            } else if (APIConstants.resultStatusArray.contains(status)) {
                final HttpResult result = JSON.parseObject(new String(response.errorBody().bytes()), HttpResult.class);
                if(result.getCode() == 0 && result.getMessage() == null){
                    throw new APIException(result.getErrorCode(),result.getErrorMessage());
                }else{
                    throw new APIException(result.getCode(), result.getMessage());
                }
            } else {
                throw new APIException(message);
            }
        } catch (final IOException e) {
            throw new APIException("APIClient executeSync exception.", e);
        }
    }

}
