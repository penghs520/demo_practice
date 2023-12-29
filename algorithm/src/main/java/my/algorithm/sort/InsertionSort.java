package my.algorithm.sort;

/**
 * Created by penghs at 2023/12/28 8:22
 */
public class InsertionSort {


    public void sort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 先做到0~0范围有序
        // 再做到0~1范围有序
        // 再做到0~2范围有序
        // 最后0~n-1范围有序
        for (int i = 1; i < arr.length; i++) {
            /*for (int j = i ; j > 0; j--) {
                //如果当前的数比左边的小，则交换
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                }
            }*/
            for (int j = i; j > 0 && arr[j] < arr[j - 1]; j--) {
                swap(arr, j, j - 1);
            }
        }
    }

    private void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {7, 1, 23, 0, 1, 3, 2, 5, 4};
        new InsertionSort().sort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
