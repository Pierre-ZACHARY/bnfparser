package ca.uqac.lif.bullwinkle.output.sail;


public class InputFileTokenWithType {

    public final InputFileToken t;
    public SailType type;
    public String existing_type;

    InputFileTokenWithType(InputFileToken t, SailType type){
        this.t = t;
        this.type = type;
    }

    InputFileTokenWithType(InputFileToken t, String existing_type){
        this.t = t;
        this.type = SailType.Existing_Type;
        this.existing_type = existing_type;
    }

    @Override
    public String toString() {
        return "InputFileTokenWithType{" +
                "t=" + t +
                ", type=" + type +
                ", existing_type='" + existing_type + '\'' +
                '}';
    }
}
