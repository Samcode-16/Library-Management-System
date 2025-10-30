package library.utils;

import java.util.Scanner;

public class InputUtils {
    private static final Scanner SC = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return SC.nextLine().trim();
    }

    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SC.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}
