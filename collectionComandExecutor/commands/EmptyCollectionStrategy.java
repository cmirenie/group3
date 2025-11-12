package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.util.List;

/**
 * Стратегия заглушка, используется при инициализации CollectionCommandExecutor
 */
public class EmptyCollectionStrategy implements CollectionStrategy {

    @Override
    public void execute(List<Data> input) {

    }
}
