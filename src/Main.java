import java.util.ArrayList;
import java.util.List;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        String str = "7+22+2+(22/11*3)-1-2/2*3-(2*3)";
        List<String> strList = ParseString(str);
        boolean hasPh = false;

        for(int i = 0; i < strList.size(); ++i) {
            if (((String)strList.get(i)).equals("(")) {
                int[] phIndexes = GetPhIndex(strList, i);
                strList.set(i, CalculatePh(strList, i));

                for(int j = i + 1; j <= phIndexes[1]; ++j) {
                    strList.remove(i + 1);
                }
                i = 0;
            }
        }

        System.out.println(Calculate(strList));
    }

    public static List<String> ParseString(String str) {
        int digitCount = 0;
        int strSize = str.length();
        List<String> strList = new ArrayList<>();

        for(int i = 0; i < strSize; ++i) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                ++digitCount;
                if (i == strSize - 1) {
                    strList.add(str.substring(i - digitCount + 1, strSize));
                }
            } else {
                char var;
                if (str.charAt(i) != '(' && str.charAt(i) != ')') {
                    if (digitCount > 0) {
                        strList.add(str.substring(i - digitCount, i));
                        digitCount = 0;
                    }

                    var = str.charAt(i);
                    strList.add("" + var);
                } else {
                    if (digitCount > 0) {
                        strList.add(str.substring(i - digitCount, i));
                        digitCount = 0;
                    }

                    var = str.charAt(i);
                    strList.add("" + var);
                }
            }
        }

        return strList;
    }

    public static String CalculatePh(List<String> stringList, int beginIndex) {
        List<String> newStringList = new ArrayList<>();
        String result = "";
        boolean isInPh = false;
        int[] phIndex = GetPhIndex(stringList, beginIndex);

        for(int i = phIndex[0] + 1; i < phIndex[1]; ++i) {
            if (((String)stringList.get(i)).equals("(")) {
                isInPh = true;
                newStringList.add(CalculatePh(stringList, i));
            } else if (((String)stringList.get(i)).equals(")")) {
                isInPh = false;
            } else if (!isInPh) {
                newStringList.add((String)stringList.get(i));
            }
        }

        return Calculate(newStringList);
    }

    public static int[] GetPhIndex(List<String> stringList, int beginIndex) {
        int[] phLocation = new int[]{beginIndex, 0};
        int phCounter = 0;

        for(int i = beginIndex; i < stringList.size(); ++i) {
            if (((String)stringList.get(i)).equals("(")) {
                ++phCounter;
            } else if (((String)stringList.get(i)).equals(")")) {
                --phCounter;
            }

            if (phCounter == 0) {
                phLocation[1] = i;
                return phLocation;
            }
        }

        return phLocation;
    }

    public static String Calculate(List<String> strList) {
        int operationResult = 0;
        int listSize = 0;
        boolean isPrevOp = false;
        List<String> firstOperationStringList = new ArrayList<>();

        for(int i = 0; i < strList.size(); ++i) {
            if (!((String)strList.get(i)).equals("*") && !((String)strList.get(i)).equals("/")) {
                if (isPrevOp) {
                    isPrevOp = false;
                } else {
                    firstOperationStringList.add((String)strList.get(i));
                }
            } else {
                listSize = firstOperationStringList.size();
                switch (strList.get(i)) {
                    case "*" ->
                            operationResult = Integer.parseInt((String) firstOperationStringList.get(listSize - 1)) * Integer.parseInt((String) strList.get(i + 1));
                    case "/" ->
                            operationResult = Integer.parseInt((String) firstOperationStringList.get(listSize - 1)) / Integer.parseInt((String) strList.get(i + 1));
                }

                firstOperationStringList.remove(firstOperationStringList.size() - 1);
                firstOperationStringList.add(String.valueOf(operationResult));
                isPrevOp = true;
            }
        }

        List<String> secondOperationStringList = new ArrayList<>();

        for(int i = 0; i < firstOperationStringList.size(); ++i) {
            if (!((String)firstOperationStringList.get(i)).equals("+") && !((String)firstOperationStringList.get(i)).equals("-")) {
                if (isPrevOp) {
                    isPrevOp = false;
                } else {
                    secondOperationStringList.add((String)firstOperationStringList.get(i));
                }
            } else {
                listSize = secondOperationStringList.size();
                operationResult = switch ((String) firstOperationStringList.get(i)) {
                    case "+" ->
                            Integer.parseInt((String) secondOperationStringList.get(listSize - 1)) + Integer.parseInt((String) firstOperationStringList.get(i + 1));
                    case "-" ->
                            Integer.parseInt((String) secondOperationStringList.get(listSize - 1)) - Integer.parseInt((String) firstOperationStringList.get(i + 1));
                    default -> operationResult;
                };

                secondOperationStringList.remove(listSize - 1);
                secondOperationStringList.add(String.valueOf(operationResult));
                isPrevOp = true;
            }
        }

        return secondOperationStringList.get(0);
    }
}
