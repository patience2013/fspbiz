package com.fangshang.fspbiz;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.Utils;
import com.fangshang.fspbiz.fragment.housing.sqldb.DaoMaster;
import com.fangshang.fspbiz.fragment.housing.sqldb.DaoSession;
import com.fangshang.fspbiz.service.AccountHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by lenovo on 2017
 */

public class App extends AppContext {
    private static final String TAG = "App";
//    public static final String H5_HOST ="http://121.40.168.108/fsp-h5/html/";
    public static final String H5_HOST ="http://192.168.1.153:8000/html/";
//    public static final String H5_HOST ="http://121.40.168.108/h5/#/";
        public static final String HTTP_API_HOST = "https://ssl.guanlouyi.com";
//    public static final String HTTP_API_HOST = "http://121.40.168.108:3939";
//    public static final String HTTP_API_HOST = "http://192.168.1.115:3939";

    public static final String IMG_HOST = "http://yk.yiyipai.cn";

    private static App instance;

    public static App getInstance() {
        return instance;
    }
    public static OkHttpClient okHttpClient;

    private DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 初始化账户登录基础信息
        AccountHelper.init(this);
//        TuSdk.enableDebugLog(true);
        // 初始化SDK (请将目标项目所对应的密钥放在这里)
//        TuSdk.init(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1");

        // 初始化
        try {
            initNetwork();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //配置图片选择器
//        initImagePicker();

        Utils.init(this);
        //初始化账户个人信息
        initInfo();
        initLogger("MyLog");//初始化日志信息，如上架则需注释掉
        setupDatabase("fangsuanpan");
    }
    /**
     * 配置数据库
     */
    private void setupDatabase(String name) {
        //创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, name, null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }
    public DaoSession getDaoSession(){
        return daoSession;
    }

    private void initLogger(String tag) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(tag) // 全局tag
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }
    private void initInfo() {
//        if (AccountHelper.isLogin()) {
//            HttpRequest.GetUserInfo cmd = new  HttpRequest.GetUserInfo(AccountHelper.getUserId()+"");
//            HttpNetwork.getInstance().request(cmd, new HttpNetwork.NetResponseCallback() {
//                @Override
//                public void onResponse(HttpRequestPacket request, HttpResponsePacket response) {
//                    Type listType = new TypeToken<UserBaseInfo>() {
//                    }.getType();
//                    UserBaseInfo userBaseInfo = response.getData(listType);
//                    AccountHelper.setUserBaseInfo(userBaseInfo);
//                }
//            }, new HttpNetwork.NetErrorCallback() {
//                @Override
//                public void onError(HttpRequestPacket request, String errorMsg) {
//
//                }
//            });
//        }
    }
    public OkHttpClient getOkHttpClient(){
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
                .build();
        return okHttpClient;
    }
    private void initNetwork() throws IOException {
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{getAssets().open("www.guanlouyi.com.cer")}, null, null);
//        Log.i(TAG, "initNetwork: " + sslParams.sSLSocketFactory.toString() + "ffawe" + sslParams.trustManager);

//        HttpParams params = new HttpParams();
//        params.put("token","b962758b084845f0a5321e2c09ca3b3813597078080199250765825557158639");
        OkGo.getInstance().init(this)                       //必须调用初始化
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                  //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(  一次原始请求，三次重连请求)，不需要可以设置为0
//
//        OkHttpUtils.initClient(okHttpClient);
//        new HttpNetwork(HTTP_API_HOST);

    }

    // 获取版本号
    public int getVersionCode() {
        int versionCode = 1;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(),
                    0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }

}
