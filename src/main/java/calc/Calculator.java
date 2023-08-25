package calc;

/**
 * This class represents a basic calculator that can perform addition, subtraction,
 * multiplication, and division operations.
 */
public class Calculator {

    /**
     * Adds two numbers together.
     *
     * @param num1 The first number.
     * @param num2 The second number.
     * @return The sum of the two numbers.
     */
    public double add(double num1, double num2) {
        return num1 + num2;
    }

    /**
     * Subtracts one number from another.
     *
     * @param num1 The number to subtract from.
     * @param num2 The number to subtract.
     * @return The result of the subtraction.
     */
    public double subtract(double num1, double num2) {
        return num1 - num2;
    }

    /**
     * Multiplies two numbers together.
     *
     * @param num1 The first number.
     * @param num2 The second number.
     * @return The product of the multiplication.
     */
    public double multiply(double num1, double num2) {
        return num1 * num2;
    }

    /**
     * Divides one number by another.
     *
     * @param dividend The number to be divided (dividend).
     * @param divisor The number by which to divide (divisor).
     * @return The result of the division.
     * @throws ArithmeticException if the divisor is zero.
     */
    public double divide(double dividend, double divisor) throws ArithmeticException {
        if (divisor == 0) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        return dividend / divisor;
    }
}
