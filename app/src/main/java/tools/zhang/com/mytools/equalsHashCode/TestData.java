package tools.zhang.com.mytools.equalsHashCode;

/**
 * Created by zhangdecheng on 2017/2/20.
 */
public class TestData {
    private String mId;
    private int mAge;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestData task = (TestData) o;
        return MyObjects.equal(mId, task.mId) &&
                MyObjects.equal(mAge, task.mAge);
    }

    @Override
    public int hashCode() {
        return MyObjects.hashCode(mId, mAge);
    }
}
