package com.yee.quiz.Controller;
public class ArtTextPrinter {
    public static void show(){
        String word = "JAVAQUIZE";

        String[][] letters = new String[8][6];

        letters[0] = new String[]{
                "██████╗ ",
                "╚════██║",
                "    ██╔╝",
                "   ██╔╝ ",
                "██████╔╝",
                "╚═════╝ "
        };

        letters[1] = new String[]{
                " ██████╗ ",
                "██╔═══██╗",
                "██║   ██║",
                "████████║",
                "██╔═══██║",
                "██║   ██║"
        };

        letters[2] = new String[]{
                "██╗   ██╗",
                "██║   ██║",
                "╚██╗ ██╔╝",
                " ╚████╔╝ ",
                "  ╚██╔╝  ",
                "   ╚═╝   "
        };

        letters[3] = new String[]{
                "██╗   ██╗",
                "██║   ██║",
                "██║   ██║",
                "██║   ██║",
                "╚██████╔╝",
                " ╚═════╝ "
        };

        letters[4] = new String[]{
                "██╗",
                "██║",
                "██║",
                "██║",
                "██║",
                "╚═╝"
        };

        letters[5] = new String[]{
                " ██████╗ ",
                "██╔═══██╗",
                "██║   ██║",
                "██║▄▄ ██║",
                "╚██████╔╝",
                " ╚══▀▀═╝ "
        };

        letters[6] = new String[]{
                "███████╗",
                "╚══███╔╝",
                "  ███╔╝ ",
                " ███╔╝  ",
                "███████╗",
                "╚══════╝"
        };

        letters[7] = new String[]{
                "███████╗",
                "██╔════╝",
                "█████╗  ",
                "██╔══╝  ",
                "███████╗",
                "╚══════╝"
        };

        for (int row = 0; row < 6; row++) {
            StringBuilder lineBuilder = new StringBuilder();
            for (char c : word.toCharArray()) {
                int index = getLetterIndex(c);
                lineBuilder.append(letters[index][row]);
            }
            System.out.println(lineBuilder.toString());
        }
    }

    private static int getLetterIndex(char c) {
        switch (c) {
            case 'J': return 0;
            case 'A': return 1;
            case 'V': return 2;
            case 'U': return 3;
            case 'I': return 4;
            case 'Q': return 5;
            case 'Z': return 6;
            case 'E': return 7;
            default: throw new IllegalArgumentException("不支持的字母: " + c);
        }
    }

}