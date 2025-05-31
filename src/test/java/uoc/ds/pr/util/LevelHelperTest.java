package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Test;
import uoc.ds.pr.enums.CollectorLevel;

public class LevelHelperTest {
    @Test
    public void levelHelperTest() {
        Assert.assertEquals(CollectorLevel.BRONZE, LevelHelper.getLevel(35));
        Assert.assertEquals(CollectorLevel.SILVER, LevelHelper.getLevel(200));
        Assert.assertEquals(CollectorLevel.SILVER, LevelHelper.getLevel(499));
        Assert.assertEquals(CollectorLevel.GOLD, LevelHelper.getLevel(500));
        Assert.assertEquals(CollectorLevel.GOLD, LevelHelper.getLevel(999));
        Assert.assertEquals(CollectorLevel.GOLD, LevelHelper.getLevel(1000));
        Assert.assertEquals(CollectorLevel.GOLD, LevelHelper.getLevel(1999));
        Assert.assertEquals(CollectorLevel.PLATINUM, LevelHelper.getLevel(2000));
        Assert.assertEquals(CollectorLevel.PLATINUM, LevelHelper.getLevel(4999));
        Assert.assertEquals(CollectorLevel.DIAMOND, LevelHelper.getLevel(5000));
        Assert.assertEquals(CollectorLevel.DIAMOND, LevelHelper.getLevel(10000));
    }

}
