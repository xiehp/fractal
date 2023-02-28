package xie.fractal.db.jooq;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;

public class JooqUtils {
    public static List<String> getTitle(Result<Record> fetchResult) {
        List<String> titleList = new ArrayList<>();
        for (Field<?> f : fetchResult.fields()) {
            System.out.println(f.getName());
            System.out.println(f.getQualifiedName());
            System.out.println(f.getUnqualifiedName());

            titleList.add(f.getName());
        }
        return titleList;
    }
}
