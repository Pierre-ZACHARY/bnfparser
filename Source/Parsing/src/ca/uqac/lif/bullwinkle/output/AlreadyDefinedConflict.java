package ca.uqac.lif.bullwinkle.output;

public class AlreadyDefinedConflict {

    public InputFileToken current;

    public AlreadyDefinedConflict(InputFileToken current, InputFileToken definedBy) {
        this.current = current;
        this.definedBy = definedBy;
    }

    public InputFileToken definedBy;

    @Override
    public String toString() {
        return "AlreadyDefinedConflict{" +
                "current=" + current +
                ", definedBy=" + definedBy +
                '}';
    }
}
