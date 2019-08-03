package com.zyz;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by yaozheng on 18/7/14.
 */
public class GenerateRandomNumber {

    private static final List<String> oldRandomNumberList = new LinkedList<String>();


    private static final List<String> newRandomNumberList = new LinkedList<String>();

    static {
        try {
            File file = new File("/Users/yaozheng/Desktop/code.txt");
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if(null==line||line.isEmpty()){
                    break;
                }
                oldRandomNumberList.add(line);
                //System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void saveCode(List<String> codeList){
        BufferedWriter out = null;
        try{
            /* 写入Txt文件 */
            File writename = new File("/Users/yaozheng/Desktop/code_new.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            out = new BufferedWriter(new FileWriter(writename));
            for (String code : codeList){
                out.write("16808"+code+"\r\n"); // \r\n即为换行
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.flush(); // 把缓存区内容压入文件
                    out.close(); // 最后记得关闭文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void saveAllCode(List<String> codeList){
        BufferedWriter out = null;
        try{
            /* 写入Txt文件 */
            File writename = new File("/Users/yaozheng/Desktop/old_code.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            out = new BufferedWriter(new FileWriter(writename));
            for (String code : codeList){
                out.write(code+"\r\n"); // \r\n即为换行
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.flush(); // 把缓存区内容压入文件
                    out.close(); // 最后记得关闭文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {



//        File file = new File("/Users/yaozheng/Desktop/Code from zyz_2016-8-23.xls");
//
//        List excelList = readExcel(file);
//        System.out.println("list中的数据打印出来");
//        for (int i = 0; i < excelList.size(); i++) {
//            List list = (List) excelList.get(i);
//            for (int j = 0; j < list.size(); j++) {
//                System.out.print(list.get(j));
//            }
//            System.out.println();
//        }




        for(int i=0;i<40000;){
            String code = getRandomNumber();
            if (isExist(code)){
                continue;
            }
            oldRandomNumberList.add(code);
            newRandomNumberList.add(code);
            System.out.println(getRandomNumber());
            i++;
        }

        saveAllCode(oldRandomNumberList);
        saveCode(newRandomNumberList);



    }


    //生成随机数
    public static String getRandomNumber(){
        Random rand=new Random();//生成随机数
        StringBuilder cardNnumer=new StringBuilder();
        for(int a=0;a<10;a++){
            cardNnumer = cardNnumer.append(rand.nextInt(10));
        }
        return cardNnumer.toString();
    }

    public static boolean isExist(String randomNumber){
        if(null==randomNumber||randomNumber.isEmpty()){
            return true;
        }
        if(oldRandomNumberList.isEmpty()){
             return false;
        }
        if(oldRandomNumberList.contains(randomNumber)){
            return true;
        }

        return false;
    }

    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public static List readExcel(File file) {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = null;

                wb = Workbook.getWorkbook(is);

            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        if(cellinfo.isEmpty()){
                            continue;
                        }
                        innerList.add(cellinfo);
                        //System.out.print(cellinfo);
                    }
                    outerList.add(i, innerList);
                   // System.out.println();
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
