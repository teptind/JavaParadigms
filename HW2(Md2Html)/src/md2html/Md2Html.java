package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    private static void closing(BufferedReader in) {
        try {
            in.close();
        } catch (IOException e) {
            System.err.println("IOException");
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("2 files expected");
            return;
        }
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0])), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
            return;
        }

        Splitter splitter = new Splitter(in);
        Parser parser = new Parser();
        StringBuilder answer = new StringBuilder();
        while (true) {
            String curr = splitter.getPar();
            if (curr == null) {
                break;
            }
            answer.append(parser.getRes(curr));
        }

        try (PrintWriter out = new PrintWriter(args[1], StandardCharsets.UTF_8)) {
            out.print(answer.toString());
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
        closing(in);
    }
}
