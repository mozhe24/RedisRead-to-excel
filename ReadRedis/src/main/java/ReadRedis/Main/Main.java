package ReadRedis.Main;

/**
 * Created by caodongj on 2017/10/20.
 */

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Main {
    /**
     * @功能：手工构建一个简单格式的Excel
     */

    public static void main(String[] args) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("标签表");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("一级标签");
        cell.setCellStyle(style);

        cell = row.createCell((short) 1);
        cell.setCellValue("二级标签");
        cell.setCellStyle(style);

        cell = row.createCell((short) 2);
        cell.setCellValue("三级标签");
        cell.setCellStyle(style);

        // 第四步，写入实体数据 实际应用中这些数据从数据库得到，
        CreateExcel(wb, sheet);
    }

    private static void CreateExcel(HSSFWorkbook wb, HSSFSheet sheet) {
        HSSFRow row;
        System.out.println("this is personal redis search tools!");
        System.out.println("    -i    imel          通过32位的imel来查询redis里的value值      e.g. redis -i imel");
        System.out.println("    -p    phone         通过手机号码来查询redis里的value值         e.g. redis -p phone");
        System.out.println("    -v    version       查看redis的版本                          e.g. redis -v");
        System.out.println("    -h    help          查看redis帮助                            e.g. redis -h");

        System.out.println("please input your cmd:");
        Scanner scanner = new Scanner(System.in);
        String cmd_order = scanner.nextLine();
        String[] cmdlist = cmd_order.split(" ");
        List<TagMsg> order = new List<TagMsg>() {
            public int size() {
                return 0;
            }

            public boolean isEmpty() {
                return false;
            }

            public boolean contains(Object o) {
                return false;
            }

            public Iterator<TagMsg> iterator() {
                return null;
            }

            public Object[] toArray() {
                return new Object[0];
            }

            public <T> T[] toArray(T[] a) {
                return null;
            }

            public boolean add(TagMsg tagMsg) {
                return false;
            }

            public boolean remove(Object o) {
                return false;
            }

            public boolean containsAll(Collection<?> c) {
                return false;
            }

            public boolean addAll(Collection<? extends TagMsg> c) {
                return false;
            }

            public boolean addAll(int index, Collection<? extends TagMsg> c) {
                return false;
            }

            public boolean removeAll(Collection<?> c) {
                return false;
            }

            public boolean retainAll(Collection<?> c) {
                return false;
            }

            public void clear() {

            }

            public TagMsg get(int index) {
                return null;
            }

            public TagMsg set(int index, TagMsg element) {
                return null;
            }

            public void add(int index, TagMsg element) {

            }

            public TagMsg remove(int index) {
                return null;
            }

            public int indexOf(Object o) {
                return 0;
            }

            public int lastIndexOf(Object o) {
                return 0;
            }

            public ListIterator<TagMsg> listIterator() {
                return null;
            }

            public ListIterator<TagMsg> listIterator(int index) {
                return null;
            }

            public List<TagMsg> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        Boolean search_flag = false;
        Predis predis = new Predis();
        if (cmdlist[0].equals("redis")) {
            if (order != null) {
                if (cmdlist[1].equals("-i")) {
                    List<TagMsg> ilist = predis.imel_cmd_parse(cmdlist[0], cmdlist[1], cmdlist[2]);
                    order = ilist;
                    search_flag = true;
                }
                if (cmdlist[1].equals("-p")) {
                    List<TagMsg> plist = predis.phone_cmd_parse(cmdlist[0], cmdlist[1], cmdlist[2]);
                    order = plist;
                    search_flag = true;
                }
                if (cmdlist[1].equals("-v")) {
                    predis.version_cmd_parse(cmdlist[0], cmdlist[1]);
                }
                if (cmdlist[1].equals("-h")) {
                    predis.help_cmd_parse(cmdlist[0], cmdlist[1]);
                }
            }
        }


        for (int i = 0; i < order.size(); i++) {
            row = sheet.createRow((int) i + 1);
            TagMsg tagmsg = (TagMsg) order.get(i);
            // 第五步，创建单元格，并设置值
            row.createCell((short) 0).setCellValue(tagmsg.getSecondindex());
            row.createCell((short) 1).setCellValue(tagmsg.getFirstindex());
            row.createCell((short) 2).setCellValue(tagmsg.getThirdindex());

        }

        // 第六步，将文件存到指定位置
        if (search_flag == true) {
            try {
                FileOutputStream fout = new FileOutputStream("E:/flag.xls");
                wb.write(fout);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            search_flag = false;
        }
    }
}