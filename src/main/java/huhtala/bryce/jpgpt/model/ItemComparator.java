package huhtala.bryce.jpgpt.model;

import java.util.Comparator;
import java.util.Random;

public class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        Random random = new Random();
        Double score1 = item1.getSelectionValue();
        Double score2 = item2.getSelectionValue();
        int sum1 = item1.getCorrect() - item1.getIncorrect();
        int sum2 = item2.getCorrect() - item2.getIncorrect();
        if (sum1 < 0) {
            score1 = score1 / sum1;
        } else if (sum1 > 0) {
            score1 = score1 * Math.abs(sum1);
        }
        if (sum2 < 0) {
            score2 = score2 / sum2;
        } else if (sum2 > 0) {
            score2 = score2 * Math.abs(sum2);
        }
        return Double.compare(score1, score2);
    }
}