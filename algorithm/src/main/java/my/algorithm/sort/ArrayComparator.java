package my.algorithm.sort;

/**
 * Created by penghs at 2023/12/23 9:37
 */
public class ArrayComparator {
    public static void compare(int[] arr1, int[] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) {
            throw new IllegalArgumentException("参数不合法");
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                System.out.println("false");
                throw new IllegalArgumentException("两个数组不相等");
            }
        }
    }
}
