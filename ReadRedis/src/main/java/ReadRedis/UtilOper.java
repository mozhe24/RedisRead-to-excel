package ReadRedis;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class UtilOper {

    private static final Logger log = LoggerFactory.getLogger(UtilOper.class);

    /**
     * 从配置文件adx.conf读取到Properties
     * @param fileName
     * @return
     */

    @Deprecated
    public static Properties getProperties(String fileName) {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                log.error("file ({}) not found", fileName);
                return null;
            }
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            Properties props = new Properties();
            props.load(reader);
            return props;
        }
        catch (Exception e) {
            log.error("something wrong with file ({}):({})", fileName, e.getMessage());
            return null;
        }
    }

    /* ===================================================================== */
    /* 获取配置文件的map的key-value里，特定key对应的不同数据类型的value（Int Long Double String Boolean），如果key不存在、没有值、值格式不对都会返回null */
    /* ===================================================================== */

    public static Integer getIntValue(String filename, String key) {
        Map<String, String> properties = getPropertiesToMap(filename);
        if (properties == null) return null;

        String value = properties.get(key);
        if (value == null) return null;

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static Long getLongValue(String filename, String key) {
        Map<String, String> properties = getPropertiesToMap(filename);
        if (properties == null) return null;

        String value = properties.get(key);
        if (value == null) return null;

        try {
            return Long.parseLong(value);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static Double getDoubleValue(String filename, String key) {
        Map<String, String> properties = getPropertiesToMap(filename);
        if (properties == null) return null;

        String value = properties.get(key);
        if (value == null) return null;

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getStringValue(String filename, String key) {
        Map<String, String> properties = getPropertiesToMap(filename);
        if (properties == null) return null;

        String value = properties.get(key);
        return value;
    }

    public static Boolean getBooleanValue(String filename, String key) {
        Map<String, String> properties = getPropertiesToMap(filename);
        if (properties == null) return null;

        String value = properties.get(key);
        if (value == null) return null;

        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }



    /* ===================================================================== */
    /*获取配置文件的map的key-value里，特定key对应的不同数据类型的value（Int Long Double String Boolean）， 如果key不存在、没有值、值格式不对都会返回defaultVal */
    /* ===================================================================== */

    public static int getIntValue(String filename, String key, int defaultVal) {
        Integer value = getIntValue(filename, key);
        if (value == null) value = defaultVal;
        return value;
    }

    public static long getLongValue(String filename, String key, long defaultVal) {
        Long value = getLongValue(filename, key);
        if (value == null) value = defaultVal;
        return value;
    }

    public static double getDoubleValue(String filename, String key, double defaultVal) {
        Double value = getDoubleValue(filename, key);
        if (value == null) value = defaultVal;
        return value;
    }

    public static String getStringValue(String filename, String key, String defaultVal) {
        String value = getStringValue(filename, key);
        if (value == null) value = defaultVal;
        return value;
    }

    public static boolean getBooleanValue(String filename, String key, boolean defaultVal) {
        Boolean value = getBooleanValue(filename, key);
        if (value == null) value = defaultVal;
        return value;
    }
    /* ======================================================================= */
    /* 将配置文件adx.conf 读取形成<key,value>对 */
    /* ======================================================================= */
    public static Map<String, String> getPropertiesToMap(String fileName) {
        File file = null;
        if (fileName.contains(".jar!")) {
            file = new File("");
            fileName = file.getAbsolutePath() + "/" + fileName.substring(fileName.lastIndexOf("/"));
            file = new File(fileName);
        } else {
            file = new File(fileName);
        }
        if (!file.exists() || file.isDirectory()) {
            log.error("the file name ({}) not exists!",fileName);
            return null;
        }

        Properties properties = new Properties();
        InputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            properties.load(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputFile != null) {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //key-value 匹配成对，放入propertyMap
        Map<String, String> propertyMap = new HashMap<String, String>();
        Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            propertyMap.put(key, value);
        }

        return propertyMap;
    }

    //用“，”分割后，移除命令里的空格，放至results的 HashSet中；
    public static Set<String> splitByCommaAndRemoveBlankElements(String s) {
        if (StringUtils.isBlank(s)) return null;

        Set<String> results = new HashSet<String>();
        String[] elements = s.split(",");
        for (int i = 0; i < elements.length; i++) {
            String element = elements[i].trim();
            if (StringUtils.isNotBlank(element)) results.add(element);
        }

        return results;
    }

    @Deprecated
    public static Set<Integer> splitByCommaAndRemoveBlankElementsAndParseInt(String s) {
        if (StringUtils.isBlank(s)) return null;

        Set<Integer> results = new HashSet<Integer>();
        String[] elements = s.split(",");
        for (int i = 0; i < elements.length; i++) {
            String element = elements[i].trim();
            if (StringUtils.isNotBlank(element)) results.add(Integer.valueOf(element));
        }

        return results;
    }

    /**
     *
     * @param millSecond 线程毫秒睡眠
     */
    public static void sleep(int millSecond) {
        try {
            Thread.currentThread().sleep(millSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param nanoSecond 用户自己定义的纳秒睡眠，因为一个普通的操作系统没有一个细粒度的足够的分辨率为纳秒一次睡眠；
     */
    public static void nanoSleep(long nanoSecond) {
        long setMillSecond = nanoSecond / 1000000;
        long setNanoSecond = nanoSecond % 1000000;
        try {
            Thread.currentThread().sleep((int) setMillSecond, (int) setNanoSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
