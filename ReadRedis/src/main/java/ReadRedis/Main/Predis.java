package ReadRedis.Main;


import ReadRedis.CacheDispatch;
import ReadRedis.FileReadToResult;
import ReadRedis.RedisClient;
import ReadRedis.UtilOper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by caodongj on 2017/10/19.
 */

public class Predis {
    private static final Logger log = LoggerFactory.getLogger(FileReadToResult.class);

    public static RedisClient rc = new RedisClient();


    //redis 初始化
    public static Jedis jedis_initial() {

        String path = Thread.currentThread().getContextClassLoader().getResource("redis.conf").getPath();
        Integer redis_port = UtilOper.getIntValue(path, "redis_port", 0);
        String redis_ip = UtilOper.getStringValue(path, "redis_ip", "0.0.0.0");
        Integer time_wait = UtilOper.getIntValue(path, "time_wait", 0);
        Integer time_out = UtilOper.getIntValue(path, "time_out", 0);
        Integer max_Active = UtilOper.getIntValue(path, "max_Active", 0);
        Integer max_Idle = UtilOper.getIntValue(path, "max_Idle", 0);
        Boolean testOnBorrow = UtilOper.getBooleanValue(path, "testOnBorrow", false);
        String auth = UtilOper.getStringValue(path, "auth", "0000");


        rc.init(redis_ip, redis_port, time_wait, time_out, max_Active, max_Idle, testOnBorrow, auth);
        Jedis jedis = rc.getJedis();
        if (jedis == null) log.error("jedis get failed!!!");
        return jedis;
    }

    public static List<TagMsg> imel_cmd_parse(String cmd, String parameter, String imel) {

        Jedis jedis = jedis_initial();

        if (cmd.equals("redis") && imel.length() == 32 && parameter.equals("-i")) {

            //Map<Integer, List<String[]>> userTagMap = new HashMap<Integer, List<String[]>>();

            BitSet bitSetTag = null;
            try {
                bitSetTag = BitSet.valueOf(jedis.get(imel.getBytes()));
            } catch (Exception e) {
                log.error("afterstr cannot get the value, redis does not have the key-value map!!!");
                e.printStackTrace();
            }
            if (null == bitSetTag) {
                return null;
            }
            List<TagMsg> msgList = new ArrayList<TagMsg>();
            for (int sourceIndex = 0; sourceIndex < 480; sourceIndex++) {
                if (bitSetTag.get(sourceIndex)) {
                    int mod = sourceIndex % 8;
                    int secondindex = (sourceIndex - mod - 1) + (8 - mod);


                    String[] tagcode = CacheDispatch.getTagsCodeMap().get(secondindex);

                    String[] codeType = tagcode;

                    if (((codeType.length == 2) && codeType[0].length() > 8)) {
                        String firstindex = codeType[0].substring(0, 12);
                        String thirdindex = codeType[1];
                        //System.out.println("    the secondindex: " + secondindex + " the firstindex: " + firstindex + " the thirdindex: " + thirdindex);
                        msgList.add(new TagMsg(secondindex, firstindex, thirdindex));

                    } else {
                        continue;
                    }

                }
            }

            return msgList;
        } else {
            System.out.println("既不是内部或外部命令，也不是可运行的程序或批处理文件");

        }

        rc.release(jedis);
        return null;

    }

    public static List<TagMsg> phone_cmd_parse(String cmd, String parameter, String phone) {

        Jedis jedis = jedis_initial();

        if (cmd.equals("redis") && phone.length() == 11 && parameter.equals("-p")) {

            //Map<Integer, List<String[]>> userTagMap = new HashMap<Integer, List<String[]>>();

            BitSet bitSetTag = null;
            try {
                bitSetTag = BitSet.valueOf(jedis.get(phone.getBytes()));
            } catch (Exception e) {
                log.error("afterstr cannot get the value, redis does not have the key-value map!!!");
                e.printStackTrace();
            }
            List<TagMsg> msgList = new ArrayList<TagMsg>();
            for (int sourceIndex = 0; sourceIndex < 480; sourceIndex++) {
                if (bitSetTag.get(sourceIndex)) {
                    int mod = sourceIndex % 8;
                    int secondindex = (sourceIndex - mod - 1) + (8 - mod);


                    String[] tagcode = CacheDispatch.getTagsCodeMap().get(secondindex);

                    String[] codeType = tagcode;

                    if (((codeType.length == 2) && codeType[0].length() > 8)) {
                        String firstindex = codeType[0].substring(0, 12);
                        String thirdindex = codeType[1];
                        //System.out.println("    the secondindex: " + secondindex + " the firstindex: " + firstindex + " the thirdindex: " + thirdindex);
                        msgList.add(new TagMsg(secondindex, firstindex, thirdindex));
                    } else {
                        continue;
                    }

                }
            }
            return msgList;
        } else {
            System.out.println("既不是内部或外部命令，也不是可运行的程序或批处理文件");
        }
        rc.release(jedis);
        return null;
    }

    public static void help_cmd_parse(String cmd, String parameter) {

        if (cmd.equals("redis") && parameter.equals("-h")) {
            System.out.println("this is personal redis search tools!");
            System.out.println("    -i    imel          通过32位的imel来查询redis里的value值      e.g. redis -i imel");
            System.out.println("    -p    phone         通过手机号码来查询redis里的value值         e.g. redis -p phone");
            System.out.println("    -v    version       查看redis的版本                          e.g. redis -v");
            System.out.println("    -h    help          查看redis帮助                            e.g. redis -h");
        } else {
            System.out.println("既不是内部或外部命令，也不是可运行的程序或批处理文件");
        }

    }

    public static void version_cmd_parse(String cmd, String parameter) {
        if (cmd.equals("redis") && parameter.equals("-v")) {
            System.out.println("redis version 1.0");
        } else {
            System.out.println("既不是内部或外部命令，也不是可运行的程序或批处理文件");
        }
    }


    public static void main(String[] args) {

        System.out.println("please input your cmd:");
        Scanner scanner = new Scanner(System.in);
        String cmd_order = scanner.nextLine();
        String[] cmdlist = cmd_order.split(" ");
        if (cmdlist.length == 3) {
            if (cmdlist[1].equals("-i")) {
                imel_cmd_parse(cmdlist[0], cmdlist[1], cmdlist[2]);
            } else if (cmdlist[1].equals("-p")) {
                phone_cmd_parse(cmdlist[0], cmdlist[1], cmdlist[2]);
            }

        } else if (cmdlist.length == 2) {
            if (cmdlist[1].equals("-h")) {
                help_cmd_parse(cmdlist[0], cmdlist[1]);
            }
            if (cmdlist[1].equals("-v")) {
                version_cmd_parse(cmdlist[0], cmdlist[1]);
            }

        } else {
            System.out.println("redis does not support this order");
            System.out.println("this is personal redis search tools!");
            System.out.println("    -i    imel          通过32位的imel来查询redis里的value值      e.g. redis -i imel");
            System.out.println("    -p    phone         通过手机号码来查询redis里的value值         e.g. redis -p phone");
            System.out.println("    -v    version       查看redis的版本                          e.g. redis -v");
        }


    }

}