package it.collectors.business.jdbc;

/*
 * Modified by Giuseppe Della Penna to simplify and integrate it in the course
 * JDBC examples
 */
/*
 * Modified version of the com.ibatis.common.jdbc.ScriptRunner class
 * from the iBATIS Apache project.
 */
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool to run database scripts
 */
public class SQLScriptRunner_JDBC {

    private static final String DEFAULT_DELIMITER = ";";
    private String delimiter;
    private static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER[ \t]*(.+)";
    private final Pattern delimiter_line_pattern;

    public SQLScriptRunner_JDBC(String delimiter_line_regex, String delimiter) {
        this.delimiter_line_pattern = Pattern.compile(delimiter_line_regex);
        this.delimiter = delimiter;
    }

    public SQLScriptRunner_JDBC() {
        this(DELIMITER_LINE_REGEX, DEFAULT_DELIMITER);
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void runScript(Connection connection, String script) throws ApplicationException {
        runScript(connection, new StringReader(script));
    }

    public void runScript(Connection connection, Reader reader) throws ApplicationException {
        String command = "";
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line;
            try ( Statement statement = connection.createStatement()) {
                while ((line = lineReader.readLine()) != null) {
                    line = line.trim();
                    while (line.length() > 0) {
                        if (line.startsWith("//") || line.startsWith("--")) { //non supporta commenti a fine riga!
                            line = ""; //skip
                        } else {
                            Matcher matcher = delimiter_line_pattern.matcher(line);
                            if (matcher.matches()) {
                                //update local delimiter
                                setDelimiter(matcher.group(1));
                                line = (line.substring(0, matcher.start()) + line.substring(matcher.end())).trim();
                            } else {
                                String line_segment;
                                boolean delimiter_found = false;
                                if (line.contains(delimiter)) {
                                    delimiter_found = true;
                                    line_segment = line.substring(0, line.indexOf(delimiter));
                                    if (line.indexOf(delimiter) + 1 <= line.length()) {
                                        line = line.substring(line.indexOf(delimiter) + 1).trim();
                                    } else {
                                        line = "";
                                    }
                                } else {
                                    line_segment = line;
                                    line = "";
                                }
                                //update overall statement
                                command += line_segment + " ";
                                if (delimiter_found) {
                                    //execute statement (end delimiter found)
                                    statement.execute(command);
                                    command = "";
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            throw new ApplicationException("Errore di esecuzione dello script SQL", ex);
        }
    }
}