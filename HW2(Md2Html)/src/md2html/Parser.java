package md2html;

import java.util.HashMap;
import java.util.Map;

class Parser {
    private String par;
    private int ind;

    private static final Map<String, String> TAGS = new HashMap<>();

    static {
        TAGS.put("*", "em");
        TAGS.put("_", "em");
        TAGS.put("**", "strong");
        TAGS.put("__", "strong");
        TAGS.put("--", "s");
        TAGS.put("`", "code");
        TAGS.put("++", "u");
        TAGS.put("~", "mark");
    }

    public static final String[] START_TOKENS = new String[]{"**", "__", "--", "_", "*", "`", "++", "~"};

    private int ifHeader() {
        int t = 0;
        while (par.charAt(t) == '#') {
            t++;
        }
        return par.charAt(t) == ' ' && t != 0 ? t : 0;
    }

    private boolean startsWith(String start) {
        boolean matches = ind + start.length() <= par.length() && par.substring(ind, ind + start.length()).equals(start);
        if (matches) {
            ind += start.length();
        }
        return matches;
    }

    private StringBuilder getText(char br) {
        StringBuilder ans = new StringBuilder();
        while (par.charAt(ind) != br) {
            ans.append(par.charAt(ind));
            ind++;
        }
        return ans;
    }

    private StringBuilder solve(String breaker) {
        StringBuilder curRes = new StringBuilder();
        String tag = TAGS.get(breaker);

        while (true) {
            if (ind >= par.length()) {
                break;
            }
            if (startsWith(breaker)) {  // check end of block
                ind--;
                StringBuilder sb = new StringBuilder();
                if (!breaker.equals("\0") && !breaker.equals("]")) {
                    sb.append("<").append(tag).append(">").append(curRes).append("</").append(tag).append(">");
                } else {
                    sb.append(curRes);
                }
                return sb;
            }
            if (par.charAt(ind) == '\\') {  // check escape character
                if (par.charAt(ind + 1) != '\0') {
                    ind++;
                }
                curRes.append(par.charAt(ind));
            } else if (par.charAt(ind) == '\0') {  // check end symbol
                break;
            } else if (startsWith("[")) {  // check link
                StringBuilder href = solve("]");
                ind += 2;
                StringBuilder link = getText(')');
                curRes.append("<a href=\'").append(link).append("\'>").append(href).append("</a>");
            } else if (startsWith("![")) {  // check image
                StringBuilder alt = getText(']');
                ind += 2;
                StringBuilder link = getText(')');
                curRes.append("<img alt=\'").append(alt).append("\' src=\'").append(link).append("\'>");
            } else if (par.charAt(ind) == '&') {  // check special symbols
                curRes.append("&amp;");
            } else if (par.charAt(ind) == '<') {
                curRes.append("&lt;");
            } else if (par.charAt(ind) == '>') {
                curRes.append("&gt;");
            } else {  // check breakers
                boolean foundBlock = false;
                for (String startToken : START_TOKENS) {
                    if (startsWith(startToken)) {
                        curRes.append(solve(startToken));
                        foundBlock = true;
                        break;
                    }
                }
                if (!foundBlock) {
                    curRes.append(par.charAt(ind));
                }
            }
            ind++;
        }
        StringBuilder sb = new StringBuilder();
        if (!breaker.equals("\0")) {
            sb.append(breaker);
        }
        sb.append(curRes);
        return sb;
    }

    String getRes(String s) {
        par = s;
        StringBuilder res = new StringBuilder();
        ind = 0;
        String mainTag;
        int h = ifHeader();
        if (h != 0) {
            mainTag = "h" + h;
            ind += h + 1;
        } else {
            mainTag = "p";
        }
        res.append("<").append(mainTag).append(">");
        res.append(solve("\0"));
        res.append("</").append(mainTag).append(">");
        return res.toString() + "\n";
    }
}
