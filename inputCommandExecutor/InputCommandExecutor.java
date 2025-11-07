package inputCommandExecutor;

import data.Data;

import java.util.List;

public class InputCommandExecutor {
    private InputStrategy strategy;

    public InputCommandExecutor(InputStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(InputStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Data> executeCommand() {
        return strategy.execute();
    }
}
