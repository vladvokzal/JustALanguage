import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.ReportingParseRunner;

@BuildParseTree
public class LanguageParser extends BaseParser<Object>{

    public LanguageParser() {
        parser = Parboiled.createParser(LanguageParser.class);
    }

    void parse(String filePath) throws IOException {
        File fileToRead = new File(filePath);
        Object tmp = new ReportingParseRunner<Object>(parser.Program())
                .run(FileUtils.readFileToString(fileToRead, "utf-8"))
                .parseTreeRoot;
    }

    Rule Letter(){
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'));
    }

    Rule Digit(){
        return CharRange('0', '9');
    }

    Rule String(){
        return Sequence('"', ZeroOrMore(NoneOf("\"")), '"');
    }

    Rule Int(){
        return Sequence(Optional('-'), OneOrMore(Digit()));
    }

    Rule Literal(){
        return FirstOf(Int(), String());
    }

    Rule Var(){
        return Sequence(Letter(), ZeroOrMore(FirstOf(Letter(), Digit(), '_')));
    }

    Rule UnarMinus(){
        return Sequence(Optional('-'), Brackets());
    }

    Rule Produce(){
        return Sequence(UnarMinus(), ZeroOrMore(Sequence(FirstOf(" * ", " / "), UnarMinus())));
    }

    Rule Sum(){
        return Sequence(Produce(), ZeroOrMore(Sequence(FirstOf(" + ", " - "), Produce())));
    }

    Rule Expression(){
        return Sequence(Sum(), Optional(Sequence(FirstOf(" < ", " > ", " == ", " != ", " >= ", " <= "), Sum())));
    }

    Rule Brackets(){
        return FirstOf(Literal(), Var(), Sequence('(', Expression(), ')'));
    }

    Rule Scope(){
        return Sequence(" {\n", Program(), ZeroOrMore(Whitepace()), "}");
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

    Rule AssignValue(){
        return Sequence(Optional("var "), Var(), " = ", Expression());
    }

    Rule Program(){
        return OneOrMore(Sequence(Statement(), '\n'));
    }

    Rule Statement(){
        return Sequence(ZeroOrMore(Whitepace()), FirstOf(AssignValue(), If(), While(), Print()));
    }

    Rule Whitepace(){
        return AnyOf(" \t");
    }

    private LanguageParser parser;
}
