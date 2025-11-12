package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Доп.задание 1:
 * сортируем по числовому полю так, чтобы элементы с чётным значением поля
 * были упорядочены по возрастанию, а элементы с нечётным значением
 * остались на исходных позициях.
 */
public class SortBySpecialStrategy implements CollectionStrategy {

    @Override
    public void execute(List<Data> input) {
        if (input == null || input.size() < 2) return;

        // Собираем позиции чётных и их копии
        List<Integer> evenIdx = new ArrayList<>();
        List<Data> evens = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            Data d = input.get(i);
            if (d.getNumber() % 2 == 0) {
                evenIdx.add(i);
                evens.add(d);
            }
        }

        // Сортируем только чётные по number (свой пузырёк — без готовых сортировок)
        bubbleSortByNumber(evens);

        // Возвращаем отсортированные чётные обратно на их исходные индексы
        for (int i = 0; i < evenIdx.size(); i++) {
            input.set(evenIdx.get(i), evens.get(i));
        }
    }

    /** Пузырьковая сортировка по числовому полю number. */
    private static void bubbleSortByNumber(List<Data> list) {
        Comparator<Data> cmp = Comparator.comparingInt(Data::getNumber);
        int n = list.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (cmp.compare(list.get(i - 1), list.get(i)) > 0) {
                    Data tmp = list.get(i - 1);
                    list.set(i - 1, list.get(i));
                    list.set(i, tmp);
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
    }
}
