package website.fanxian.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * my utils
 * @author Kelvin范显
 * @createDate 2018年10月11日
 */
public class KelvinUtils {

    static AtomicInteger atomicInteger = new AtomicInteger();

    /// consumers

    /**
     * "method1 method2" =>
     *      @Test public void _1method1(){}
     *      @Test public void _2method2(){}
     */
    public static Consumer<? super String> consumer_genTestMethod = t -> System.out.print("@Test\npublic void _"+atomicInteger.incrementAndGet()+t+"(){\n}\n");

    /**
     * 处理字符串
     * @param s
     * @param regex
     * @param consumer
     */
    public synchronized static void  handleString(String s, String regex, Consumer<? super String> consumer) {
        atomicInteger.set(0);
        String[] arr = s.split(regex);
        Arrays.stream(arr)
                .filter(StringUtils::isNotBlank)
                .forEach(consumer);
    }

    /// prints

    public static void printStart() {System.out.printf("--begin-------------------------\n\n\n");}
    public static void printEnd() {System.out.printf("\n\n\n--end-------------------------\n");}
    public static void print(String s) {
        print(()->System.out.print(s));
    }
    public static void print(Runnable runnable) {
        printStart();
        runnable.run();
        printEnd();
    }
}
