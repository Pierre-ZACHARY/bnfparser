package ca.uqac.lif.bullwinkle.output.sail;

import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.output.OutputFormatVisitor;
import ca.uqac.lif.bullwinkle.output.sail.errors.AlreadyDefinedConflict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VariableDefinitionVisitor implements OutputFormatVisitor {


    public HashMap<InputFileTokenWithType, Range> tokenDefinitionRangeHashMap = new HashMap<>();
    public HashMap<String, StructDef> structDefinitionHashMap = new HashMap<>();
    public HashMap<String, MethodDef> methodDefinitionHashMap = new HashMap<>();

    public List<AlreadyDefinedConflict> definitionConflicts = new ArrayList<>();
    private boolean process_declaration;
    private boolean method_declaration;
    private boolean enum_declaration;
    private boolean struct_declaration;
    private boolean signal_declaration;
    private boolean method_with_type_declaration;
    private Range tempMethodRange;
    private InputFileToken tempMethod;
    private boolean struct_attribute_declaration;
    private InputFileTokenWithType lastEnum;
    private boolean enum_name_declaration;
    private boolean function_parameters_declaration2;

    public List<InputFileTokenWithType> tokenWithSameName(String name){
        ArrayList<InputFileTokenWithType> res = new ArrayList<>();
        for(InputFileTokenWithType inputFileToken : tokenDefinitionRangeHashMap.keySet()){
            if(name.equals(inputFileToken.t.content)){
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
    public InputFileTokenWithType checkDefined(InputFileToken token){
        for(InputFileTokenWithType token1 : tokenWithSameName(token.content)){ // on recup tous les range des token du même nom, s'il y en a
            if(token.inputIndex> tokenDefinitionRangeHashMap.get(token1).start && token.inputIndex < tokenDefinitionRangeHashMap.get(token1).end){ // on regarde si la définition de celui ci a lieu dans le range d'un qui est déjà def
                return token1;
            }
        }
        return null;
    }

    private Integer currentIndex = 0;
    public String inputFile = "";

    private StructDef lastStruct = null;
    private MethodDef lastMethod = null;

    public VariableDefinitionVisitor(String inputfile){
        this.inputFile = inputfile;
    }

    public void addToken(String token){
        // trouve la position du token définit 0 après current index;
        System.out.println(token);
        int tokenStartPosition = inputFile.indexOf(token);
        InputFileToken inputFileToken = new InputFileToken(token, tokenStartPosition);
        InputFileTokenWithType already_defined = checkDefined(inputFileToken);
        if (already_defined != null){
            definitionConflicts.add(new AlreadyDefinedConflict(inputFileToken, already_defined));
        }

        // remonte jusqu'au { qui précède le token ( ou début du fichier pour une définition de method ... )
        if(process_declaration){
            process_declaration = false;
            tokenDefinitionRangeHashMap.put(new InputFileTokenWithType(inputFileToken, SailType.Process), new Range(0, inputFile.length()-1));
            return ;
        }
        if(method_declaration){
            method_declaration = false;
            MethodDef value = new MethodDef();
            lastMethod = value;
            methodDefinitionHashMap.put(inputFileToken.content, value);

            if(method_with_type_declaration){
                tempMethod = inputFileToken;
                tempMethodRange = new Range(0, inputFile.length()-1);
            }
            else{
                InputFileTokenWithType key = new InputFileTokenWithType(inputFileToken, SailType.Method);
                tokenDefinitionRangeHashMap.put(key, new Range(0, inputFile.length()-1));
                lastMethod.method_def = key;
            }
            return ;
        }
        if(enum_declaration){
            enum_declaration = false;
            InputFileTokenWithType key = new InputFileTokenWithType(inputFileToken, SailType.Enum);
            lastEnum = key;
            tokenDefinitionRangeHashMap.put(key, new Range(0, inputFile.length()-1));
            return ;
        }
        if(struct_declaration){
            struct_declaration = false;
            InputFileTokenWithType structdef = new InputFileTokenWithType(inputFileToken, SailType.Struct);
            lastStruct = new StructDef(structdef);
            structDefinitionHashMap.put(inputFileToken.content, lastStruct);
            tokenDefinitionRangeHashMap.put(structdef, new Range(0, inputFile.length()-1));
            return ;
        }
        if(enum_name_declaration){
            tokenDefinitionRangeHashMap.put(new InputFileTokenWithType(inputFileToken, lastEnum.t.content), tokenDefinitionRangeHashMap.get(lastEnum));
            enum_name_declaration = false;
            return;
        }


        // trouve le } qui match
        int blockEnd = tokenStartPosition+token.length();
        ArrayList<Character> buffer = new ArrayList<Character>();
        int pos = tokenStartPosition;
        char[] array = inputFile.substring(tokenStartPosition).toCharArray();
        for(int i = 0; i<array.length; i++){
            if(buffer.isEmpty() && (array[i] == '}' || i< array.length-1 && array[i]=='|' && array[i+1]=='|') ){
                blockEnd = pos;
                break;
            } else if (array[i] == '}') {
                buffer.remove(buffer.size()-1);
            } else if(array[i]=='{'){
                buffer.add('{');
                if(function_parameters_declaration){
                    buffer.remove(0);
                    function_parameters_declaration = false;
                }
            }
            pos ++;
        }

        if(signal_declaration){
            tokenDefinitionRangeHashMap.put(new InputFileTokenWithType(inputFileToken, SailType.Signal), new Range(tokenStartPosition, blockEnd));
            signal_declaration = false;
            return;
        }




        // le range du token en question, et  définit la current pos à l'index du token ( pour ne pas match le même ensuite dans le cas où plusieurs token avec le même nom sont def )
        temp = inputFileToken ;
        tempRange = new Range(tokenStartPosition, blockEnd);
    }

    InputFileToken temp = null;
    Range tempRange = null;


    boolean is_declaration = false;
    boolean function_parameters_declaration = false;


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
        is_declaration = node.getToken().equals("<declaration-name>");
        if(node.getToken().equals("<function-parameters-declaration>")){
            function_parameters_declaration = true;
            function_parameters_declaration2 = true;
        }

        if(node.getToken().equals("<process-declaration>")){
            process_declaration = true;
        }
        if(node.getToken().equals("<method-declaration>")){
            method_declaration = true;
        }
        if(node.getToken().equals("<method-with-type-declaration>")){
            method_with_type_declaration = true;
        }
        if(node.getToken().equals("<enum-declaration>")){
            enum_declaration = true;
        }
        if(node.getToken().equals("<enum-name-declaration>")){
            enum_name_declaration = true;
        }
        if(node.getToken().equals("<struct-declaration>")){
            struct_declaration = true;
        }
        if(node.getToken().equals("<struct-attributes-declaration>")){
            struct_attribute_declaration = true;
        }
        if(node.getToken().equals("<signal-declaration>")){
            signal_declaration = true;
        }
        if(node.getToken().equals("<type-declaration>")){

            if(method_with_type_declaration && temp == null){
                assert tempMethod != null;
                assert tempMethodRange != null;
                method_with_type_declaration = false;
                InputFileTokenWithType inputFileTokenWithType = new InputFileTokenWithType(tempMethod, typeFormat(node));
                inputFileTokenWithType.type = SailType.Method;
                tokenDefinitionRangeHashMap.put(inputFileTokenWithType, tempMethodRange);
                lastMethod.method_def = inputFileTokenWithType;
                tempMethod = null;
                tempRange = null;
            }
            else{
                assert temp != null;
                assert tempRange != null;
                String existing_type = typeFormat(node);
                InputFileTokenWithType key = new InputFileTokenWithType(temp, existing_type);
                tokenDefinitionRangeHashMap.put(key, tempRange);
                if(structDefinitionHashMap.containsKey(existing_type)){
                    // on a instancié une struct, on souhaite donc ajouté les différents attribut de la struct au même range que son parent
                    String struct_object_name = temp.content;
                    for(InputFileTokenWithType attr : structDefinitionHashMap.get(existing_type).attributes){
                        InputFileTokenWithType struct_attr_def = new InputFileTokenWithType(new InputFileToken(struct_object_name+"."+attr.t.content, temp.inputIndex), attr.existing_type);
                        tokenDefinitionRangeHashMap.put(struct_attr_def, tempRange);
                    }
                }
                if(struct_attribute_declaration){
                    assert lastStruct!=null;
                    lastStruct.addAttr(key);
                    struct_attribute_declaration = false;
                }
                if(function_parameters_declaration2){
                    assert lastMethod!=null;
                    lastMethod.addParam(key);
                    function_parameters_declaration2 = false;
                }
                temp = null;
                tempRange = null;
            }

        }
    }

    public String typeFormat(ParseNode type_declaration){
        return type_declaration.toString().replaceAll("<type-declaration>", "").replaceAll("\n", "").replaceAll(":", "").replaceAll("<type>", "").replaceAll("<existing-type>", "").replaceAll(" ", "");
    }

    @Override
    public void pop() {

    }

    @Override
    public String toOutputString() {
        return null;
    }
}
