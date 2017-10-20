package ReadRedis;


import ReadRedis.Main.Predis;
import redis.clients.jedis.Jedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.List;

/**
 * Created by caodongj on 2017/10/13.
 */
public class FileReadToResult
{
    private static final Logger log = LoggerFactory.getLogger(FileReadToResult.class);



    public static void main(String[] args) throws Exception
    {
        Predis predis=new Predis();
        Jedis jedis=predis.jedis_initial();

        ReadKey readKey = new ReadKey();
        List<String> list = readKey.readFileByLines("E:/study/1.txt");

        Map<Integer, List<String[]>> userTagMap = new HashMap<Integer, List<String[]>>();
        String afterstr = null;
        for (String str : list) {
            if (str.length() == 11) {
                log.debug("This key belongs to phone number!!!");
                afterstr = str;
            } else if (str.length() == 32) {
                log.debug("This is an imei after MD5!!!");
                afterstr = str;
            }


            BitSet bitSetTag = null;
            try {
                bitSetTag = BitSet.valueOf(jedis.get(afterstr.getBytes()));
            } catch (Exception e) {
                log.error("afterstr cannot get the value, redis does not have the key-value map!!!");
                e.printStackTrace();
            }


            for (int sourceIndex = 0; sourceIndex < 480; sourceIndex++) {
                if (bitSetTag.get(sourceIndex)) {
                    int mod = sourceIndex % 8;
                    int secondindex = (sourceIndex - mod - 1) + (8 - mod);


                    String[] tagcode = CacheDispatch.getTagsCodeMap().get(secondindex);

                    String[] codeType = tagcode;

                    if (((codeType.length == 2) && codeType[0].length() > 8)) {
                        String firstindex = codeType[0].substring(0, 8);
                        String thirdindex = codeType[1];
                        System.out.println("    the secondindex: " + secondindex + " the firstindex: " + firstindex + " the thirdindex: " + thirdindex);
                    } else {
                        continue;
                    }
                    List<String[]> codeList = userTagMap.get(secondindex);

                    if (null != codeList) {
                        codeList.add(tagcode);
                        userTagMap.put(secondindex, codeList);
                    }
                }
            }
        }
        //rc.release(jedis);

    }

}

