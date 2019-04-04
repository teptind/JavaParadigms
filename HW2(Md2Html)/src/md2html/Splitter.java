package md2html;

import java.io.*;

class Splitter {
    private BufferedReader in;
    Splitter(BufferedReader fin) {
        in = fin;
    }

    String getPar() {
        StringBuilder res = new StringBuilder();
        String curr = "";
        try {
            while (curr != null && curr.isEmpty()) { // skipping empty lines
                curr = in.readLine();
            }
            while (curr != null && !curr.isEmpty()) { // building new paragraph
                res.append(curr);
                res.append('\n');
                curr = in.readLine();
            }
            if (res.length() == 0) {
                return null;
            }
            if (res.charAt(res.length() - 1) == '\n') {
                res.deleteCharAt(res.length() - 1);
            }
            res.append('\0');
            return res.toString();
        } catch (IOException e) {
            System.err.println(e.toString());
            return null;
        }
    }
}
