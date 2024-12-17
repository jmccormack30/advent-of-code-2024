import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

class Main {
	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        long value = decodeFilePart2(br);
        System.out.println("\nAnswer = " + value);
    }

    private static long decodeFile(BufferedReader br) throws Exception {
        String input = br.readLine();

        List<Integer> diskMap = getDiskMap(input);
        System.out.println(diskMap);
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

    private static long decodeFilePart2(BufferedReader br) throws Exception {
        String input = br.readLine();

        List<Integer> diskMap = getDiskMap(input);
        int[] arr = diskMap.stream().mapToInt(Integer::intValue).toArray();

        List<Pair> files = getMemoryGroups(arr);
        int end = arr.length - 1;

        for (Pair file : files) {
            Pair free = getNextFreeGroup(arr, 0, end);

            while (free != null && getSize(free) < getSize(file)) {
                free = getNextFreeGroup(arr, free.end + 1, end);
            }

            if (free != null) {
                int freeStart = free.start;

                int memStart = file.start;
                int memEnd = file.end;
    
                for (int i = memStart; i <= memEnd; i++) {
                    arr[freeStart++] = arr[i];
                    arr[i] = -1;
                }

                end = memStart - 1;
            }
        }
        
        return getCheckSum(arr);
    }

    private static List<Pair> getMemoryGroups(int[] arr) {
        List<Pair> files = new ArrayList<>();
        int i = arr.length - 1;

        while (i > 0) {
            int num = -1;

            while (num == -1 && i >= 0) {
                num = arr[i--];
            }
            i++;
            if (i <= 0) {
                files.add(new Pair(0, 0));
                return files;
            }
            int last = i;

            int num2 = num;

            while (num2 == num && i >= 0) {
                num2 = arr[i--];
            }
            i += (i >= 0) ? 2 : 1;
            if (i < 0) {
                files.add(new Pair(0, 0));
                return files;
            }

            int first = i;

            files.add(new Pair(first, last));
            i--;
        }

        return files;
    }

    private static Pair getNextFreeGroup(int[] arr, int i, int end) {
        Integer num = null;

        while (!Objects.equals(num, -1) && i < end) {
            num = arr[i++];
        }

        i--;
        if (i >= end || !Objects.equals(num, -1)) return null;
        int first = i;

        int num2 = -1;

        while (num2 == -1 && i < end) {
            num2 = arr[i++];
        }

        if (i >= end) {
            i = end;
        }
        else {
            i -= 2;
        }
        int last = i;

        return new Pair(first, last);
    }

    public record Pair(int start, int end) {}

    public static int getSize(Pair p) {
        return p.end - p.start;
    }
}