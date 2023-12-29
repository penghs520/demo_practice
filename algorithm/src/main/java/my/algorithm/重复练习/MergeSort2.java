package my.algorithm.重复练习;

/**
 * Created by penghs at 2023/12/29 8:00
 */
public class MergeSort2 {


    public static void main(String[] args) {
        int[] arr = {4, 5, 2, 4, 3, 1};
        new MergeSort2().sort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    public void sort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    private void process(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        int mid = l + ((r - l) >> 1);
        process(arr, l, mid);
        process(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    private void merge(int[] arr, int l, int mid, int r) {
        int[] swap = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = mid + 1;

        //p1、p2都向右移动，如果都没有越界，则比较它们的大小，谁小谁填入swap数组
        while (p1 <= mid && p2 <= r) {
            swap[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        //如果有一个越界了，那么剩下的那个可以直接加入swap
        //这两个while实际只有一个被执行
        while (p1 <= mid) {
            swap[i++] = arr[p1++];
        }
        while (p2 <= r) {
            swap[i++] = arr[p2++];
        }
        //将swap复制到原数组
        for (i = 0; i < swap.length; i++) {
            arr[l + i] = swap[i];
        }
    }

}
