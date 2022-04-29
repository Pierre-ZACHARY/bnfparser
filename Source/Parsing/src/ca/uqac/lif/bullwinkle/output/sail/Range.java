package ca.uqac.lif.bullwinkle.output.sail;

public class Range {
    public int start;
    public int end;

    public Range(int i, int i1) {
        start = i;
        end = i1;
    }

    @Override
    public String toString() {
        return "Range{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
