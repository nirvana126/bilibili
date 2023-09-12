import java.util.HashSet;
import java.util.Set;

public class BonusMethods {
    public static int countBonusMethods(int n) {
        // 创建大小为n+1的数组用于存储不同金额下的方法总数
        int[] methods = new int[n + 1];
        
        // 当金额为0时，只有一种方法，即不给奖金
        methods[0] = 1;
        
        // 遍历计算每个金额下的方法总数
        for (int i = 1; i <= n; i++) {
            // 若i大于等于1，则可以选择发放1元奖金
            if (i >= 1)
                methods[i] += methods[i - 1];
            
            // 若i大于等于2，则可以选择发放2元奖金
            if (i >= 2)
                methods[i] += methods[i - 2];
            
            // 若i大于等于3，则可以选择发放3元奖金
            if (i >= 3)
                methods[i] += methods[i - 3];
        }
        
        return methods[n];
    }

    public static int reverseDistinct(int num){
        Set<Integer> set = new HashSet<>();
        int result=0;
        while (num>0){
            int tmp = num%10;
            if (!set.contains(tmp)){
                result=result*10+tmp;
                set.add(tmp);
            }
            num=num/10;
        }
        return result;
    }

    public static void main(String[] args) {
        int n = 3;
        int totalMethods = countBonusMethods(n);
        System.out.println("不同的发放方法总数：" + totalMethods);
    }
}
