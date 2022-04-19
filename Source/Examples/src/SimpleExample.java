/* MIT License
 *
 * Copyright 2014-2021 Sylvain Hallé
 *
 * Laboratoire d'informatique formelle
 * Université du Québec à Chicoutimi, Canada
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseNodeVisitor;
import ca.uqac.lif.bullwinkle.output.GraphvizVisitor;
import ca.uqac.lif.bullwinkle.output.XmlVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class SimpleExample
{
	public static void main(String[] args)
	{
		try
		{
			BnfParser parser = new BnfParser(SimpleExample.class.getResourceAsStream("SAIL3.bnf"));

			Path sailFile = Path.of("sail-examples/cooperate1.sl");
			String sailInput = Files.readString(sailFile);

			String test = "process Main(){\n" +
					"        y = x + 3 * 2\n" +
					"}";

			ParseNode node1 = parser.parse(sailInput);

			if(node1 != null){
				XmlVisitor visitorXML = new XmlVisitor();
				node1.prefixAccept(visitorXML);
				System.out.println(visitorXML.toOutputString());
			}
			else{
				System.err.println("Syntaxe error");
				System.err.println("READ : '"+parser.last_error_key.split(" ")[0]+"'");
				System.err.println("EXPECTED : '"+parser.last_errors.get(parser.last_error_key)+"'");
				System.err.println("AT : "+parser.input_string_error_index+"");
				int line = 0;
				int character = parser.input_string_error_index;
				for(String lines : sailInput.split("\n")){
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


		} catch (BnfParser.InvalidGrammarException | BnfParser.ParseException | ParseNodeVisitor.VisitException | IOException e) {
			e.printStackTrace();
		}
	}
}
