package xie.fractal.obj.people;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;
import xie.fractal.obj.base.Obj;

@Data
@Entity
@ToString(callSuper = true)
public class People extends Obj {
    private Integer age;
    private String address;
    private String phone;
    private String email;
    private String remark;
}
