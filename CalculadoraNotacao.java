import java.util.Stack;
import java.util.Scanner;

public class CalculadoraNotacao {

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static int applyOperator(int a, int b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Divisão por zero.");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Operador inválido: " + operator);
        }
    }

    public static int evaluatePostfix(String postfix) {
        Stack<Integer> operandStack = new Stack<>();

        for (char c : postfix.toCharArray()) {
            if (Character.isDigit(c)) {
                operandStack.push(c - '0');
            } else if (isOperator(c)) {
                if (operandStack.size() < 2) {
                    throw new IllegalArgumentException("Expressão pós-fixa mal formada.");
                }
                int operand2 = operandStack.pop();
                int operand1 = operandStack.pop();
                int result = applyOperator(operand1, operand2, c);
                operandStack.push(result);
            }
        }

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Expressão pós-fixa mal formada.");
        }

        return operandStack.pop();
    }

    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        for (char c : infix.toCharArray()) {
            if (Character.isDigit(c)) {
                postfix.append(c);
            } else if (c == ' ') {
                continue;
            } else if (isOperator(c)) {
                while (!operatorStack.isEmpty() && getPriority(c) <= getPriority(operatorStack.peek())) {
                    postfix.append(" ").append(operatorStack.pop());
                }
                operatorStack.push(c);
            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    postfix.append(" ").append(operatorStack.pop());
                }
                operatorStack.pop();
            }
        }

        while (!operatorStack.isEmpty()) {
            postfix.append(" ").append(operatorStack.pop());
        }

        return postfix.toString();
    }

    public static String infixToPrefix(String infix) {
        StringBuilder prefix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        for (char c : new StringBuilder(infix).reverse().toString().toCharArray()) {
            if (Character.isDigit(c)) {
                prefix.insert(0, c);
            } else if (c == ' ') {
                continue;
            } else if (isOperator(c)) {
                while (!operatorStack.isEmpty() && getPriority(c) < getPriority(operatorStack.peek())) {
                    prefix.insert(0, operatorStack.pop());
                }
                operatorStack.push(c);
            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    prefix.insert(0, operatorStack.pop());
                }
                operatorStack.pop();
            }
        }

        while (!operatorStack.isEmpty()) {
            prefix.insert(0, operatorStack.pop());
        }

        return prefix.toString();
    }

    public static int getPriority(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a expressão: ");
        String expressao = scanner.nextLine();

        System.out.print("Digite o tipo de notação (infixa, pós-fixa ou pré-fixa): ");
        String notacao = scanner.nextLine();

        if (notacao.equalsIgnoreCase("infixa")) {
            String postfixExpression = infixToPostfix(expressao);
            int result = evaluatePostfix(postfixExpression);
            String prefixExpression = infixToPrefix(expressao);
            System.out.println("Expressão Pós-fixa: " + postfixExpression);
            System.out.println("Expressão Pré-fixa: " + prefixExpression);
            System.out.println("Resultado: " + result);
        } else if (notacao.equalsIgnoreCase("pós-fixa")) {
            int result = evaluatePostfix(expressao);
            String infixExpression = postfixToInfix(expressao);
            String prefixExpression = infixToPrefix(infixExpression);
            System.out.println("Expressão Infixa: " + infixExpression);
            System.out.println("Expressão Pré-fixa: " + prefixExpression);
            System.out.println("Resultado: " + result);
        } else if (notacao.equalsIgnoreCase("pré-fixa")) {
            int result = evaluatePostfix(infixToPostfix(infixToPrefix(expressao)));
            String infixExpression = prefixToInfix(expressao);
            String postfixExpression = infixToPostfix(infixExpression);
            System.out.println("Expressão Infixa: " + infixExpression);
            System.out.println("Expressão Pós-fixa: " + postfixExpression);
            System.out.println("Resultado: " + result);
        } else {
            System.out.println("Notação inválida.");
        }

        scanner.close();
    }

    public static String postfixToInfix(String postfix) {
        Stack<String> operandStack = new Stack<>();

        for (char c : postfix.toCharArray()) {
            if (Character.isDigit(c)) {
                operandStack.push(String.valueOf(c));
            } else if (isOperator(c)) {
                if (operandStack.size() < 2) {
                    throw new IllegalArgumentException("Expressão pós-fixa mal formada.");
                }
                String operand2 = operandStack.pop();
                String operand1 = operandStack.pop();
                String result = "(" + operand1 + c + operand2 + ")";
                operandStack.push(result);
            }
        }

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Expressão pós-fixa mal formada.");
        }

        return operandStack.pop();
    }

    public static String prefixToInfix(String prefix) {
        Stack<String> operandStack = new Stack<>();

        for (char c : new StringBuilder(prefix).reverse().toString().toCharArray()) {
            if (Character.isDigit(c)) {
                operandStack.push(String.valueOf(c));
            } else if (isOperator(c)) {
                if (operandStack.size() < 2) {
                    throw new IllegalArgumentException("Expressão pré-fixa mal formada.");
                }
                String operand1 = operandStack.pop();
                String operand2 = operandStack.pop();
                String result = "(" + operand1 + c + operand2 + ")";
                operandStack.push(result);
            }
        }

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Expressão pré-fixa mal formada.");
        }

        return operandStack.pop();
    }
}
