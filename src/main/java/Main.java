import java.io.IOException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Usage: [sourceCodeFile]");
            exit(1);
        }
        String fileName = args[0];
        LanguageParser parser = new LanguageParser();
        try {
            parser.parse(fileName);
        } catch (IOException e) {
            //ignored
        }

    }
}
