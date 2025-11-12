package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import collectionComandExecutor.CustomList;
import data.Data;

import java.util.List;

/**
 * Доп. задание 3: перенос в кастомную коллекцию с использованием стримов.
 */
public class TransferToCustomCollectionStrategy implements CollectionStrategy {

    @Override
    public void execute(List<Data> input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Входной список пуст, кастомная коллекция не будет заполнена.");
            return;
        }

        // Переносим элементы List<Data> -> CustomList<Data> через Stream API
        CustomList<Data> customDataList = CustomList.fromStream(input.stream());

        // Небольшая демонстрация работы методов кастомного списка:
        System.out.println("CustomList размер: " + customDataList.size());

        Data first = customDataList.get(0);
        System.out.println("Первый элемент: " + first);

        boolean hasFirst = customDataList.contains(first);
        System.out.println("Содержит первый элемент? " + hasFirst);

        int idx = customDataList.indexOf(first);
        System.out.println("Индекс первого элемента: " + idx);

        // Пример преобразования в массив (если где-то нужно)
        Object[] arr = customDataList.toArray();
        System.out.println("В массиве элементов: " + arr.length);

        // Ничего не возвращаем — по контракту стратегии просто выполняем действие
    }
}
