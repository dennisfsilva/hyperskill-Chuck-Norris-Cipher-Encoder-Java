package chucknorris;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please input operation (encode/decode/exit):");
            String operation = scanner.nextLine();

            switch (operation) {
                case "encode" -> {
                    System.out.println("Input string:");
                    String inputString = scanner.nextLine();
                    System.out.printf("Encoded string:\n%s\n\n", convertToCNUCode(convertToBinaryString(inputString)));
                }

                case "decode" -> {
                    System.out.println("Input encoded string:");
                    String encodedString = scanner.nextLine();

                    if (!isInputValid(encodedString)) {
                        System.out.println("Encoded string is not valid.\n");
                        continue;
                    }

                    System.out.println("Decoded string:");
                    System.out.println(binaryToString(cnuCodeToBinary(encodedString)));
                }

                case "exit" -> {
                    System.out.println("Bye!");
                    System.exit(0);
                }

                default -> System.out.printf("There is no '%s' operation\n", operation);
            }
        }
    }

    private static boolean isInputValid(String encodedString) {
        if (!isValidFirstBlock(encodedString)) {
            return false;
        }
        if (!isValidAmountOfBlocks(encodedString)) {
            return false;
        }
        if (!isValidBinaryStringLength(cnuCodeToBinary(encodedString))) {
            return false;
        }
        return encodedString.replaceAll("\\s", "").replaceAll("0", "").length() == 0;
    }

    private static boolean isValidBinaryStringLength(String binaryString) {
        return binaryString.length() % 7 == 0;
    }

    private static boolean isValidAmountOfBlocks(String encodedString) {
        return encodedString.trim().split(" ").length % 2 == 0;
    }

    private static boolean isValidFirstBlock(String encodedString) {
        int whiteSpaceCounter = 0, startingIndex = 0;
        for (int i = 0; i < encodedString.length(); i++) {
            if (encodedString.charAt(i) == ' ') {
                whiteSpaceCounter++;
            }

            if (whiteSpaceCounter == 2 || i == encodedString.length() - 1) {
                String[] pair = encodedString.substring(startingIndex, i + 1).trim().split(" ");
                if (pair[0].length() > 2) {
                    return false;
                }

                startingIndex = i;
                whiteSpaceCounter = 0;
            }
        }

        return true;
    }

    private static String convertToBinaryString(String input) {
        StringBuilder sb = new StringBuilder();

        char[] chars = input.toCharArray();
        for (char ch : chars) {
            sb.append(
                    String.format("%7s", Integer.toBinaryString(ch)).replaceAll(" ", "0")
            );
        }

        return sb.toString();
    }

    private static String convertToCNUCode(String binaryString) {
        StringBuilder sb = new StringBuilder();

        int sameCharsInARow = 0;
        for (int i = 0; i < binaryString.length(); i++) {
            sameCharsInARow++;

            if (i + 1 < binaryString.length() && binaryString.charAt(i + 1) != binaryString.charAt(i)
                    || i + 1 == binaryString.length()) {
                String oneZero = binaryString.charAt(i) == '1' ? "0 " : "00 ";
                sb.append(oneZero);

                sb.append("0".repeat(Math.max(0, sameCharsInARow)));

                sb.append(' ');
                sameCharsInARow = 0;
            }
        }

        return sb.toString().trim();
    }

    private static String cnuCodeToBinary(String cnuCode) {
        StringBuilder sb = new StringBuilder();

        int whiteSpaceCounter = 0, startingIndex = 0;
        for (int i = 0; i < cnuCode.length(); i++) {
            if (cnuCode.charAt(i) == ' ') {
                whiteSpaceCounter++;
            }

            if (whiteSpaceCounter == 2 || i == cnuCode.length() - 1) {
                String pair = cnuCode.substring(startingIndex, i + 1).trim();
                startingIndex = i;
                whiteSpaceCounter = 0;

                String[] pairArr = pair.split(" ");
                String oneZero = pairArr[0].equals("0") ? "1" : "0";
                sb.append(oneZero.repeat(pairArr[1].length()));
            }
        }

        return sb.toString();
    }

    private static String binaryToString(String binaryString) {
        ArrayList<String> lettersInBinary = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        // Split at 7.
        for (int i = 0; i < binaryString.length(); i += 7) {
            lettersInBinary.add(binaryString.substring(i, i + 7));
        }

        // Converts the binary to character.
        for (String letter : lettersInBinary) {
            sb.append((char) Integer.parseInt(letter, 2));
        }

        return sb.toString();
    }
}