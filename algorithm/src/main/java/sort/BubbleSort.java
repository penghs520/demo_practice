package sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by penghs at 2023/12/23 9:27
 */
public class BubbleSort {

    public void sort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 0 ~ N-1
        // 0 ~ N-2
        // 0 ~ N-3
        for (int i = arr.length - 1; i > 0; i--) {
            // 0 ~ i
            for (int j = 0; j < i; j++) {
                // j 和 j+1 比较，大的往后放
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }

    }

    private void swap(int[] arr, int j, int i) {
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 5, 4};
        int[] arr2 = {1, 3, 2, 5, 4};
        new BubbleSort().sort(arr);
        new SelectionSort().sort(arr2);
        ArrayComparator.compare(arr, arr2);
    }
}
