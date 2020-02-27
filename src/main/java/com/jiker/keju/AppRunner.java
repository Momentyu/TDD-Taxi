package com.jiker.keju;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppRunner {
    /*TODO
    1. args[0]为resources下的测试数据文件名，例如传入的args[0]值为"testData.txt"，注意并不包含文件路径。
    2. 你写的程序将把testDataFile作为参数加载此文件并读取文件内的测试数据，并对每条测试数据计算结果。
    3. 将所有计费结果拼接并使用\n分割，然后保存到receipt变量中。
    */

    //起步价:6元
    private static Integer startingPrice = 6;

    public static void main(String[] args) {
        String testDataFile=args[0];
        String receipt="";
        String path = AppRunner.class.getClassLoader().getResource(testDataFile).getPath();
        List<String> result = readFile(path);
        List<KmMin> kmMins = parsingResult(result);
        receipt = calculationResults(kmMins);
        System.out.println(receipt);
    }

    //计算打印结果
    private static String calculationResults(List<KmMin> kmMins) {
        StringBuilder sb = new StringBuilder();
        for (KmMin entry : kmMins) {
            Integer price = valuation(entry.getKm(), entry.getMin());
            sb.append("收费").append(price).append("元").append("\n");
        }
        return sb.toString();
    }

    //计算价格
    private static Integer valuation(Integer key, Integer value) {
        if (key <= 2) {
            int round = (int) Math.round(value * 0.25);
            return startingPrice + round;
        } else if (key > 2 && key <= 8) {
            return startingPrice + (int) Math.round(value * 0.25 + (key - 2) * 0.8);
        } else if (key > 8) {
            return startingPrice + (int) Math.round(value * 0.25 + (key - 2) * 0.8 + (key - 8) * 0.5);
        }
        return null;
    }

    //用正则取出公里数和分钟,封装到集合中
    private static List<KmMin> parsingResult(List<String> result) {
        List<KmMin> list = new ArrayList<KmMin>();
        for (String s : result) {
            String[] split = s.split(",");
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m1 = p.matcher(split[0]);
            Matcher m2 = p.matcher(split[1]);
            Integer km = Integer.valueOf(m1.replaceAll("").trim());
            Integer min = Integer.valueOf(m2.replaceAll("").trim());
            KmMin kmMin = new KmMin();
            kmMin.setKm(km);
            kmMin.setMin(min);
            list.add(kmMin);
        }
        return list;
    }

    //加载测试文件
    public static List<String> readFile(String file) {
        if (file == null || file.length() <= 0) {
            throw new RuntimeException("文件名称不能为空");
        }
        FileReader fr = null;
        BufferedReader br = null;
        List<String> content = new ArrayList<String>();
        try {
//            打开文件
            fr = new FileReader(file);
            br = new BufferedReader(fr);
//            读文件，并将读取的数据存储在list集合中
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    content.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            关闭文件，要求：反方向倒着关闭资源，先关闭br,再关闭fr
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            return content;
        }
    }

    //内部实体类,封装公里数和分钟数
    public static class KmMin {
        public KmMin() {

        }

        private Integer km;
        private Integer min;

        public Integer getKm() {
            return km;
        }

        public void setKm(Integer km) {
            this.km = km;
        }

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }
    }
}
