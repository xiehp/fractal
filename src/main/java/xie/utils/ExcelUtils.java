package xie.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import xie.fractal.obj.base.Obj;

public class ExcelUtils {
    /**
     * @param filepath
     * @return
     * @throws Exception
     */
    public static List<List<Map<String, String>>> readTable(File filepath) throws Exception {
        /*
         * 首先判断文件是否存在
         * 在判断文件类型，xls还是xlsx
         */
        if (!filepath.exists()) {
            throw new Exception("文件不存在！");
        }
        String filename = filepath.toString();// 转化为string类型
        String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());// 提取文件名后缀
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(filepath);
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new Exception("文件名错误!" + fileType);
            }
            // 新建集合，考虑到要用value值去查询数据库，所以value设置为string类型
            List<List<Map<String, String>>> result = new ArrayList<>();
            int sheetSize = wb.getNumberOfSheets();// 获取表格的个数
            for (int i = 0; i < sheetSize; i++) {// 遍历所有表格
                Sheet sheet = wb.getSheetAt(i);
                List<Map<String, String>> sheetList = new ArrayList<>();
                List<String> titles = new ArrayList<>();// 放置所有的标题
                int rowSize = sheet.getLastRowNum() + 1;// 此处getLastRowNum()方法获取的行数从0开始，故要+1
                for (int j = 0; j < rowSize; j++) {// 遍历所有行
                    Row row = sheet.getRow(j);
                    if (row == null) {// 略过空行
                        continue;
                    }
                    int cellSize = row.getLastCellNum();// 获取列数
                    if (j == 0) {// 第一行是标题行
                        for (int k = 0; k < cellSize; k++) {// 添加到标题集合中
                            Cell cell = row.getCell(k);
                            titles.add(cell.toString());
                        }
                    } else {// 其他行是数据行，为数字
                        Map<String, String> rowMap = new HashMap<>();// 保存一行的数据
                        for (int k = 0; k < titles.size(); k++) {// 遍历保存此行数据
                            Cell cell = row.getCell(k);
                            String key = titles.get(k);
                            String value = null;
                            if (cell != null) {
                                /*
                                 * 这里因为读取excel数据默认值是double类型的，但我的数据都是整数，为了方便先进行一次转换
                                 * 先判断数据类型，然后先转换然后在复制给value
                                 * 数值类型是0，字符串类型是1，公式型是2，空值是3，布尔值4，错误5
                                 */
                                if (CellType.NUMERIC.equals(row.getCell(k).getCellType())) {
                                    value = (int) row.getCell(k).getNumericCellValue() + "";
                                } else {
                                    value = cell.toString();// 转换成string赋值给value
                                }
                            }
                            rowMap.put(key, value);// 把数据存入map集合
                        }
                        sheetList.add(rowMap);// 把存好行的数据存入表格的集合中
                    }
                }
                result.add(sheetList);// 把表格的数据存到excel的集合中
            }
            return result;
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (wb != null) {
                wb.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * @param filepath
     * @param nullValue 碰到该值，则转换为null
     * @return
     * @throws Exception
     */
    public static Map<String, List<Map<String, String>>> readExcelSheetAsMap(File filepath, String nullValue) throws Exception {
        /*
         * 首先判断文件是否存在
         * 在判断文件类型，xls还是xlsx
         */
        if (!filepath.exists()) {
            throw new Exception("文件不存在！");
        }
        String filename = filepath.toString();// 转化为string类型
        String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());// 提取文件名后缀
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(filepath);
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new Exception("文件名错误!" + fileType);
            }
            // 新建集合，考虑到要用value值去查询数据库，所以value设置为string类型
            // List<List<Map<String, String>>> result = new ArrayList<List<Map<String,
            // String>>>();
            Map<String, List<Map<String, String>>> result = new LinkedHashMap<>();
            int sheetSize = wb.getNumberOfSheets();// 获取表格的个数
            for (int i = 0; i < sheetSize; i++) {// 遍历所有表格
                Sheet sheet = wb.getSheetAt(i);
                List<Map<String, String>> sheetList = new ArrayList<Map<String, String>>();
                List<String> titles = new ArrayList<String>();// 放置所有的标题
                int rowSize = sheet.getLastRowNum() + 1;// 此处getLastRowNum()方法获取的行数从0开始，故要+1
                for (int j = 0; j < rowSize; j++) {// 遍历所有行
                    Row row = sheet.getRow(j);
                    if (row == null) {// 略过空行
                        continue;
                    }
                    int cellSize = row.getLastCellNum();// 获取列数
                    if (j == 0) {// 第一行是标题行
                        for (int k = 0; k < cellSize; k++) {// 添加到标题集合中
                            Cell cell = row.getCell(k);
                            titles.add(cell.toString());
                        }
                    } else {// 其他行是数据行，为数字
                        Map<String, String> rowMap = new HashMap<String, String>();// 保存一行的数据
                        for (int k = 0; k < titles.size(); k++) {// 遍历保存此行数据
                            Cell cell = row.getCell(k);
                            String key = titles.get(k);
                            String value = null;
                            if (cell != null) {
                                /*
                                 * 这里因为读取excel数据默认值是double类型的，但我的数据都是整数，为了方便先进行一次转换
                                 * 先判断数据类型，然后先转换然后在复制给value
                                 * 数值类型是0，字符串类型是1，公式型是2，空值是3，布尔值4，错误5
                                 */
                                if (CellType.NUMERIC.equals(row.getCell(k).getCellType())) {
                                    value = (int) row.getCell(k).getNumericCellValue() + "";
                                } else {
                                    value = cell.toString();// 转换成string赋值给value
                                }
                            } else {
                                // 没有cell的情况
                                value = "";
                            }
                            if (nullValue != null && nullValue.equals(value)) {
                                value = null;
                            }
                            rowMap.put(key, value);// 把数据存入map集合
                        }
                        sheetList.add(rowMap);// 把存好行的数据存入表格的集合中
                    }
                }
                String sheetName = sheet.getSheetName();
                result.put(sheetName, sheetList);
            }
            return result;
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (wb != null) {
                wb.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 写入excel
     * 
     * @param <T>
     * @param response
     */
    public static <T extends Obj> void writeExcel(HttpServletResponse response, List<T> dataList, Class<T> cls)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            SecurityException {
        Field[] fields = ReflectUtil.getFields(cls);
        List<Field> tempFieldList = Arrays.asList(fields);
        List<Field> tempFieldList2 = new ArrayList<>();
        tempFieldList.forEach((f -> {
            if (!f.getName().startsWith("$$")) {
                tempFieldList2.add(f);
            }
        }));
        fields = tempFieldList2.toArray(new Field[] {});

        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    // ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    // if (annotation != null && annotation.col() > 0) {
                    // field.setAccessible(true);
                    // return true;
                    // }
                    // return false;
                    field.setAccessible(true);
                    return true;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            // 写入头部
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                } else {
                    columnName = field.getName();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                // cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                // cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                Font font = wb.createFont();
                // font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellValue(value.toString());
                    }
                });
            });
        }
        // 冻结窗格
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        // 浏览器下载excel
        buildExcelDocument("abbot.xlsx", wb, response);
        // 生成excel文件
        // buildExcelFile(".\default.xlsx",wb);
    }

    /**
     * 写入excel
     * 
     * @param <T>
     * @param response
     */
    public static <T extends Obj> void writeExcel(HttpServletResponse response, List<T> dataList)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            SecurityException {

        // 转换
        List<Map<String, Object>> mapList = new ArrayList<>();
        dataList.forEach(d -> {
            Map<String, Object> m = BeanUtil.beanToMap(d);
            // String v = objectMapper.writeValueAsString(d);

            // Map<String, Object> m = new LinkedHashMap<>();
            // BeanUtils.copyProperties(d, m);
            // PropertyUtils.
            mapList.add(m);
        });

        String sheetName = "Sheet1";

        Workbook wb = new XSSFWorkbook();

        if (mapList != null && mapList.size() > 0) {
            sheetName = dataList.get(0).getClass().getSimpleName();

            Sheet sheet = wb.createSheet(sheetName);

            AtomicInteger ai = new AtomicInteger();
            {
                Row row = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                // 写入头部
                mapList.get(0).keySet().forEach(key -> {
                    // ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    // String columnName = "";
                    // if (annotation != null) {
                    // columnName = annotation.value();
                    // } else {
                    // columnName = field.getName();
                    // }
                    String columnName = key;
                    Cell cell = row.createCell(aj.getAndIncrement());

                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    // cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    // cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                    Font font = wb.createFont();
                    // font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                    cellStyle.setFont(font);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(columnName);
                });
            }
            if (CollectionUtils.isNotEmpty(dataList)) {
                mapList.forEach(t -> {
                    Row row1 = sheet.createRow(ai.getAndIncrement());
                    AtomicInteger aj = new AtomicInteger();
                    mapList.get(0).keySet().forEach(key -> {
                        Object value = t.get(key);
                        Cell cell = row1.createCell(aj.getAndIncrement());
                        if (value != null) {
                            if (value instanceof Date) {
                                cell.setCellValue(value.toString());
                            } else {
                                cell.setCellValue(value.toString());
                            }
                            cell.setCellValue(value.toString());
                        }
                    });
                });
            }
        } else {
            wb.createSheet(sheetName);
        }
        // 冻结窗格
        wb.getSheet(sheetName).createFreezePane(0, 1, 0, 1);
        // 浏览器下载excel
        buildExcelDocument("abbot.xlsx", wb, response);
        // 生成excel文件
        // buildExcelFile(".\default.xlsx",wb);
    }

    private static int index = 0;

    /**
     * 写入多个Sheet的excel
     * 
     * @param <T>
     * @param response
     */
    public static void writeExcelMutiSheet(HttpServletResponse response, List<List<?>> allList)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            SecurityException {

        Workbook wb = new XSSFWorkbook();
        index = 0;
        allList.forEach(sheetData -> {

            if (sheetData == null || sheetData.size() == 0) {
                return;
            }

            String sheetName = "Sheet";
            if (!(sheetData.get(0) instanceof Map)) {
                sheetName = sheetData.get(0).getClass().getSimpleName();
            } else {
                sheetName = "Sheet" + index;
            }

            // 转换
            List<Map<String, Object>> rowMapList = new ArrayList<>();
            sheetData.forEach(d -> {
                Map<String, Object> m = BeanUtil.beanToMap(d);
                // String v = objectMapper.writeValueAsString(d);

                // Map<String, Object> m = new LinkedHashMap<>();
                // BeanUtils.copyProperties(d, m);
                // PropertyUtils.
                rowMapList.add(m);
            });

            if (rowMapList != null && rowMapList.size() > 0) {

                Sheet sheet = wb.createSheet(sheetName);

                AtomicInteger ai = new AtomicInteger();
                {
                    Row row = sheet.createRow(ai.getAndIncrement());
                    AtomicInteger aj = new AtomicInteger();
                    // 写入头部
                    rowMapList.get(0).keySet().forEach(key -> {
                        // ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                        // String columnName = "";
                        // if (annotation != null) {
                        // columnName = annotation.value();
                        // } else {
                        // columnName = field.getName();
                        // }
                        String columnName = key;
                        Cell cell = row.createCell(aj.getAndIncrement());

                        CellStyle cellStyle = wb.createCellStyle();
                        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        // cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                        // cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                        Font font = wb.createFont();
                        // font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(columnName);
                    });
                }
                if (CollectionUtils.isNotEmpty(allList)) {
                    rowMapList.forEach(t -> {
                        Row row = sheet.createRow(ai.getAndIncrement());
                        AtomicInteger aj = new AtomicInteger();
                        rowMapList.get(0).keySet().forEach(key -> {
                            Object value = t.get(key);
                            Cell cell = row.createCell(aj.getAndIncrement());
                            if (value != null) {
                                if (value instanceof Date) {
                                    cell.setCellValue(value.toString());
                                } else {
                                    cell.setCellValue(value.toString());
                                }
                                cell.setCellValue(value.toString());
                            } else {
                                cell.setCellValue("null");
                            }
                        });
                    });
                }
            } else {
                wb.createSheet(sheetName);
            }

            // 冻结窗格
            wb.getSheet(sheetName).createFreezePane(0, 1, 0, 1);
        });

        // 浏览器下载excel
        buildExcelDocument("allData.xlsx", wb, response);
        // 生成excel文件
        // buildExcelFile(".\default.xlsx",wb);
    }

    /**
     * 写入多个Sheet的excel
     * 
     * @param <T>
     * @param response
     */
    public static void writeExcelMutiSheetByMap(HttpServletResponse response, Map<String, List<?>> allDataMap,
            Map<String, List<String>> allTitleMap)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            SecurityException {

        Workbook wb = new XSSFWorkbook();
        index = 0;
        allDataMap.keySet().forEach(keyName -> {

            List<?> sheetData = allDataMap.get(keyName);
            List<String> titlelist = null;
            if (allTitleMap != null) {
                titlelist = allTitleMap.get(keyName);
            }

            String sheetName = "Sheet";
            if (sheetData != null && sheetData.size() > 0 && !(sheetData.get(0) instanceof Map)) {
                sheetName = sheetData.get(0).getClass().getSimpleName();
            } else {
                sheetName = keyName;
            }

            // 转换
            List<Map<String, Object>> rowMapList = new ArrayList<>();
            sheetData.forEach(d -> {
                Map<String, Object> m = BeanUtil.beanToMap(d);
                // String v = objectMapper.writeValueAsString(d);

                // Map<String, Object> m = new LinkedHashMap<>();
                // BeanUtils.copyProperties(d, m);
                // PropertyUtils.
                rowMapList.add(m);
            });

            Sheet sheet = wb.createSheet(sheetName);
            AtomicInteger ai = new AtomicInteger();

            // Title处理
            {
                Row row = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                // 写入头部
                Collection<String> titles;
                // if (titlelist != null && titlelist.size() > 0) {
                // titles = titlelist;
                // } else {
                if (rowMapList != null && rowMapList.size() > 0) {
                    titles = rowMapList.get(0).keySet();
                } else {
                    titles = Arrays.asList("no title");
                }
                // }
                titles.forEach(key -> {
                    // ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    // String columnName = "";
                    // if (annotation != null) {
                    // columnName = annotation.value();
                    // } else {
                    // columnName = field.getName();
                    // }
                    String columnName = key;
                    Cell cell = row.createCell(aj.getAndIncrement());

                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    cellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
                    // cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    // cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                    Font font = wb.createFont();
                    // font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                    cellStyle.setFont(font);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(columnName);
                });
            }

            if (sheetData == null || sheetData.size() == 0) {
                return;
            }

            if (rowMapList != null && rowMapList.size() > 0) {
                if (CollectionUtils.isNotEmpty(allDataMap.keySet())) {
                    rowMapList.forEach(t -> {
                        Row row = sheet.createRow(ai.getAndIncrement());
                        AtomicInteger aj = new AtomicInteger();
                        rowMapList.get(0).keySet().forEach(key -> {
                            Object value = t.get(key);
                            Cell cell = row.createCell(aj.getAndIncrement());
                            if (value != null) {
                                if (value instanceof Date) {
                                    cell.setCellValue(value.toString());
                                } else {
                                    cell.setCellValue(value.toString());
                                }
                                cell.setCellValue(value.toString());
                            } else {
                                cell.setCellValue("null");
                            }
                        });
                    });
                }
            }

            // 冻结窗格
            wb.getSheet(sheetName).createFreezePane(0, 1, 0, 1);
        });

        // 浏览器下载excel
        buildExcelDocument("allData.xlsx", wb, response);
        // 生成excel文件
        // buildExcelFile(".\default.xlsx",wb);
    }

    /**
     * 浏览器下载excel
     * 
     * @param fileName
     * @param wb
     * @param response
     */
    private static void buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            // response.flushBuffer();
            wb.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
