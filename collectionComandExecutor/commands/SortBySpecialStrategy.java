package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;

import data.Data;
import java.util.List;

/**
 * Дополнительное задание 1: дополнительно к основным сортировкам реализовать эти же алгоритмы сортировки таким образом,
 * что объекты классов будут сортироваться по какому-либо числовому полю: объекты с четными значениями этого поля должны
 * быть отсортированы в натуральном порядке, а с нечетными – оставаться на исходных позициях.
 */
public class SortBySpecialStrategy implements CollectionStrategy {

    @Override
    public void execute(List<Data> input) {
        Data.bubbleSort(input, (d1, d2) -> {
            boolean d1Even = d1.getNumber() % 2 == 0;
            boolean d2Even = d2.getNumber() % 2 == 0;

            if (d1Even && d2Even) {
                return Integer.compare(d1.getNumber(), d2.getNumber());
            } else if (!d1Even && !d2Even) {
                return 0;
            } else if (d1Even) {
                return -1;
            } else {
                return 1;
            }
        });
    }
}