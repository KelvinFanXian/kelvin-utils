package website.fanxian.utils;

import joinery.DataFrame;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Kelvin范显
 * @date 2020/7/31 下午5:56
 */
public final class DataFrameUtils {
    public static void flushExcel(String filename, DataFrame dataFrame) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("octets/stream");
        response.setCharacterEncoding("utf-8");
        try {
            response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
        } catch (UnsupportedEncodingException e) {
            response.setHeader("Content-disposition", "attachment;filename="+filename+".xls");
        }

        try(ServletOutputStream out = response.getOutputStream()) {
            dataFrame.writeXls(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * getDataFrame from List<Map></Map>
     * @param maps
     * @return
     */
    public static DataFrame getDataFrame(List<Map> maps) {
        Set colums = maps.stream().max(Comparator.comparing(m -> m.size())).get().keySet();
        DataFrame df = new DataFrame(colums);
        for (Map map1 : maps) {
            df.append(Arrays.asList(map1.values().toArray()));
        }
        df = df.fillna(0);
        return df;
    }
}
