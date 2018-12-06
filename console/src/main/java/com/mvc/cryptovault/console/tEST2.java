package com.mvc.cryptovault.console;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/11/30 18:11
 */
public class tEST2 {

    public static void main(String[] args) throws IOException {
        Web3j web3j = Web3j.build(new HttpService("http://47.75.14.213:8545", okHttpClient(), false));


        Function function = new Function(
                "approve",
                Arrays.asList(new Address("0x75342dc35e9a8d1e3ec92de5f691c863f06f0d51"), new Uint256(BigInteger.valueOf(10000000))),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.ZERO,
                new BigInteger("5000000000"),
                new BigInteger("45867"),
                "0x44866562beacdd42a863b5f2058649fdb4396cc6",
                encodedFunction);
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger("99754501592548518430501953653774084225683557642569129348457047233736258644581"));
        Credentials ALICE = Credentials.create(ecKeyPair);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, ALICE);
        String hexValue = Numeric.toHexString(signedMessage);
        System.out.println(hexValue);
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

}
