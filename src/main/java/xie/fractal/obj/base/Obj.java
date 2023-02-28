package xie.fractal.obj.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.experimental.Accessors;
import xie.fractal.obj.LombokConstructorField;

/**
 * 实体，关系
 * 
 */
@Data
// @RequiredArgsConstructor(onConstructor = @__(@LombokConstructorField))
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Obj {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String objId;

    // @Id
    // @GeneratedValue(generator = "uuid")
    // @GenericGenerator(name = "uuid", strategy = "uuid")
    // private String id;

    @LombokConstructorField
    private String name;
    private Boolean isTemplete;
    private String type;
    private String parentType;

    @CreatedDate
    private Date createTime;
    @CreatedBy
    private String createUser;
    @LastModifiedDate
    private Date updateTime;
    @LastModifiedBy
    private String updateUser;

    public List<String> listKeys() {
        return listFiledNames();
    }

    public List<String> listFiledNames() {
        Field[] fields = this.getClass().getFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            list.add(field.getName());
        }
        return list;
    }
}
