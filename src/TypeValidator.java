public interface TypeValidator {
    boolean isValidType(String data);

    TypeValidator StringValidator = data -> !data.isEmpty();

    TypeValidator IntegerValidator = data -> {
        try {
            Integer.parseInt(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };

    TypeValidator DoubleValidator = data -> {
        try {
            Double.parseDouble(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };
}
