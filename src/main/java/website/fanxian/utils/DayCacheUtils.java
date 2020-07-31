package website.fanxian.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 缓存当天数据
 * @author Kelvin范显
 * @date 2020/7/21 下午4:07
 */
public final class DayCacheUtils {
    private static final Map<String, Object> day_cache = new HashMap<>();
    private static String getDay() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd");
    }

    private static void clearKey(String key) {
        Iterator<Map.Entry<String, Object>> it = day_cache.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getKey().endsWith("_"+key)) {
                it.remove();
            }
        }
    }


    public static <T> T getCache(String key, T t) {
        String today_key = String.format("%s_%s", getDay(), key);
        if (day_cache.get(today_key) == null) {
            clearKey(key);
            day_cache.put(today_key, t);
        }
        return (T) day_cache.get(today_key);
    }
}
