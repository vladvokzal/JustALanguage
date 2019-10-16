import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

@BuildParseTree
public class TreeBuilder extends BaseParser<Object> {

    TreeBuilder() { }

    Rule Program(){
        return OneOrMore(Sequence(Statement(), '\n'));
    }

    Rule Statement(){
        return Sequence(ZeroOrMore(Whitepace()), FirstOf(AssignValue(), If(), While(), Print()));
    }

    Rule AssignValue(){
        return Sequence(Optional("var "), Var(), " = ", Expression());
    }

    Rule If(){
        return Sequence("if ", Expression(), Scope());
    }

    Rule While(){
        return Sequence("while ", Expression(), Scope());
    }

    Rule Print(){
        return Sequence("print ", Expression());
    }

    Rule Scope(){
        return Sequence(" {\n", Program(), ZeroOrMore(Whitepace()), "}");
    }

    Rule Expression(){
        return Sequence(Sum(), Optional(Sequence(FirstOf(" < ", " > ", " == ", " != ", " >= ", " <= "), Sum())));
    }

    Rule Sum(){
        return Sequence(
                Produce(),
                ZeroOrMore(
                        Sequence(
                                FirstOf(" + ", " - "),
                                Produce()
                        )
                )
        );
    }

    Rule Produce(){
        return Sequence(UnarMinus(), ZeroOrMore(Sequence(FirstOf(" * ", " / "), UnarMinus())));
    }

    Rule UnarMinus(){
        return Sequence(Optional('-'), Brackets());
    }

    Rule Brackets(){
        return FirstOf(Literal(), Var(), Sequence('(', Expression(), ')'));
    }

    Rule Var(){
        return Sequence(Letter(), ZeroOrMore(FirstOf(Letter(), Digit(), '_')));
    }

    Rule Literal(){
        return FirstOf(Int(), String());
    }

    Rule Int(){
        return Sequence(Optional('-'), OneOrMore(Digit()));
    }

    Rule String(){
        return Sequence('"', ZeroOrMore(NoneOf("\"")), '"');
    }

    Rule Digit(){
        return CharRange('0', '9');
    }

    Rule Letter(){
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'));
    }

    Rule Whitepace(){
        return AnyOf(" \t");
    }
}
