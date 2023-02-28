package xie.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    /**
     * Excel标题
     * 
     * @return
     * @author Lynch
     */
    String value() default "";

    /**
     * Excel从左往右排列位置
     * 
     * @return
     * @author Lynch
     */
    int col() default 0;
}