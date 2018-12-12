package website.fanxian.utils;

import com.supwisdom.spreadsheet.mapper.f2w.WorkbookReader;
import com.supwisdom.spreadsheet.mapper.f2w.excel.Excel2WorkbookReader;
import com.supwisdom.spreadsheet.mapper.model.core.Workbook;
import com.supwisdom.spreadsheet.mapper.model.meta.*;
import com.supwisdom.spreadsheet.mapper.o2w.DefaultObject2SheetComposer;
import com.supwisdom.spreadsheet.mapper.o2w.DefaultObject2WorkbookComposer;
import com.supwisdom.spreadsheet.mapper.o2w.Object2SheetComposer;
import com.supwisdom.spreadsheet.mapper.o2w.Object2WorkbookComposer;
import com.supwisdom.spreadsheet.mapper.validation.DefaultSheetValidationJob;
import com.supwisdom.spreadsheet.mapper.validation.DefaultWorkbookValidationJob;
import com.supwisdom.spreadsheet.mapper.w2f.WorkbookWriter;
import com.supwisdom.spreadsheet.mapper.w2f.excel.Workbook2ExcelWriter;
import com.supwisdom.spreadsheet.mapper.w2o.DefaultSheet2ObjectComposer;
import com.supwisdom.spreadsheet.mapper.w2o.DefaultWorkbook2ObjectComposer;
import com.supwisdom.spreadsheet.mapper.w2o.Sheet2ObjectComposer;
import com.supwisdom.spreadsheet.mapper.w2o.Workbook2ObjectComposer;
import org.springframework.core.io.ByteArrayResource;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ExcelUtils
 */
public abstract class ExcelUtils {

    /**
     * 导出包含一个sheet的Excel工作薄为Resource对象
     * @param sheetName sheet名称
     * @param colMap k:属性-v:中文列名对照Map
     * @param dataListVO VO List 数据对象
     * @return
     * @throws Exception
     */
    public static ByteArrayResource exportExcel(String sheetName, LinkedHashMap<String,String> colMap, List dataListVO) throws IOException {
        Workbook workbook = ExcelUtils.buildCustomerWorkBook(sheetName, colMap, dataListVO);
        byte[] dataBytes = ExcelUtils.genernateExcelByteArray(workbook);
        ByteArrayResource bar = new ByteArrayResource(dataBytes);
        return bar;
    }

    /**
     * 写入工作薄到临时文件夹
     * @param fileName 文件名称
     * @param workbook 工作薄对象
     * @throws IOException
     */
    private static void writeFile2Disk(String fileName, Workbook workbook) throws IOException {
        WorkbookWriter workbookWriter = new Workbook2ExcelWriter();
        File tempFile = File.createTempFile(fileName, ".xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            workbookWriter.write(workbook, outputStream);
        }

    }

    /**
     * 工作薄对象转换为byte[]
     * @param workbook 工作薄对象
     * @return
     * @throws IOException
     */
    public static byte[] genernateExcelByteArray(Workbook workbook) throws IOException {
        WorkbookWriter workbookWriter = new Workbook2ExcelWriter();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbookWriter.write(workbook, bos);
            return  bos.toByteArray();
        }
    }

    /**
     * 创建包含一个sheet的Excel工作薄
     * @param sheetName  sheet名称
     * @param colMap k:属性-v:中文列名对照Map
     * @param dataListVO VO List 数据对象
     * @return
     */
    public static Workbook buildCustomerWorkBook(String sheetName, LinkedHashMap<String,String> colMap, List dataListVO) {
        WorkbookMeta workbookMeta = new WorkbookMetaBean();
        SheetMeta sheetMeta = new SheetMetaBean(sheetName, 2);
        int colNum = 1;
        for (Iterator iterator = colMap.keySet().iterator(); iterator.hasNext(); ) {
            String colProp = (String) iterator.next();
            FieldMeta fieldMeta = new FieldMetaBean(colProp, colNum);
            fieldMeta.addHeaderMeta(new HeaderMetaBean(1, colMap.get(colProp)));
            sheetMeta.addFieldMeta(fieldMeta);
            colNum++;
        }
        workbookMeta.addSheetMeta(sheetMeta);
        List<List> dataOfSheets = new ArrayList<>();
        dataOfSheets.add(dataListVO);
        Object2WorkbookComposer object2WorkbookComposer = new DefaultObject2WorkbookComposer();
        Object2SheetComposer<Object> object2SheetComposer = new DefaultObject2SheetComposer<>();
        Workbook workbook = object2WorkbookComposer
                .addObject2SheetComposer(object2SheetComposer)
                .compose(dataOfSheets, workbookMeta);
        return workbook;
    }


    /**
     * 导入Excel文件并转换成指定对象list
     * @param excelInputStream excel文件inputStream
     * @param ObjectPropNameList 要转换的VO类属性名列表
     * @param clazz 要转换的VO类
     * @return
     */
    public static <T> List<List<T>> importWorkBook(InputStream excelInputStream, List<String> ObjectPropNameList, Class<T> clazz) {
        WorkbookReader workbookReader = new Excel2WorkbookReader();
        try (InputStream inputStream = excelInputStream) {
            Workbook workbook = workbookReader.read(inputStream);
            return convertWBook2Object(workbook, ObjectPropNameList, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static List convertWBook2Object(Workbook workbook, List<String> ObjectPropNameList, Class clazz) {
        if(workbook == null || ObjectPropNameList == null)
            return  null;
        DefaultWorkbookValidationJob workbookValidationJob = new DefaultWorkbookValidationJob();
        DefaultSheetValidationJob sheetValidationJob = new DefaultSheetValidationJob();
        WorkbookMeta workbookMeta = new WorkbookMetaBean();
        SheetMeta sheetMeta = new SheetMetaBean("sheet1", 2);
        int colNum = 1;
        for (String propName : ObjectPropNameList) {
            FieldMeta fieldMeta = new FieldMetaBean(propName, colNum);
            sheetMeta.addFieldMeta(fieldMeta);
            //try validate start
            //try validate end
            colNum++;
        }
        workbookMeta.addSheetMeta(sheetMeta);
        Workbook2ObjectComposer workbook2ObjectComposer = new DefaultWorkbook2ObjectComposer();
        Sheet2ObjectComposer sheet2ObjectComposer = new DefaultSheet2ObjectComposer();
        sheet2ObjectComposer.setObjectFactory((row, sheet) -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return  null;
        });

        workbook2ObjectComposer.addSheet2ObjectComposer(sheet2ObjectComposer);
        return workbook2ObjectComposer.compose(workbook, workbookMeta);
    }

}
