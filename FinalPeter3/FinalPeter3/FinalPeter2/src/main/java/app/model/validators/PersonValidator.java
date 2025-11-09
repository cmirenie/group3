package app.model.validators;

public final class PersonValidator {
    private PersonValidator() { }

    public static boolean isValidRaw(String name, Integer age, Integer id) {
        return name != null
                && !name.trim().isEmpty()
                && age != null && age >= 0 && age <= 150
                && id != null && id >= 0;
    }
    public static boolean isInvalidRaw(String name, Integer age, Integer id) {
        return !isValidRaw(name, age, id);
    }
}
