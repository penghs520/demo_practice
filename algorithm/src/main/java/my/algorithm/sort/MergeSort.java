package my.algorithm.sort;

/**
 * Created by penghs at 2023/12/23 12:42
 * 归并排序
 */
public class MergeSort {

    public void sort(int[] arr) {
        process(arr, 0, arr.length - 1);
    }

    private void process(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        int mid = l + ((r - l) >> 1);
        process(arr, l, mid);//先把左边排序好
        process(arr, mid + 1, r);//再把右边排序好
        merge(arr, l, mid, r);//将两个有序子列合并
    }

    private void merge(int[] arr, int l, int mid, int r) {
        int[] swap = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = mid + 1;

        //p1、p2都向右移动，如果都没有越界，则比较它们的大小，谁小谁填入swap数组
        while (p1 <= mid && p2 <= r) {
            //取最小的
            //为了保证稳定性。当p1==p2时，取p1
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
        for (i = 0; i < swap.length; i++) {
            arr[l + i] = swap[i];
        }
    }

    public static void main(String[] args) {
        int[] arr = {2, 1, 4, 5, 3};
        new MergeSort().sort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

}
