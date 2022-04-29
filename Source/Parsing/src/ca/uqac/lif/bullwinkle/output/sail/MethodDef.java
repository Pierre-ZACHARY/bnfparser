package ca.uqac.lif.bullwinkle.output.sail;

import java.util.ArrayList;
import java.util.List;

public class MethodDef {

    InputFileTokenWithType method_def;
    List<InputFileTokenWithType> parameters;
    public MethodDef(){
        parameters = new ArrayList<>();
    }

    public void addParam(InputFileTokenWithType param){
        parameters.add(param);
    }

    @Override
    public String toString() {
        return "MethodDef{" +
                "name=" + method_def +
                ", parameters=" + parameters +
                '}';
    }
}
