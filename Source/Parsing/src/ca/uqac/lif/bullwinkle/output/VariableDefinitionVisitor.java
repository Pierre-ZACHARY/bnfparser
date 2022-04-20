package ca.uqac.lif.bullwinkle.output;

import ca.uqac.lif.bullwinkle.ParseNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VariableDefinitionVisitor implements OutputFormatVisitor{


    public HashMap<InputFileToken, Range> tokenDefinitionRangeHashMap = new HashMap<>();

    public List<AlreadyDefinedConflict> definitionConflicts = new ArrayList<>();

    public List<InputFileToken> tokenWithSameName(String name){
        ArrayList<InputFileToken> res = new ArrayList<>();
        for(InputFileToken inputFileToken : tokenDefinitionRangeHashMap.keySet()){
            if(name.equals(inputFileToken.content)){
                res.add(inputFileToken);
            }
        }
        return res;
    }

    /**
     * @param token : un token du fichier source
     * @return true si le token est en range d'un parent du même nom, false sinon ( en gros si le token a bien été def avant : true, sinon false )
     * Peut servir à check si on ne définit pas le même nom deux fois, et si un token est bien définit avant qu'il soit utilisé
     */
    public InputFileToken checkDefined(InputFileToken token){
        for(InputFileToken token1 : tokenWithSameName(token.content)){ // on recup tous les range des token du même nom, s'il y en a
            if(token.inputIndex> tokenDefinitionRangeHashMap.get(token1).start && token.inputIndex < tokenDefinitionRangeHashMap.get(token1).end){ // on regarde si la définition de celui ci a lieu dans le range d'un qui est déjà def
                return token1;
            }
        }
        return null;
    }

    private Integer currentIndex = 0;
    public String inputFile = "";

    public VariableDefinitionVisitor(String inputfile){
        this.inputFile = inputfile;
    }

    public void addToken(String token){
        // trouve la position du token définit 0 après current index;
        int tokenStartPosition = inputFile.indexOf(token);
        InputFileToken inputFileToken = new InputFileToken(token, tokenStartPosition);
        InputFileToken already_defined = checkDefined(inputFileToken);
        if (already_defined != null){
            definitionConflicts.add(new AlreadyDefinedConflict(inputFileToken, already_defined));
        }

        // remonte jusqu'au { qui précède le token ( ou début du fichier pour une définition de method ... )
        int blockStart = inputFile.substring(0, tokenStartPosition).lastIndexOf('{');
        if(blockStart == -1){

            tokenDefinitionRangeHashMap.put(inputFileToken, new Range(0, inputFile.length()-1));

            return ;
        }
        // trouve le } qui match
        int blockEnd = tokenStartPosition+token.length();
        ArrayList<Character> buffer = new ArrayList<Character>();
        int pos = tokenStartPosition;
        for(char c : inputFile.substring(tokenStartPosition).toCharArray()){
            if(c == '}' && buffer.isEmpty()){
                blockEnd = pos;
                break;
            } else if (c == '}') {
                buffer.remove(buffer.size()-1);
            } else if(c=='{'){
                buffer.add('{');
            }
            pos ++;
        }

        // le range du token en question, et  définit la current pos à l'index du token ( pour ne pas match le même ensuite dans le cas où plusieurs token avec le même nom sont def )
        tokenDefinitionRangeHashMap.put(inputFileToken, new Range(blockStart, blockEnd));
    }


    Boolean is_declaration = false;

    @Override
    public void visit(ParseNode node) throws VisitException {

        if(node.getValue() == null){
            if(is_declaration){
                addToken(node.getToken());
            }
            if(!node.getToken().equals("{") && !node.getToken().equals("}")){
                int startIndex = inputFile.indexOf(node.getToken());
                inputFile = inputFile.substring(0, startIndex)+ " ".repeat(node.getToken().length()) + inputFile.substring(startIndex+node.getToken().length());
            }
        }
        is_declaration = node.getToken().contains("declaration-name");
    }

    @Override
    public void pop() {

    }

    @Override
    public String toOutputString() {
        return null;
    }
}
