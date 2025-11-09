package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.util.Comparator;
import java.util.List;
/**
 * Стратегия сортировки по числовому полю
 */
public class SortByNumberStrategy implements CollectionStrategy {
    public static void bubbleSortByNumber(List<Data> list) {
        Data.bubbleSort(list, Comparator.comparing(Data::getNumber));
    }
    @Override
    public void execute(List<Data> input) {

    }
}
