package searchCommandExecutor;

import data.Data;

import java.util.List;

public interface SearchStrategy {
    List<Data> execute(List<Data> input);
}
