import Types.VariableTypes;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.parboiled.Node;
import java.io.IOException;
import java.util.HashMap;

public class Compiler {

    public Compiler(Writer writer, LanguageParser languageParser){
        this.languageParser = languageParser;
        this.writer = writer;
    }


    public void compile() throws IOException {
        Node<Object> root = languageParser.parse();
        if (null == root || root.hasError()){
            return;
        }
        compileProgram(root);
        writer.write();
    }

    private void compileStatement(Node<Object> root){
        Node node = root.getChildren().get(1).getChildren().get(0);
        String nodeLabel = node.getLabel();
        if (nodeLabel.equals("If")){

        } else if (nodeLabel.equals("While")){

        } else if (nodeLabel.equals("Print")){
            //System.out.println("victory!");
            compilePrint(node);
        }
    }

    private void executeIf(Node<Object> node){

    }

    private void executeWhile(Node<Object> node){

    }

    private void executePrint(Node<Object> node){

    }

    private VariableTypes compileExpression(Node<Object> node){
//        Node chldFirst = node.getChildren().get(1);
//        if (chldFirst.getStartIndex() == chldFirst.getEndIndex()){
//            return compileSum(node.getChildren().get(0));
//        }
        return VariableTypes.STRING;
//        Node chldSecond = (Node)chldFirst.getChildren().get(0);
//        String operation = chldSecond.getChildren().get(0).toString();
//        Node leftOperand = node.getChildren().get(0);
//        Object rightOperand = chldSecond.getChildren().get(1);

//        VariableTypes leftType = ();
//        VariableTypes rightType = ();

//        if (operation.equals(" == ")){
//
//        } else if(operation.equals(" >= ")){
//
//        }
//        return null;
    }

    private void compileProgram(Node<Object> root){
        for(Node<Object> tmp : root.getChildren()){
            compileStatement(tmp.getChildren().get(0));
        }
        //root.getChildren().forEach(x -> compileStatement(x.getChildren().get(0)));
    }

    private VariableTypes compileSum(Node<Object> node){
        Node chld1 = node.getChildren().get(1);
        VariableTypes resType = compileProduce(node.getChildren().get(0));
        if (chld1.getStartIndex() == chld1.getEndIndex()){
            return resType;
        }
        //chld1.getChildren().forEach(x -> forEachChildrenSumTask((Node)x, resType));
        return resType;
    }

    private void forEachChildrenSumTask(Node currentChildren, VariableTypes resType){
        VariableTypes currentType = compileUnarMinus((Node)currentChildren.getChildren().get(1));
        if (resType != currentType){
            return;
        }
        try {
            Node tmp = (Node)currentChildren.getChildren().get(0);
            String action = languageParser.getFullProgramCode().substring(tmp.getStartIndex(), tmp.getEndIndex());
            if (action.equals(" + ")){
                compileAdd(tmp, currentType);
            } else if (action.equals(" - ")){
                compileSub(tmp, currentType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VariableTypes compileUnarMinus(Node<Object> node){
        Node chld = node.getChildren().get(0);
        VariableTypes resType = compileBrackets(node.getChildren().get(1));
        if (chld.getStartIndex() == chld.getEndIndex()){
            return resType;
        }
        writer.getBaseVisitor().visitInsn(Opcodes.INEG);
        return resType;
    }

    private VariableTypes compileBrackets(Node<Object> node){
        Node chld = node.getChildren().get(0);
        if (chld.getLabel().equals("Sequence")){
            return compileExpression((Node)chld.getChildren().get(1));
        } else if (chld.getLabel().equals("Var")){
            return compileVariable(chld);
        } else if (chld.getLabel().equals("Literal")){
            return compileLiteral(chld);
        }
        return null;
    }

    private VariableTypes compileVariable(Node<Object> node){
        return null;
    }

    private VariableTypes compileProduce(Node<Object> node){
        Node chld = node.getChildren().get(1);
        VariableTypes resType = compileUnarMinus(node.getChildren().get(0));
        if (chld.getEndIndex() == chld.getStartIndex()){
            return resType;
        }
        //chld.getChildren().forEach(x -> forEachChildrenProduceTask((Node)x, resType));
        return resType;
    }

    private void forEachChildrenProduceTask(Node currentChildren, VariableTypes resType){
        VariableTypes currentType = compileUnarMinus((Node)currentChildren.getChildren().get(1));
        if (resType != currentType){
            return;
        }
        try {
            Node tmp = (Node)currentChildren.getChildren().get(0);
            String action = languageParser.getFullProgramCode().substring(tmp.getStartIndex(), tmp.getEndIndex());
            if (action.equals(" / ")){
                compileDiv(tmp, currentType);
            } else if (action.equals(" * ")){
                compileMul(tmp, currentType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void compileAdd(Node<Object> node, VariableTypes type){
        if(type != VariableTypes.INT){
            return;
        }
        writer.getBaseVisitor().visitInsn(Opcodes.IADD);
    }

    private void compileSub(Node<Object> node, VariableTypes type){
        if(type != VariableTypes.INT){
            return;
        }
        writer.getBaseVisitor().visitInsn(Opcodes.ISUB);
    }

    private void compileDiv(Node<Object> node, VariableTypes type){
        if (type != VariableTypes.INT){
            return;
        }
        writer.getBaseVisitor().visitInsn(Opcodes.IDIV);
    }

    private void compileMul(Node<Object> node, VariableTypes type){
        if (type != VariableTypes.INT){
            return;
        }
        writer.getBaseVisitor().visitInsn(Opcodes.IMUL);
    }

    private void compilePrint(Node<Object> node){
        VariableTypes type = compileExpression(node.getChildren().get(1));
        writer.getBaseVisitor().visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        switch (type){
            case INT:
                writer.getBaseVisitor().visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                break;
            case STRING:
                writer.getBaseVisitor().visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                break;
            case BOOL:
                writer.getBaseVisitor().visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);

        }

    }

    private VariableTypes compileLiteral(Node<Object> rootNode){
        return null;
    }


    private HashMap<Var, String> variablesMap = new HashMap<Var, String>();
    private Writer writer;
    private LanguageParser languageParser;
}
