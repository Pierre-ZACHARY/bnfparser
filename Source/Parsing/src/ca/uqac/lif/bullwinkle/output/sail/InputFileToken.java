package ca.uqac.lif.bullwinkle.output.sail;

public class InputFileToken {
    public String content;
    public int inputIndex;

    InputFileToken(String content, int inputIndex){
        this.content = content;
        this.inputIndex = inputIndex;
    }

    @Override
    public String toString() {
        return "InputFileToken{" +
                "content='" + content + '\'' +
                ", inputIndex=" + inputIndex +
                '}';
    }
}
