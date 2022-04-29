package ca.uqac.lif.bullwinkle.output.sail;

import java.util.ArrayList;
import java.util.List;

public class StructDef {

    InputFileTokenWithType name;
    List<InputFileTokenWithType> attributes;

    public StructDef(InputFileTokenWithType struct){
        name = struct;
        attributes = new ArrayList<>();
    }

    public void addAttr(InputFileTokenWithType attr){
        attributes.add(attr);
    }

    @Override
    public String toString() {
        return "StructDef{" +
                "name=" + name +
                ", attributes=" + attributes +
                '}';
    }
}
