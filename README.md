FlyParse: an on-the-fly parser
==============================

FlyParse is a parser for LL(k) languages that operates through recursive descent
with backtracking.

[Parser generators](http://en.wikipedia.org/wiki/Parser_generator) such as
ANTLR, Yacc or Bison take a grammar as input and produce code for a parser
specific to that grammar, which must then be compiled to be used. On the
contrary, FlyParse reads the definition of the grammar (expressed in
[Backus-Naur Form](http://en.wikipedia.org/wiki/Backus-Naur_form) (BNF)) at
runtime and can readily parse strings on the spot.

An example
----------

Consider for example the following simple grammar, taken from the file
`Examples/Simple-Math.bnf` in the FlyParse archive:

    <exp> := <add> | <sub> | <mul> | <div> | - <exp> | <num>;
    <add> := <num> + <num> | ( <exp> + <exp> );
    <sub> := <num> - <num> | ( <exp> - <exp> );
    <mul> := <num> × <num> | ( <exp> × <exp> );
    <div> := <num> ÷ <num> | ( <exp> ÷ <exp> );
    <num> := ^[0-9]+;

Here is a simple Java program that reads characters strings and tries to parse
them against this grammar (a complete working program can be found in the file
`SimpleExample.java`:
    
    try
    {
      BnfParser parser = new BnfParser("Examples/Simple-Math.bnf");
      ParseNode node1 = parser.parse("3+4");
      ParseNode node2 = parser.parse("(10 + (3 - 4))");
    }
    catch (IOException | InvalidGrammarExpression | ParseException)
    {
      System.err.println("Some error occurred");
    }

The first instruction loads the grammar definition and instantiates an object
`parser` for that grammar. Calls to method `parse()` give this parser a
character string, and return an object of class `ParseNode` which points to the
head of the corresponding parse tree (or null if the input string does not
follow the grammar). These instructions are enclosed in a try/catch block to
catch potential exceptions thrown during this process. The whole process is done
dynamically at runtime, without requiring any compiling.

Here is the parse tree returned for the second expression in the previous
example:

![Parse tree](Simple-Math.svg?raw=true)

Defining a grammar
------------------

The grammar must be [LL(k)](http://en.wikipedia.org/wiki/LL_parser). Roughly,
this means that it must not contain a production rules of the form
`<S> := <S> something`. Trying to parse such a rule by recursive descent causes
an infinite recursion (which will throw a ParseException when the maximum
recursion depth is reached).

Defining a grammar can be done in two ways.

### Parsing a string

The first way is by parsing a character string (taken from a file or created
directly) that contains the grammar declaration. This format uses a fairly
intuitive syntax, as the example above has shown.
   
- Non-terminal symbols are enclosed in `<` and `>` and their names must not
  contain spaces.
- Rules are defined with `:=` and cases are separated by the pipe character.
- A rule can span multiple lines (any whitespace character after the first one
  is ignored, as in e.g. HTML) and must end by a semicolon.
- Terminal symbols are defined by typing them directly in a rule, or through
  regular expressions and begin with the `^` (hat) character. The example above
  shows both cases: the `+` symbol is typed directly into the rules, while the
  terminal symbol `<num>` is defined with a regex. If a space needs to be used
  in the regular expression, it must be declared by using the regex sequence
  `\s`, and *not* by putting a space. Caveat emptor: a few corner cases are not
  covered at the moment, such as a regex that would contain a semicolon.
- The left-hand side symbol of the first rule found is assumed to be the start
  symbol. This can be overridden by calling method `setStartSymbol()` on an
  instance of the parser.
- Whitespace acts as a token separator, so there is no need to declare terminal
  tokens separately. This means that the rule `<num> + <num>` matches any string
  with a number, the symbol +, and another number, separated by any number of
  spaces, including none. This also means that writing `1+2` defines a *single*
  token that matches only the string "1+2". When declaring rules, tokens *must*
  be separated by a space. Writing `(<exp>)` is illegal and will throw an
  exception; one must write `( <exp> )` (note the spaces). However, since
  whitespace is ignored when parsing, this rule would still match the string
  "(1+1)".

### Building the rules manually

A second way of defining a grammar consists of assembling rules by creating
instances of objects programmatically. Roughly:

- A `BnfRule` contains a left-hand side that must be a `NonTerminalToken`, and
  a right-hand side containing multiple cases that are added through method
  `addAlternative()`.
- Each case is itself a `TokenString`, formed of multiple `TerminalToken`s and
  `NonTerminalToken`s which can be `add`ed. Terminal tokens include
  `NumberTerminalToken`, `StringTerminalToken` and `RegexTerminalToken`.
- `BnfRule`s are `add`ed to an instance of the `BnfParser`.

Using the parse tree
--------------------

Once a grammar has been loaded into an instance of `BnfParser`, the `parse()`
method is used to parse a given string and produce a parse tree (or null if the
string does not parse). This parse tree can then be explored in two ways:

1. In a manner similar to the DOM, by calling the `getChildren()` method of an
   instance of a `ParseNode` to get the list of its children (and so on,
   recursively);
2. Through the [Visitor design
   pattern](http://en.wikipedia.org/wiki/Visitor_pattern). In that case, one
   creates a class that implements the `ParseNodeVisitor` interface, and passes
   this visitor to the `ParseNode`'s `acceptPostfix()` or `acceptPrefix()`
   method, depending on the desired mode of traversal. The sample code shows an
   example of a visitor (class `GraphvizVisitor`), which produces a DOT file
   from the contents of the parse tree.

About the author
----------------

FlyParse was written (quickly) by Sylvain Hallé, assistant professor at
Université du Québec à Chicoutimi, Canada. It arose from the need to experiment
with various grammars without requiring compilation, as with classical parser
generators.
