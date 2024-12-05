import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Main {
	public static void main(String[] args) {
        String fileName = "input.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int value = decodeFilePart1(br);
            System.out.println("Answer = " + value);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int decodeFilePart1(BufferedReader br) throws Exception {
        String line;
        Map<Integer, Set<Integer>> pageOrderMap = new HashMap<>();
        List<List<Integer>> updatesList = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line == null || line.length() == 0) {
                continue;
            }
            
            if (line.contains("|")) {
                String[] arr = line.split("\\|");
                int num1 = Integer.parseInt(arr[0]);
                int num2 = Integer.parseInt(arr[1]);
                pageOrderMap.computeIfAbsent(num1, k -> new HashSet<Integer>()).add(num2);
            }
            else {
                String[] arr = line.split(",");
                List<Integer> update = Arrays.stream(arr).map(str -> Integer.parseInt(str)).toList();
                updatesList.add(update);
            }
        }

        int total = 0;

        for (List<Integer> update : updatesList) {
            total += getValue(update, pageOrderMap);
        }
        
        return total;
    }

    private static int getValue(List<Integer> update, Map<Integer, Set<Integer>> pageOrderMap) {
        for (int i = update.size() - 1; i > 0; i--) {
            int cur = update.get(i);
            Set<Integer> nums = pageOrderMap.get(cur);
            if (nums != null && !nums.isEmpty()) {
                boolean invalid = update.subList(0, i).stream().anyMatch(n -> nums.contains(n));
                if (invalid) return 0;
            }
        }
        System.out.println(update);
        return update.get(update.size() / 2);
    }
}