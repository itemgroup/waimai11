package com.amx.luckin.utils;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class LogUtil {
    private static final String TAG_LOG = "LuckinCoffee:";

    private static final int DOMAIN_ID = 0xD002B00;

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.DEBUG, DOMAIN_ID, LogUtil.TAG_LOG);

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    private LogUtil() {}

    public static void info(String tag, String msg) {
        HiLog.info(LABEL_LOG, LOG_FORMAT, tag, msg);
    }

    public static void error(String tag, String msg) {
        HiLog.error(LABEL_LOG, LOG_FORMAT, tag, msg);
    }
}