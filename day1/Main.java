import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class Main {
    public static void main(String[] args) {
        String fileName = "input.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int value = decodeFilePart2(br);
            System.out.println("Answer = " + value);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int decodeFilePart1(BufferedReader br) throws Exception {
        List<Integer> left = new ArrayList<>(); 
        List<Integer> right = new ArrayList<>();

        String line;

        while ((line = br.readLine()) != null) {
            String[] nums = line.trim().split("   ");

            left.add(Integer.parseInt(nums[0]));
            right.add(Integer.parseInt(nums[1]));
        }

        Collections.sort(left);
        Collections.sort(right);

        return IntStream.range(0, left.size())
            .map(i -> Math.abs(left.get(i) - right.get(i)))
            .sum();
    }

    private static int decodeFilePart2(BufferedReader br) throws Exception {
        List<Integer> left = new ArrayList<>(); 
        Map<Integer, Integer> rightMap = new HashMap<>();

        String line;

        while ((line = br.readLine()) != null) {
            String[] nums = line.trim().split("   ");

            left.add(Integer.parseInt(nums[0]));
            rightMap.compute(Integer.parseInt(nums[1]), (k, v) -> v == null ? 1 : v + 1);
        }

        return left.stream()
            .mapToInt(num -> num * rightMap.getOrDefault(num, 0))
            .sum();
    }
}