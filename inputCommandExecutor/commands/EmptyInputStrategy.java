package inputCommandExecutor.commands;

import data.Data;
import inputCommandExecutor.InputStrategy;

import java.util.List;

/**
 *  * Стратегия заглушка, используется при инициализации InputCommandExecutor
 */
public class EmptyInputStrategy implements InputStrategy {

    @Override
    public List<Data> execute() {
        return List.of();
    }
}