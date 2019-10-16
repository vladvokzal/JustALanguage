import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.parboiled.BaseParser;
import org.parboiled.Node;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.ReportingParseRunner;


public class LanguageParser {

    private File fileToRead;

    LanguageParser(String filePath) {
        this.fileToRead = new File(filePath);
        this.parser = Parboiled.createParser(TreeBuilder.class);
    }

    Node<Object> parse() throws IOException {
        return new ReportingParseRunner<Object>(parser.Program())
                .run(FileUtils.readFileToString(fileToRead, Charset.forName("UTF-8")))
                .parseTreeRoot;
    }

    String getFullProgramCode() throws IOException {
        return FileUtils.readFileToString(fileToRead, Charset.forName("UTF-8"));
    }

    private TreeBuilder parser;
}
