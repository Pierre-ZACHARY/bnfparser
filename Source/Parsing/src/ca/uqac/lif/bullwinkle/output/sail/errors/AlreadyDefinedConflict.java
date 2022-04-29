package ca.uqac.lif.bullwinkle.output.sail.errors;

import ca.uqac.lif.bullwinkle.output.sail.InputFileToken;
import ca.uqac.lif.bullwinkle.output.sail.InputFileTokenWithType;

public class AlreadyDefinedConflict {

    public InputFileToken current;

    public AlreadyDefinedConflict(InputFileToken current, InputFileTokenWithType definedBy) {
        this.current = current;
        this.definedBy = definedBy;
    }

    public InputFileTokenWithType definedBy;

    @Override
    public String toString() {
        return "AlreadyDefinedConflict{" +
                "current=" + current +
                ", definedBy=" + definedBy +
                '}';
    }
}
