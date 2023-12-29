package my.algorithm.heap;

/**
 * Created by penghs at 2023/12/25 13:40
 * 使用数组实现的一个大根堆，堆是一个完全二叉树，大根根堆的性质是：每个节点的值都大于等于其左右孩子节点的值
 */
public class BigRootHeap {


    /**
     * heapify，移除某个根节点后，需要重新调整堆，使其满足大根堆的性质
     */
    public void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1;//左孩子的下标
        while (left < heapSize) {//表示还有孩子（不只是左孩子，因为完全二叉树左孩子的index会比大孩子小）
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;//largest表示左右孩子中较大的那个的下标
            largest = arr[largest] > arr[index] ? largest : index;//largest表示左右孩子中较大的那个和根节点中较大的那个的下标
            if (largest == index) {//如果根节点就是最大的，那么就不用调整了
                break;
            }
            swap(arr, largest, index);//否则，交换根节点和左右孩子中较大的那个
            index = largest;//然后继续调整
            left = index * 2 + 1;
        }

    }

    private void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }
}
