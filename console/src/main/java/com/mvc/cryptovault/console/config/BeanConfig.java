package com.mvc.cryptovault.console.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.mvc.cryptovault.common.util.JwtHelper;
import com.mvc.cryptovault.console.util.btc.BtcAction;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import lombok.Cleanup;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/11/13 14:44
 */
@Configuration
public class BeanConfig {

    @Value("${jpush.secret}")
    private String MASTER_SECRET;

    @Value("${jpush.app_key}")
    private String APP_KEY;

    @Value("${service.name}")
    private String serviceName;
    @Value("${service.expire}")
    private Long expire;
    @Value("${service.refresh}")
    private Long refresh;
    @Value("${service.base64Secret}")
    private String base64Secret;
    @Value("${eth.geth}")
    public String WALLET_SERVICE;
    @Value("${usdt.propId}")
    private Integer propId;

    @Bean
    public HTreeMap hTreeMap() {
        HTreeMap myCache = DBMaker.heapDB().concurrencyScale(16).make().hashMap("consoleCache")
                .expireMaxSize(10000)
                .expireAfterCreate(1, TimeUnit.HOURS)
                .expireAfterUpdate(1, TimeUnit.HOURS)
                .expireAfterGet(1, TimeUnit.HOURS).create();
        return myCache;
    }

    @Bean
    public JPushClient jPushClient() {
        JwtHelper.serviceName = serviceName;
        JwtHelper.expire = expire;
        JwtHelper.refresh = refresh;
        JwtHelper.base64Secret = base64Secret;
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
        return jpushClient;
    }

    @Bean
    public Quorum quorum() throws IOException {
        return Quorum.build(new HttpService(WALLET_SERVICE, okHttpClient(), false));
    }

    @Bean
    public Web3j web3j() throws IOException {
        Web3j web3j = Web3j.build(new HttpService(WALLET_SERVICE, okHttpClient(), false));
        return web3j;
    }

    public static OkHttpClient okHttpClient() throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request requestWithUserAgent = originalRequest.newBuilder().build();
                        return chain.proceed(requestWithUserAgent);
                    }
                });
        return builder.build();
    }

    @Bean
    public BtcdClient btcdClient() throws IOException, BitcoindException, CommunicationException {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(cm).build();
        Properties nodeConfig = new Properties();
        String filePath = System.getProperty("user.dir")
                + "/application.yml";
        @Cleanup InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            ClassPathResource resource = new ClassPathResource("application.yml");
            inputStream = resource.getInputStream();
        }
        nodeConfig.load(inputStream);
        BtcdClientImpl btcdClient = new BtcdClientImpl(httpProvider, nodeConfig);
        BtcAction.init(propId, btcdClient);
        return btcdClient;
    }


}
