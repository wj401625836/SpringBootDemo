package com.project.plan;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Scanner;

/**
 * Created by Barry on 2018/5/21.
 */
public class JavaTest {


    @Before
    public void setUp() throws Exception {
        System.out.println("setUp...");
    }

    @Test
    public void test2() {
        for (int i=101;i<=200 ;i++ )
        {
            boolean a=true;
            for (int j=2;j<i ;j++ )
            {
                if (i%j==0)
                {
                    a=false;
                    break;
                }
            }
            if (a)
            {
                System.out.println(i+"是素数");
            }
        }
    }
    @Test
    public void test4(){
        System.out.println("test4 start...");

        int n = 232;
        int k=2;
        System.out.print(n+"=");//输出第一步格式
        while(k<=n){//初值k为2,n为输入的数字,在程序执行的过程中k渐渐变大(k++),n渐渐变小(n/k)
            if(k==n){//当n和k相等的时候,就直接输出n的值(此时输出k也行,因为n==k)
                System.out.println(n);
                break;
            } else if(n%k==0){
                System.out.print(k+"*");//如果n <> k，但n能被k整除，则应打印出k的值
                n = n/k;//n除以k的商,作为新的正整数你n
            }else{
                k++;//如果n不能被k整除，则用k+1作为k的值
            }
        }


        System.out.println("test4 end...");
    }

}