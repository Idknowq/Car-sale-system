package com.carsales.backend.util;

public final class DbVendorUtil {
    private DbVendorUtil() {}
    public static boolean isOpenGauss(String vendor) { return "opengauss".equalsIgnoreCase(vendor); }
    public static boolean isPostgres(String vendor) { return "postgres".equalsIgnoreCase(vendor); }
}
