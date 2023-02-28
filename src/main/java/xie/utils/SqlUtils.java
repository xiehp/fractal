package xie.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Sql 操作
 */
public class SqlUtils {

    /**
     * 获得where的sql
     */
    public static String getDbWhereSQL(HashMap<String, Object> dbData) {
        Iterator<Entry<String, Object>> iter = dbData.entrySet().iterator();
        StringBuilder strSQL = new StringBuilder(" ");
        boolean addAnd = false;
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            if (addAnd) {
                strSQL.append(" and ");
            }
            strSQL.append("[" + entry.getKey() + "]=");
            if (entry.getValue() == null) {
                strSQL.append("NULL");
            } else {
                strSQL.append("N'" + entry.getValue() + "'");
            }
            addAnd = true;
        }
        return strSQL.toString();
    }

    /**
     * 获得Insert用sql
     */
    public static String getDbInsertSQL(String dbTable, LinkedHashMap<String, Object> dbData) {
        Iterator<Entry<String, Object>> iter = dbData.entrySet().iterator();
        String strSQL = "INSERT INTO " + dbTable;
        String strFields = "";
        String strValues = "";
        boolean add = false;
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();

            if (add) {
                strFields += ", ";
                strValues += ", ";
            }

            strFields += "[" + entry.getKey() + "]";
            if (entry.getValue() != null) {
                strValues += "N'" + entry.getValue() + "'";
            } else {
                strValues += "NULL";
            }

            add = true;
        }
        if (strFields.length() > 0 && strValues.length() > 0) {
            strSQL = strSQL + "(" + strFields + ") VALUES (" + strValues + ")";
            return strSQL;
        } else {
            return null;
        }
    }

    /**
     * 获得update用sql
     */
    public static String getDbUpdateSQL(String dbTable, String strWhere, LinkedHashMap<String, Object> dbData) {
        if (strWhere == null || strWhere.trim().length() == 0) {
            throw new RuntimeException("getDbUpdateSQL: where can not be empty!");
        }

        Iterator<Entry<String, Object>> iter = dbData.entrySet().iterator();
        String strSQL = "UPDATE " + dbTable + " SET ";
        // int iSize = strSQL.length();
        boolean addAnd = false;
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            if (addAnd) {
                strSQL += ", ";
            }
            strSQL += "[" + entry.getKey() + "]=";
            if (entry.getValue() == null) {
                strSQL += "NULL";
            } else {
                strSQL += "N'" + entry.getValue() + "'";
            }
            addAnd = true;

        }

        return strSQL + " WHERE " + strWhere;
    }
}
