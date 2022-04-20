package ca.uqac.lif.bullwinkle;

import java.util.HashSet;

public class UnexpectedTokenError{
    public String input_string;
    public Integer start_index;
    public HashSet<Token> expected_tokens;

    UnexpectedTokenError(String input_string, Integer start_index){
        this.input_string = input_string;
        this.start_index = start_index;
        this.expected_tokens = new HashSet<>();
    }


}