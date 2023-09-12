import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        ListNode dummy = new ListNode(-1);
        ListNode p = dummy;
        ListNode p1=list1,p2=list2;
        while (p1!=null && p2!=null){
            if (p1.val > p2.val){
                p.next = p2;
                p2 = p2.next;
            }else {
                p.next = p1;
                p1 = p1.next;
            }
            p = p.next;
        }
        if (p1!=null){
            p.next = p1;
        }
        if (p2!=null){
            p.next = p2;
        }
        return dummy.next;
    }

    //找到链表中间节点
    public ListNode middleNode(ListNode head) {
        ListNode slow=head,fast=head;
        while (fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    //回文链表
    public boolean isPalindrome(ListNode head) {

        ListNode slow,fast;
        slow=fast=head;
        while (fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        if (fast != null) {
            slow = slow.next;
        }
        ListNode left = head;
        ListNode right = reverse(slow);
        while (right!=null){
            if (left.val!=right.val){
                return false;
            }
            left=left.next;
            right=right.next;
        }
        return true;

    }

    //反转链表
    public ListNode reverse(ListNode head){
        ListNode pre = null,cur = head;
        ListNode tmp = null;
        while (cur!=null){
            tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        return pre;
    }

    //两数相加
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        ListNode dummy = new ListNode(-1);
        ListNode cur = dummy;
        ListNode p1=l1,p2=l2;
        int carry=0;
        while (p1!=null || p2!=null || carry > 0){
            int val = carry;
            if (p1!=null){
                val+=p1.val;
                p1=p1.next;
            }
            if (p2!=null){
                val+= p2.val;
                p2=p2.next;
            }
            cur.next = new ListNode(val%10);
            cur = cur.next;
            carry = val/10;
        }
        return dummy.next;
    }

    public ListNode deleteNode(ListNode head, int val) {
        return null;
    }


    //逐行输出文本文件
    public static void readFileContent(String filePath) throws IOException {

        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

        // 装饰者模式使得 BufferedReader 组合了一个 Reader 对象
        // 在调用 BufferedReader 的 close() 方法时会去调用 Reader 的 close() 方法
        // 因此只要一个 close() 调用即可
        bufferedReader.close();
    }


    public static void main(String[] args) throws IOException {

        readFileContent("D:\\桌面文件\\今日八股内容.txt");
    }

}
