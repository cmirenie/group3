package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;

import data.Data;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Дополнительное задание 1: дополнительно к основным сортировкам реализовать эти же алгоритмы сортировки таким образом,
 * что объекты классов будут сортироваться по какому-либо числовому полю: объекты с четными значениями этого поля должны
 * быть отсортированы в натуральном порядке, а с нечетными – оставаться на исходных позициях.
 */
public class SortBySpecialStrategy implements CollectionStrategy {
    public static void BubbleSortBySpecial(List<Data> list) {
        Data.bubbleSort(list, (d1, d2) -> {
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

    @Override
    public void execute(List<Data> input) {
        List<Integer> evenIndices = new ArrayList<>();
        List<Data> evenValues = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).getNumber() % 2 == 0) {
                evenIndices.add(i);
                evenValues.add(input.get(i));
            }
        }

        evenValues.sort(Comparator.comparingInt(Data::getNumber));

        for (int i = 0; i < evenIndices.size(); i++) {
            input.set(evenIndices.get(i), evenValues.get(i));
        }
    }
}
