package com.mvc.cryptovault.dashboard.util;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.mvc.cryptovault.common.bean.AppInfo;
import org.apkinfo.api.util.AXmlResourceParser;
import org.apkinfo.api.util.TypedValue;
import org.apkinfo.api.util.XmlPullParser;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author ZSL
 */
public final class ReadUtil {

    /**
     * 读取apk
     *
     * @return
     */
    public static AppInfo readAPK(InputStream inputStream) {
        AppInfo apkInfo = new AppInfo();
        apkInfo.setAppType("APK");
        try {
            AXmlResourceParser parser = new AXmlResourceParser();
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zipStream.getNextEntry();
            while (true) {
                if (null == zipEntry || zipEntry.getName().equalsIgnoreCase("androidmanifest.xml")) {
                    break;
                }
            }
            parser.open(zipStream);
            while (true) {
                int type = parser.next();
                if (type == XmlPullParser.END_DOCUMENT) {
                    break;
                }
                String name = parser.getName();
                if (null != name && name.toLowerCase().equals("manifest")) {
                    for (int i = 0; i != parser.getAttributeCount(); i++) {
                        if ("versionName".equals(parser.getAttributeName(i))) {
                            String versionName = getAttributeValue(parser, i);
                            if (null == versionName) {
                                versionName = "";
                            }
                            apkInfo.setAppVersion(versionName);
                        } else if ("package".equals(parser.getAttributeName(i))) {
                            String packageName = getAttributeValue(parser, i);
                            if (null == packageName) {
                                packageName = "";
                            }
                            apkInfo.setAppPackage(packageName);
                        } else if ("versionCode".equals(parser.getAttributeName(i))) {
                            String versionCode = getAttributeValue(parser, i);
                            if (null == versionCode) {
                                versionCode = "";
                            }
                            apkInfo.setAppVersionCode(Integer.valueOf(versionCode));
                        }
                    }
                    break;
                }
            }
            zipStream.close();
        } catch (Exception e) {
            return null;
        }
        return apkInfo;
    }

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == TypedValue.TYPE_STRING) {
            return parser.getAttributeValue(index);
        }
        if (type == TypedValue.TYPE_ATTRIBUTE) {
            return String.format("?%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_REFERENCE) {
            return String.format("@%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_FLOAT) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (type == TypedValue.TYPE_INT_HEX) {
            return String.format("0x%08X", data);
        }
        if (type == TypedValue.TYPE_INT_BOOLEAN) {
            return data != 0 ? "true" : "false";
        }
        if (type == TypedValue.TYPE_DIMENSION) {
            return Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type == TypedValue.TYPE_FRACTION) {
            return Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return String.format("#%08X", data);
        }
        if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", data, type);
    }

    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    // ///////////////////////////////// ILLEGAL STUFF, DONT LOOK :)
    public static float complexToFloat(int complex) {
        return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
    }

    private static final float RADIX_MULTS[] =
            {
                    0.00390625F, 3.051758E-005F,
                    1.192093E-007F, 4.656613E-010F
            };
    private static final String DIMENSION_UNITS[] = {"px", "dip", "sp", "pt", "in", "mm", "", ""};
    private static final String FRACTION_UNITS[] = {"%", "%p", "", "", "", "", "", ""};

    /**
     * 读取ipa
     */
    public static AppInfo readIPA(InputStream inputStream) {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppType("IPA");
        try {
            ZipInputStream zipIns = new ZipInputStream(inputStream);
            ZipEntry ze;
            InputStream infoIs = null;
            NSDictionary rootDict = null;
            String icon = null;
            while ((ze = zipIns.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String name = ze.getName();
                    if (null != name &&
                            name.toLowerCase().contains(".app/info.plist")) {
                        ByteArrayOutputStream _copy = new
                                ByteArrayOutputStream();
                        int chunk = 0;
                        byte[] data = new byte[1024];
                        while (-1 != (chunk = zipIns.read(data))) {
                            _copy.write(data, 0, chunk);
                        }
                        infoIs = new ByteArrayInputStream(_copy.toByteArray());
                        rootDict = (NSDictionary) PropertyListParser.parse(infoIs);

                        //我们可以根据info.plist结构获取任意我们需要的东西
                        //比如下面我获取图标名称，图标的目录结构请下面图片
                        //获取图标名称
                        NSDictionary iconDict = (NSDictionary) rootDict.get("CFBundleIcons");

                        while (null != iconDict) {
                            if (iconDict.containsKey("CFBundlePrimaryIcon")) {
                                NSDictionary CFBundlePrimaryIcon = (NSDictionary) iconDict.get("CFBundlePrimaryIcon");
                                if (CFBundlePrimaryIcon.containsKey("CFBundleIconFiles")) {
                                    NSArray CFBundleIconFiles = (NSArray) CFBundlePrimaryIcon.get("CFBundleIconFiles");
                                    icon = CFBundleIconFiles.getArray()[0].toString();
                                    if (icon.contains(".png")) {
                                        icon = icon.replace(".png", "");
                                    }
                                    System.out.println("获取icon名称:" + icon);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
            // 应用包名
            NSString parameters = (NSString) rootDict.get("CFBundleIdentifier");
            appInfo.setAppPackage(parameters.toString());
            // 应用版本名
            parameters = (NSString) rootDict.objectForKey("CFBundleShortVersionString");
            appInfo.setAppVersion(parameters.toString());
            //应用版本号
            parameters = (NSString) rootDict.get("CFBundleVersion");
            appInfo.setAppVersionCode(Integer.valueOf(parameters.toString()));

            /////////////////////////////////////////////////
            infoIs.close();
            zipIns.close();

        } catch (Exception e) {
            return null;
        }
        return appInfo;
    }

}