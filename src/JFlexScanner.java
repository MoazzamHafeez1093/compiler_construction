import java.io.FileReader;

public class JFlexScanner {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java JFlexScanner <inputfile>");
            return;
        }

        try {
            Yylex scanner = new Yylex(new FileReader(args[0]));
            Token token;

            System.out.println("========================================");
            System.out.println("JFlex Scanner Output");
            System.out.println("========================================");

            while ((token = scanner.yylex()) != null) {
                System.out.println(token);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
