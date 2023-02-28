package xie.fractal.obj.resource;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;
import xie.fractal.obj.base.Obj;

/**
 * 资源，有使用者，产出
 */
@Data
@Entity
@ToString(callSuper = true)
public class Resources extends Obj {
    private Integer currentCapacity;
    private Integer maxCapacity;
    private Integer recoverySpeed;
    @Column(nullable = false)
    private Integer miningSpeed;
}
