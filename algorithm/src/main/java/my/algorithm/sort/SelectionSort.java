package my.algorithm.sort;

/**
 * Created by penghs at 2023/12/23 9:21
 */
public class SelectionSort {

    public void sort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i; // 最小值的下标
            for (int j = i + 1; j < arr.length; j++) {
                minIndex = arr[j] < arr[minIndex] ? j : minIndex;
            }
            swap(arr, i, minIndex);
        }
    }

    private void swap(int[] arr, int i, int minIndex) {
        int tmp = arr[i];
        arr[i] = arr[minIndex];
        arr[minIndex] = tmp;
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 5, 4};
        new SelectionSort().sort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
