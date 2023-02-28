package xie.fractal.obj.people;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class Farmer extends People {
    
    public Farmer(String name) {
        setName(name);
    }
}
