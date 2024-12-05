import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class Main {
	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int value = decodeFile(br);
        System.out.println("Answer = " + value);
    }

    private static int decodeFile(BufferedReader br) throws Exception {
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
                List<Integer> update = Arrays.stream(arr).map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                updatesList.add(update);
            }
        }

        SortByMap sortByMapComparator = new SortByMap(pageOrderMap);

        return updatesList.stream().mapToInt(list -> getValuePart2(list, pageOrderMap, sortByMapComparator)).sum();
    }

    private static int getValuePart1(List<Integer> update, Map<Integer, Set<Integer>> pageOrderMap) {
        for (int i = update.size() - 1; i > 0; i--) {
            int cur = update.get(i);
            Set<Integer> nums = pageOrderMap.get(cur);
            if (nums != null && !nums.isEmpty()) {
                boolean invalid = update.subList(0, i).stream().anyMatch(n -> nums.contains(n));
                if (invalid) return 0;
            }
        }
        return update.get(update.size() / 2);
    }

    private static int getValuePart2(List<Integer> update, Map<Integer, Set<Integer>> pageOrderMap, SortByMap comparator) {
        for (int i = update.size() - 1; i > 0; i--) {
            int cur = update.get(i);
            Set<Integer> nums = pageOrderMap.get(cur);
            if (nums != null && !nums.isEmpty()) {
                boolean invalid = update.subList(0, i).stream().anyMatch(n -> nums.contains(n));
                if (invalid) {
                    Collections.sort(update, comparator);
                    return update.get(update.size() / 2);
                }
            }
        }
        return 0;
    }

    public static class SortByMap implements Comparator<Integer> {

        private Map<Integer, Set<Integer>> pageOrderMap;

        public SortByMap(Map<Integer, Set<Integer>> pageOrderMap) {
            this.pageOrderMap = pageOrderMap;
        }

        @Override
        public int compare(Integer a, Integer b) {
            Set<Integer> aNums = pageOrderMap.get(a);
            Set<Integer> bNums = pageOrderMap.get(b);

            boolean aLessThan = (aNums != null && aNums.contains(b));
            boolean bLessThan = (bNums != null && bNums.contains(a));

            if (!aLessThan && !bLessThan) return 0;
            if (aLessThan && bLessThan) throw new RuntimeException("Invalid bi-directional relationship");

            return aLessThan ? -1 : 1;
        }
    };
}   