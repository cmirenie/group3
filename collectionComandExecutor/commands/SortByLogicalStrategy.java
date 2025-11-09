package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.util.Comparator;
import java.util.List;
/**
 * Стратегия сортировки по логическому полю
 */
public class SortByLogicalStrategy implements CollectionStrategy {
    public static void bubbleSortByLogical(List<Data> list) {
        Data.bubbleSort(list, Comparator.comparing(Data::isLogical));
    }
    @Override
    public void execute(List<Data> input) {

    }
}
