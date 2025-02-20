import java.io.*;
import java.util.*;

class Token {
    String type;
    String value;

    Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return type + " : " + value;
    }
}

class SymbolTableEntry {
    String name;
    String type;
    String value;
    String scope;

    SymbolTableEntry(String name, String type, String value, String scope) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.scope = scope;
    }

    public String toString() {
        return "Name: " + name + ", Type: " + type + ", Value: " + value + ", Scope: " + scope;
    }
}

class Lexer {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "grab", "shout", "numba", "floatie", "chary", "nocap", "cap", "constg", "constl" , "bool"
    ));

    private static final List<String> errors = new ArrayList<>();
    private static final Map<String, String> stateTable = new HashMap<>();

    public static void main(String[] args) {
        String filename = "C:\\Users\\Wastech\\IdeaProjects\\Assignment\\src\\sample.aa"; // Change this to your file path
        if (!filename.endsWith(".aa")) {
            System.out.println("Invalid file extension. Expected .aa file.");
            return;
        }

        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder content = new StringBuilder();
            int c;
            while ((c = br.read()) != -1) {
                content.append((char) c);
            }
            br.close();
            validateBrackets(content.toString());
            List<Token> tokens = tokenize(content.toString());

            // Check if the entire code is inside {}
            if (!content.toString().trim().startsWith("{") || !content.toString().trim().endsWith("}")) {
                errors.add("Syntax Error: Code must be wrapped in curly brackets `{}`.");
            }

            // Print tokens
            for (Token token : tokens) {
                System.out.println(token);
            }

            // Print errors (if any)
            if (!errors.isEmpty()) {
                System.out.println("\nErrors found:");
                for (String error : errors) {
                    System.out.println(error);
                }
            } else {
                System.out.println("\nNo lexical errors found.");
            }

            // Print state table for debugging
            SymbolTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Map<String, SymbolTableEntry> table = new HashMap<>();

    public static void addEntry(String name, String type, String value, String scope) {
        if (!table.containsKey(name)) {
            table.put(name, new SymbolTableEntry(name, type, value, scope));
        } else {
            System.out.println("Error: Duplicate entry for " + name);
        }
    }

    public static void display() {
        System.out.println("\n--- Symbol Table ---");
        for (SymbolTableEntry entry : table.values()) {
            System.out.println(entry);
        }
    }

    public static void SymbolTable() {
        addEntry("numba", "DATATYPE", "-", "GLOBAL");
        addEntry("floatie", "DATATYPE", "-", "GLOBAL");
        addEntry("chary", "DATATYPE", "-", "GLOBAL");
        addEntry("nocap", "DATATYPE", "-", "GLOBAL");

        // Adding input/output
        addEntry("grab", "INPUT", "-", "GLOBAL");
        addEntry("shout", "OUTPUT", "-", "GLOBAL");

        // Adding constants
        addEntry("constg", "CONSTANT", "-", "GLOBAL");
        addEntry("constl", "CONSTANT", "-", "LOCAL");

        // Adding arithmetic operators
        addEntry("+", "ARITHMETIC_OP", "-", "GLOBAL");
        addEntry("-", "ARITHMETIC_OP", "-", "GLOBAL");
        addEntry("*", "ARITHMETIC_OP", "-", "GLOBAL");
        addEntry("/", "ARITHMETIC_OP", "-", "GLOBAL");
        addEntry("%", "ARITHMETIC_OP", "-", "GLOBAL");
        display();
    }
    public static void validateBrackets(String input) {
        Stack<Character> curly = new Stack<>();
        Stack<Character> round = new Stack<>();
        Stack<Character> square = new Stack<>();


        Map<Character, Character> matchingBracket = Map.of(')', '(', ']', '[', '}', '{');

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '{') {
                curly.push(ch);
            } else if (ch == '}') {
                if (curly.isEmpty() || curly.pop() != matchingBracket.get(ch)) {
                    errors.add("Syntax Error: Unmatched closing bracket `" + ch + "` at position " + i);
                }
            }
            else if (ch=='(')
            {
round.push(ch);
            }
            else if (ch=='[')
            {
square.push(ch);
            }
            else if (ch==')')
            {if (round.isEmpty() || round.pop() != matchingBracket.get(ch)) {
                errors.add("Syntax Error: Unmatched closing bracket `" + ch + "` at position " + i);
            }

            }else if (ch==']')
            {
                if (square.empty() || square.pop() != matchingBracket.get(ch)) {
                    errors.add("Syntax Error: Unmatched closing bracket `" + ch + "` at position " + i);
                }
            }
        }

        while (!curly.isEmpty()) {
            errors.add("Syntax Error: Unmatched opening bracket `" + curly.pop() + "`");
        }
        while (!round.isEmpty()) {
            errors.add("Syntax Error: Unmatched opening bracket `" + round.pop() + "`");
        }
        while (!square.isEmpty()) {
            errors.add("Syntax Error: Unmatched opening bracket `" + square.pop() + "`");
        }
    }

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int state = 0;
        StringBuilder buffer = new StringBuilder();
        boolean inComment = false;
        boolean inMultiLineComment = false;
        int lineNumber = 1;
        boolean invalid = false;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            invalid = false;

            if (ch == '\n') {
                lineNumber++;
            }

            // Handle Single-Line Comments
            if (inComment) {
                if (ch == '\n') {
                    inComment = false;
                }
                continue;
            }

            // Handle Multi-Line Comments (#** ... **#)
            if (inMultiLineComment) {
                if (i + 2 < input.length() && input.charAt(i) == '*' && input.charAt(i + 1) == '*' && input.charAt(i + 2) == '#') {
                    inMultiLineComment = false;
                    i += 2;
                }
                continue;
            }

            switch (state) {
                case 0:
                    if (Character.isWhitespace(ch)) {
                        continue;
                    }else if (ch== '{' || ch=='}') {
                        continue;
                    }


                    else if (Character.isLowerCase(ch)) {
                        buffer.append(ch);
                        state = 1;
                    } else if (Character.isDigit(ch)) {
                        invalid=false;
                        while(true)
                        {
                            i++;
                            ch = input.charAt(i);
                            if ( !Character.isDigit(ch) && ch!='^' ) {
                                invalid=true;
                            }else if (ch=='^')
                            {
                                tokens.add(new Token("Power", "^"));
                            }

                            if(ch ==' ' || ch== '\n' || ch==';')
                                break;
                        }
                        if (invalid)
                            continue;
                        buffer.append(ch);
                        state = 2;
                    }else if (ch=='\"' )
                    {String word="";
                        i++;
                        ch=input.charAt(i);
                        while (i<input.length() && ch!='\"')
                        {
                            word+=ch;
                            ch=input.charAt(i);
                            i++;
                        }
                        tokens.add(new Token("String",word ));
                        buffer.setLength(0);
                    }
                    else if ("+-*/%".indexOf(ch) != -1) {
                        tokens.add(new Token("ARITH_OP", String.valueOf(ch)));
                    } else if (ch == '=') {
                        tokens.add(new Token("ASSIGN_OP", "="));
                    }
                    else if (ch=='~')
                    {
                        inComment=true;
                    }else if (ch == ';') {
                        tokens.add(new Token("SEMICOLON", ";"));
                    } else if (ch == '#' && i + 2 < input.length() && input.charAt(i + 1) == '*' && input.charAt(i + 2) == '*') {
                        inMultiLineComment = true;
                        i += 2;
                    } else if (!Character.isLowerCase(ch)) {
                        while ( input.charAt(i) != ' ' && input.charAt(i) != '\n') {
                            ch=input.charAt(i);
                            buffer.append(ch);
                            i++;
                        }
                        errors.add("Invalid identifier: starts with invalid character at line " + lineNumber +" = "+ buffer  );
                        buffer.setLength(0);
                    } else {
                        errors.add("Unexpected character: " + ch + " at line " + lineNumber);
                    }
                    break;

                case 1: // Identifiers or Keywords
                    if (Character.isLowerCase(ch) || Character.isDigit(ch) || ch == '_') {
                        buffer.append(ch);
                    } else {
                        String word = buffer.toString();
                        buffer.setLength(0);
                        state = 0;
                        i--;

                        if (KEYWORDS.contains(word)) {
                            tokens.add(new Token("KEYWORD", word));

                            // ✅ Check if "shout" is followed by parentheses
                            if (word.equals("shout")) {
                                i++;
                                int k=0;
                                while (i < input.length() && input.charAt(i) == ' ') i++;  // Skip spaces

                                    if (i < input.length() && input.charAt(i) != '(') {
                                        errors.add("Syntax Error: `shout` must be followed by parentheses `()` at line " + lineNumber);
                                    }
                                    k=i;
                                while (k < input.length() && input.charAt(k) != ')') {
                                    k++ ;
                                if (input.charAt(k)==';') {
                                    errors.add("Syntax Error: Brackets not closed expected ')' at line " + lineNumber);
                                    break;
                                }

                                }



                            }
                        } else {
                            tokens.add(new Token("IDENTIFIER", word));
                        }
                    }
                    break;
                case 2: // Numbers
                    if (Character.isDigit(ch)) {
                        buffer.append(ch);
                    } else if (ch == '.') {
                        buffer.append(ch);
                        state = 3;
                    } else {
                        tokens.add(new Token("NUMBER", buffer.toString()));
                        buffer.setLength(0);
                        state = 0;
                        i--; // Reprocess current char
                    }
                    break;

                case 3: // Floating-point numbers
                    if (Character.isDigit(ch)) {
                        buffer.append(ch);
                    } else {
                        String num = buffer.toString();
                        if (num.matches("\\d+\\.\\d+")) {
                            tokens.add(new Token("FLOAT", num));
                        } else {
                            errors.add("Invalid float format: " + num + " at line " + lineNumber);
                        }
                        buffer.setLength(0);
                        state = 0;
                        i--; // Reprocess current char
                    }
                    break;
            }
        }

        return tokens;
    }
}
