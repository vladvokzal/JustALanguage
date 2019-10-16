import org.apache.commons.io.FileUtils;
import org.parboiled.Node;

import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args){
        if (args.length != 1){
            System.out.println("Usage: [sourceCodeFile]");
            exit(1);
        }
        String fileName = args[0];
        LanguageParser parser = new LanguageParser(fileName);
        try {
            //Node root = parser.parse();
            Writer writer = new Writer();
            Compiler compiler = new Compiler(writer, parser);
            compiler.compile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
