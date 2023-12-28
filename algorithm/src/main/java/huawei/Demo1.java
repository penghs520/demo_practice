package huawei;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by penghs at 2023/12/25 8:45
 */
public class Demo1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String hex = in.nextLine();
        //截取0x
        hex = hex.substring(2);
        int decimal = hexToDecimal(hex);
        System.out.println("十进制数: " + decimal);
    }

    public static int hexToDecimal(String hex) {
        int decimal = 0;
        int len = hex.length();
        for (int i = 0; i < len; i++) {
            char hexChar = hex.charAt(len - 1 - i);
            decimal += hexCharToDecimal(hexChar) * (int) Math.pow(16, i);
        }
        return decimal;
    }

    public static int hexCharToDecimal(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else if (hexChar >= 'a' && hexChar <= 'f') {
            return hexChar - 'a' + 10;
        } else {
            throw new IllegalArgumentException("Invalid hex character: " + hexChar);
        }
    }
}
