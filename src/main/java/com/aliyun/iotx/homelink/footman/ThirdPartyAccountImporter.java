package com.aliyun.iotx.homelink.footman;

import com.aliyun.iotx.api.sdk.platform.thirdparty.ThirdPartyApi;
import com.aliyun.iotx.api.util.context.ApiContext;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;


/**
 * 三方账号导入
 */
public class ThirdPartyAccountImporter {

    private static final String KEY_HOST = "footman.api.host";

    private static final String KEY_SYSTEM = "footman.api.system";

    private static final String KEY_APP_KEY = "footman.api.appKey";

    private static final String KEY_APP_SECRET = "footman.api.appSecret";

    private static final String KEY_OPEN_ID = "footman.api.hid";

    private static final String KEY_LOGIN_NAME = "footman.api.loginName";

    private static final String KEY_PHONE = "footman.api.phone";

    private static final String KEY_EMAIL = "footman.api.email";

    public static void main(String[] args) {
        try {
            process(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private static void process(String[] args) throws Exception {
        System.out.println("三方账号导入工具");
        System.out.println("使用config参数指定配置文件");

        Properties ps = loadParams(args);

        String host = ps.getProperty(KEY_HOST);
        String appKey = ps.getProperty(KEY_APP_KEY);
        String appSecret = ps.getProperty(KEY_APP_SECRET);

        // 初始化环境
        ApiContext.setEnv("default",
            host,
            appKey,
            appSecret,
            3000L,
            true);
        if (ps.containsKey(KEY_SYSTEM)) {
            ApiContext.setPre(!parseBoolean(ps.getProperty(KEY_SYSTEM)));
        }

        // 参数
        String openId = ps.getProperty(KEY_OPEN_ID);
        String loginName = ps.getProperty(KEY_LOGIN_NAME);
        String phone = ps.getProperty(KEY_PHONE);
        String email = ps.getProperty(KEY_EMAIL);

        // 调用接口
        String identityId = ThirdPartyApi.importAccountForThirdParty(
            openId, loginName, phone, email, "", "", openId, null, false, 23)
            .executeAndGet();

        System.out.println("---------------------------------------");
        System.out.println("申请的账号");
        System.out.println("  identityId = " + identityId);
        System.out.println("         hid = " + openId);
        System.out.println("     hidType = OPEN");
        System.out.println("   loginName = " + loginName);
        System.out.println("       phone = " + phone);
        System.out.println("       email = " + email);
        System.out.println("---------------------------------------");
    }

    @NotNull
    private static Properties loadParams(String[] args) throws Exception {
        String path = getConfig(args);
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("没找到配置文件: " + path);
            System.exit(1);
        }

        Properties ps = new Properties();
        ps.load(new FileInputStream(file));

        System.out.println("加载配置文件：" + path);

        return ps;
    }

    private static String getConfig(String[] args) throws Exception {
        Optional<String> config = Arrays.stream(args)
            .filter(s -> !Strings.isNullOrEmpty(s))
            .filter(s -> s.startsWith("config"))
            .map(s -> {
                int idx = s.indexOf("=");
                if (-1 != idx) {
                    return s.substring(idx + 1);
                }
                throw new RuntimeException("config参数格式错误. 示例 config=/xxx/xxx.properties");
            }).findFirst();
        if (!config.isPresent()) {
            throw new Exception("没找到config参数");
        }

        return config.get();
    }
}
