package collectionComandExecutor;

import data.Data;
import java.util.List;

public interface CollectionStrategy {
    void execute(List<Data> input);
}
