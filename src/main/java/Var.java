import Types.VariableTypes;

public class Var {

    public Var(VariableTypes TYPE, String name, int index){
        this.index = index;
        this.name = name;
        this.TYPE = TYPE;
    }

    public VariableTypes getTYPE() {
        return TYPE;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    private VariableTypes TYPE;
    private String name;
    private int index;
}
