package ReadRedis.Main;

/**
 * Created by caodongj on 2017/10/20.
 */
public class TagMsg {

    public TagMsg() {

    }

    public TagMsg(int secondindex, String firstindex, String thirdindex) {
        this.secondindex = secondindex;
        this.firstindex = firstindex;
        this.thirdindex = thirdindex;
    }

    private int secondindex;
    private String firstindex;
    private String thirdindex;

    public int getSecondindex() {
        return secondindex;
    }

    public void setSecondindex(int secondindex) {
        this.secondindex = secondindex;
    }

    public String getFirstindex() {
        return firstindex;
    }

    public void setFirstindex(String firstindex) {
        this.firstindex = firstindex;
    }

    public String getThirdindex() {
        return thirdindex;
    }

    public void setThirdindex(String thirdindex) {
        this.thirdindex = thirdindex;
    }
}
