package my.algorithm.search;

/**
 * Created by penghs at 2023/12/23 9:59
 */
public class BinarySearch {


    /**
     * 使用二分法查找一个局部最小值的index
     * 局部最小值的定义：一个数组中，如果一个数左右两边的数都比它大，那么这个数就是局部最小值
     */
    public int getAnyLocalMinimum(int[] arr) {
        //开头递增趋势，那么第一个数就是局部最小值
        if (arr.length == 1 || arr[0] < arr[1]) {
            return 0;
        }
        //结尾递减趋势，那么最后一个数就是局部最小值
        if (arr[arr.length - 1] < arr[arr.length - 2]) {
            return arr.length - 1;
        }
        int left = 0;
        int right = arr.length - 1;
        //int mid =  (right + left) / 2; 这么写，如果right和left都是很大的数，那么就会溢出
        int mid = left + (right - left) / 2;//这么写就不会溢出
        while (true) {
            if (arr[mid] < arr[mid - 1] && arr[mid] < arr[mid + 1]) {
                return mid;
            } else if (arr[mid] > arr[mid - 1]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
            mid = left + (right - left) / 2;
        }
    }

    public static void main(String[] args) {
        //int[] arr = {3, 2, 1, 4, 5, 6, 7};
        //int[] arr = {3, 1, 1, 2};输入不满足，要求两两不相等
        //int[] arr = {3, 1, 2};
        int[] arr = {3, 1};
        System.out.println(new BinarySearch().getAnyLocalMinimum(arr));
    }
}
