import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int value = decodeFilePart2(br);
        System.out.println("Answer = " + value);
    }

    private static int decodeFilePart1(BufferedReader br) throws Exception {
        StringBuilder strBuilder = new StringBuilder();
        br.lines().forEach(line -> strBuilder.append(line));
        String input = strBuilder.toString();

        String pattern = "mul\\([0-9]{1,3},[0-9]{1,3}\\)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);

        int sum = 0;

        while (matcher.find()) {
            sum += getMultiplyValue(matcher.group());
        }

        return sum;
    }

    private static int decodeFilePart2(BufferedReader br) throws Exception {
        StringBuilder strBuilder = new StringBuilder();
        br.lines().forEach(line -> strBuilder.append(line));
        String input = strBuilder.toString();

        String pattern = "mul\\([0-9]{1,3},[0-9]{1,3}\\)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);

        String doPattern = "do()";
        Pattern compiledDoPattern = Pattern.compile(doPattern);
        Matcher doMatcher = compiledDoPattern.matcher(input);

        String dontPattern = "don't()";
        Pattern compiledDontPattern = Pattern.compile(dontPattern);
        Matcher dontMatcher = compiledDontPattern.matcher(input);

        List<Integer> doIndices = new ArrayList<>();
        while (doMatcher.find()) {
            doIndices.add(doMatcher.start());
        }

        List<Integer> dontIndices = new ArrayList<>();
        while (dontMatcher.find()) {
            dontIndices.add(dontMatcher.start());
        }

        int sum = 0;

        while (matcher.find()) {
            int index = matcher.start();
            String func = matcher.group();

            Optional<Integer> closestDo = doIndices.stream().filter(i -> i < index).max(Comparator.naturalOrder());
            Optional<Integer> closestDont = dontIndices.stream().filter(i -> i < index).max(Comparator.naturalOrder());

            int doIndex = closestDo.isPresent() ? closestDo.get() : 0;
            int dontIndex = closestDont.isPresent() ? closestDont.get() : 0;

            boolean doMultiply = ((doIndex == 0 && dontIndex == 0) || (doIndex > dontIndex));

            sum += doMultiply ? getMultiplyValue(func) : 0;
        }

        return sum;
    }

    private static int getMultiplyValue(String input) {
        input = input.substring(4, input.length()-1);
        String[] nums = input.split(",");
        return (Integer.parseInt(nums[0]) * Integer.parseInt(nums[1]));
    }
}