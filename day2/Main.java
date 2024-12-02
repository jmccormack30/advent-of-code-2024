import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        int total = 0;

        while ((line = br.readLine()) != null) {
            String[] numStrings = line.trim().split(" ");
            List<String> numStringsList = Arrays.asList(numStrings);
            total += decodeReportPart2(numStringsList);
        }

        return total;
    }

    private static int decodeReportPart1(String[] numStrings) {
        Boolean increasing = null;

        for (int i = 1; i < numStrings.length; i++) {
            int curNum = Integer.parseInt(numStrings[i]);
            int prevNum = Integer.parseInt(numStrings[i - 1]);

            int dif = curNum - prevNum;
            if (dif == 0) return 0;

            boolean curIncreasing = (dif > 0);
            if (increasing == null) {
                increasing = curIncreasing;
            }

            if (increasing != curIncreasing) {
                return 0;
            }
            else if (Math.abs(dif) >= 4) {
                return 0;
            }
        }

        return 1;
    }

    private static int decodeReportPart2(List<String> numStrings) {
        int total = getTotal(numStrings);

        boolean reportSafe = (total == numStrings.size() - 1);

        for (int i = 0; i < numStrings.size() && !reportSafe; i++) {
            List<String> numStringsCopy = new ArrayList<>(numStrings);
            numStringsCopy.remove(i);
            total = getTotal(numStringsCopy);
            reportSafe = (total == numStringsCopy.size() - 1);
        }

        return reportSafe ? 1 : 0;
    }

    private static boolean isLevelSafe(boolean increasing, boolean isLevelIncreasing, int curNum, int prevNum) {
        int diff = Math.abs(curNum - prevNum);
        return (diff >= 1 && diff <= 3) && increasing == isLevelIncreasing;
    }

    private static boolean isLevelIncreasing(int curNum, int prevNum) {
        return (curNum - prevNum) > 0;
    }

    private static int getTotal(List<String> numStrings) {
        Boolean increasing = null;
        int total = 0;

        for (int i = 1; i < numStrings.size(); i++) {
            int curNum = Integer.parseInt(numStrings.get(i));
            int prevNum = Integer.parseInt(numStrings.get(i - 1));

            boolean isLevelIncreasing = isLevelIncreasing(curNum, prevNum);
            if (increasing == null) increasing = isLevelIncreasing;

            boolean isLevelSafe = isLevelSafe(increasing, isLevelIncreasing, curNum, prevNum);

            if (isLevelSafe) {
                total += 1;
                continue;
            }
        }

        return total;
    }
}