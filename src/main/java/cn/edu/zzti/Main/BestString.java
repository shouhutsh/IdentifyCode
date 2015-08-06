package cn.edu.zzti.Main;

/**
 * Created by ae-mp02 on 2015/8/6.
 */
public class BestString {
    private BestString() {
    }

    public static int similarity(String str1, String str2){
        return  100 - (100 * compute(str1, str2) / max(str1.length(), str2.length()));
    }

    private static int compute(String str1, String str2){
        if(str1 == null || str1.length() == 0)
            return str2.length();
        if(str2 == null || str2.length() == 0)
            return str1.length();

        int i, j, temp;
        int array[][] = new int[str1.length()][str2.length()];

        for(i = 0; i < str1.length(); ++i){
            array[i][0] = i;
        }
        for(j = 0; j < str2.length(); ++j){
            array[0][j] = j;
        }

        for(i = 1; i < str1.length(); ++i){
            for(j = 1; j < str2.length(); ++j){
                if(str1.charAt(i) == str2.charAt(j))
                    temp = 0;
                else
                    temp = 1;

                array[i][j] = minThree(array[i-1][j] + 1, array[i][j-1] + 1, array[i-1][j-1]) + temp;
            }
        }
        return array[i-1][j-1];
    }

    private static int minThree(int i, int j, int k){
        return min(min(i, j), k);
    }

    private static int min(int i, int j){
        return i < j ? i : j;
    }

    private static int max(int i, int j){
        return i > j ? i : j;
    }

    public static final void main(String[] args){
        String str1 = "string1";
        String str2 = "string2";

        System.out.printf("%d", similarity(str1, str2));
    }
}
