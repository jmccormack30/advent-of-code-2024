import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class Main {
	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        long value = decodeFile(br);
        System.out.println("\nAnswer = " + value);
    }

    private static long decodeFile(BufferedReader br) throws Exception {
        String input = br.readLine();

        List<Integer> diskMap = getDiskMap(input);
        int[] arr = diskMap.stream().mapToInt(Integer::intValue).toArray();

        int i = getNextFreeSlot(arr, 0);
        int j = getNextNumberSlot(arr, arr.length - 1);

        while (i < j) {
            arr[i] = arr[j];
            arr[j] = -1;

            i = getNextFreeSlot(arr, 0);
            j = getNextNumberSlot(arr, arr.length - 1);
        }

        return getCheckSum(arr);
    }

    private static List<Integer> getDiskMap(String input) {
        List<Integer> diskMap = new ArrayList<>();

        int id = 0;
        boolean isMemory = true;
        char[] arr = input.toCharArray();

        for (char c : arr) {
            int num = Character.getNumericValue(c);
            int value = (isMemory) ? id++ : -1;
            diskMap.addAll(Collections.nCopies(num, value));
            isMemory = !isMemory;
        }
        
        return diskMap;
    }

    private static int getNextFreeSlot(int[] arr, int i) {
        Integer num = null;

        while (!Objects.equals(num, -1) && i < arr.length) {
            num = arr[i++];
        }

        return (i >= arr.length) ? -1 : i - 1;
    }

    private static int getNextNumberSlot(int[] arr, int i) {
        int num = -1;

        while (num == -1 && i >= 0) {
            num = arr[i--];
        }

        return (i <= 0) ? -1 : i + 1;
    }

    private static long getCheckSum(int[] arr) {
        long sum = 0;

        for (int i = 0; i < arr.length; i++) {
            int num = arr[i];
            if (num == -1) continue;
            sum += (i * num);
        }

        return sum;
    }
}