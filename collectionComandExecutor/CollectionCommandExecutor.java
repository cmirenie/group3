package collectionComandExecutor;

import data.Data;
import java.util.List;

public class CollectionCommandExecutor {
    private CollectionStrategy strategy;

    public CollectionCommandExecutor(CollectionStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(CollectionStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeCommand(List<Data> input) {
        strategy.execute(input);
    }
}


