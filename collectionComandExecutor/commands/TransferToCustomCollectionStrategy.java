package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import collectionComandExecutor.CustomList;
import data.Data;
import java.util.List;

/**
 * Дополнительное задание 3: заполнение коллекций должно осуществляться посредством стримов.
 * 3* Коллекции для заполнения должны быть кастомными.
 */
public class TransferToCustomCollectionStrategy implements CollectionStrategy {
    @Override
    public void execute(List<Data> input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Входной список пуст, кастомная коллекция не будет заполнена.");
            return;
        }
        // Используем стримы для переноса данных в CustomList.
        CustomList<Data> customDataList = CustomList.fromStream(input.stream());

        // Демонстрация использования методов кастомного списка:

        System.out.println("--- Заполнение CustomList завершено ---");
        System.out.println("Размер кастомного списка: " + customDataList.size());
        System.out.println("Первый элемент: " + customDataList.get(0));

        // Пример использования метода contains из CustomList с объектом Data
        Data firstElement = customDataList.get(0);
        System.out.println("Содержит ли список первый элемент? " + customDataList.contains(firstElement));

        // Пример вывода всего списка
         System.out.println("Содержимое списка:\n" + customDataList+"\n");
    }
}
