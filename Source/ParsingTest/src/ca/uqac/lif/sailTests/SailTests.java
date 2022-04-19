package ca.uqac.lif.sailTests;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseNodeVisitor;
import ca.uqac.lif.bullwinkle.output.XmlVisitor;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SailTests {

    static BnfParser parser;
    String input;

    @BeforeAll
    static void setUp() throws Exception
    {
        System.out.println("BeforeAll setUp() method called");

        // Nothing to do
        parser = new BnfParser(SailTests.class.getResourceAsStream("sail.bnf"));
    }

    public static String removeComments(String input){
        System.out.println("Test removeComments() method called");

        String MyCommentsRegex = "(?://.*)|(/\\*(?:.|[\\n\\r])*?\\*/)";
        String res = input.replaceAll(MyCommentsRegex, " ");
        System.out.println(res);
        return res;
    }

    @Test
    public void testComments() throws IOException {
        System.out.println("Test testComments() method called");

        Path sailFile = Path.of("sail-examples/commentsTests.sl");
        input = Files.readString(sailFile);
    }

    @AfterEach
    public void resolve() throws ParseNodeVisitor.VisitException, BnfParser.ParseException {
        System.out.println("After Each resolve() method called");

        ParseNode result = parser.parse(removeComments(input));
        if(result != null){
            XmlVisitor visitorXML = new XmlVisitor();
            result.prefixAccept(visitorXML);
            System.out.println(visitorXML.toOutputString());
        }
        else{
            System.err.println("Syntaxe error");
            System.err.println("READ : '"+parser.last_error_key.split(" ")[0]+"'");
            System.err.println("EXPECTED : '"+parser.last_errors.get(parser.last_error_key)+"'");
            System.err.println("AT : "+parser.input_string_error_index+"");
            int line = 0;
            int character = parser.input_string_error_index;
            for(String lines : input.split("\n")){
                if(character-(lines.length()+1)<0){
                    System.err.println("LINE : "+line+"");
                    System.err.println("CHR : "+character+"");
                    break;
                }
                else{
                    character -= lines.length() +1;
                    line +=1;
                }
            }
        }
        Assertions.assertNotNull(result);
    }

    @Test
    public void arithmetic() throws IOException {
        System.out.println("Test arithmetic() method called");

        Path sailFile = Path.of("sail-examples/arithmetic.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void bettercallsaul() throws IOException {
        System.out.println("Test bettercallsaul() method called");

        Path sailFile = Path.of("sail-examples/bettercallsaul.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void cooperate1() throws IOException {
        System.out.println("Test cooperate1() method called");

        Path sailFile = Path.of("sail-examples/cooperate1.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void cooperate2() throws IOException {
        System.out.println("Test cooperate2() method called");

        Path sailFile = Path.of("sail-examples/cooperate2.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void cooperate3() throws IOException {
        System.out.println("Test cooperate3() method called");

        Path sailFile = Path.of("sail-examples/cooperate3.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void cooperate4() throws IOException {
        System.out.println("Test cooperate4() method called");

        Path sailFile = Path.of("sail-examples/cooperate4.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void counter() throws IOException {
        System.out.println("Test counter() method called");

        Path sailFile = Path.of("sail-examples/counter.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void counter2() throws IOException {
        System.out.println("Test counter2() method called");

        Path sailFile = Path.of("sail-examples/counter2.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void decl1() throws IOException {
        System.out.println("Test decl1() method called");

        Path sailFile = Path.of("sail-examples/decl1.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void decl2() throws IOException {
        System.out.println("Test decl2() method called");

        Path sailFile = Path.of("sail-examples/decl2.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void drop1() throws IOException {
        System.out.println("Test drop1() method called");

        Path sailFile = Path.of("sail-examples/drop1.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void drop2() throws IOException {
        System.out.println("Test drop2() method called");

        Path sailFile = Path.of("sail-examples/drop2.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void drop3() throws IOException {
        System.out.println("Test drop3() method called");

        Path sailFile = Path.of("sail-examples/drop3.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void drop4() throws IOException {
        System.out.println("Test drop4() method called");

        Path sailFile = Path.of("sail-examples/drop4.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void emptypar() throws IOException {
        System.out.println("Test emptypar() method called");

        Path sailFile = Path.of("sail-examples/emptypar.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void factorial() throws IOException {
        System.out.println("Test factorial() method called");

        Path sailFile = Path.of("sail-examples/factorial.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void helloworld() throws IOException {
        System.out.println("Test helloworld() method called");

        Path sailFile = Path.of("sail-examples/helloworld.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void min() throws IOException {
        System.out.println("Test min() method called");

        Path sailFile = Path.of("sail-examples/min.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void minArray() throws IOException {
        System.out.println("Test minArray() method called");

        Path sailFile = Path.of("sail-examples/minArray.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void mutable1() throws IOException {
        System.out.println("Test mutable1() method called");

        Path sailFile = Path.of("sail-examples/mutable1.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void parallel1() throws IOException {
        System.out.println("Test parallel1() method called");

        Path sailFile = Path.of("sail-examples/parallel1.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void parallel2() throws IOException {
        System.out.println("Test parallel2() method called");

        Path sailFile = Path.of("sail-examples/parallel2.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void point() throws IOException {
        System.out.println("Test point() method called");

        Path sailFile = Path.of("sail-examples/point.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void sum() throws IOException {
        System.out.println("Test sum() method called");

        Path sailFile = Path.of("sail-examples/sum.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void sumArray() throws IOException {
        System.out.println("Test sumArray() method called");

        Path sailFile = Path.of("sail-examples/sumArray.sl");
        input = Files.readString(sailFile);
    }

    @Test
    public void while1() throws IOException {
        System.out.println("Test while1() method called");

        Path sailFile = Path.of("sail-examples/while1.sl");
        input = Files.readString(sailFile);
    }


    @Test
    public void testsFunctionCall() throws IOException {
        input = "process Main(){\n" +
                "    x=test(1,2,3)\n" +
                "}";
    }

    @Test
    public void testsBooleanOperation() throws IOException {
        input = "process Main(){\n" +
                "    if(!(1==1 or 2>1 and 8!=5)){}\n" +
                "}";
    }

    @Test
    public void testArray() throws IOException {
        System.out.println("Test testArray() method called");

        Path sailFile = Path.of("sail-examples/test-array.sl");
        input = Files.readString(sailFile);
    }

}
