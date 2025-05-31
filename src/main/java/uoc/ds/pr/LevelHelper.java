package uoc.ds.pr;

import uoc.ds.pr.enums.CollectorLevel;

public class LevelHelper {

  public static CollectorLevel getLevel(int points) {
    if (points < 200) {
      return CollectorLevel.BRONZE;
    } else if (points < 500) {
      return CollectorLevel.SILVER;
    } else if (points < 2000) {
      return CollectorLevel.GOLD;
    } else if (points < 5000) {
      return CollectorLevel.PLATINUM;
    } else {
      return CollectorLevel.DIAMOND;
    }
  }
}
