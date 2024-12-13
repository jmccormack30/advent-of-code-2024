import java.io.BufferedReader;
import java.io.FileReader;

class Main {

    enum Operation {
        ADD,
        SUB,
        MUL,
        DIV
    }

	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        long value = decodeFile(br);
        System.out.println("\nAnswer = " + value);
    }

    private static long decodeFile(BufferedReader br) throws Exception {
        String line;
        long total = 0;

        while ((line = br.readLine()) != null) {
            String[] arr = line.split(":");

            long value = Long.parseLong(arr[0]);

            String[] strNums = arr[1].trim().split(" ");
            int[] nums = new int[strNums.length];

            for (int i = 0; i < strNums.length; i++) {
                nums[i] = Integer.parseInt(strNums[i]);
            }

            total += decodeEquation(value, nums);
        }

        return total;
    }

    private static long decodeEquation(long value, int[] nums) {
        int total = nums[0];
        int i = 1;

        long x = getValue(value, total, i, nums);
        return (x == value) ? value : 0;
    }

    private static long getValue(long value, long total, int i, int[] nums) {
        if (i >= nums.length) return total;
        int y = nums[i];
        i += 1;

        long sum = performOperation(total, y, Operation.ADD);
        long sumTotal = getValue(value, sum, i, nums);
        if (sumTotal == value) return sumTotal;

        long product = performOperation(total, y, Operation.MUL);
        long productTotal = getValue(value, product, i, nums);
        if (productTotal == value) return productTotal;

        return 0;
    }

    private static long performOperation(long x, long y, Operation operation) {
        switch (operation) {
            case ADD:
                return (x + y);
            case MUL:
                return (x * y);
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }
}