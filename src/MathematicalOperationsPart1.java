class MathematicalOperations {
    public static void main(String[] args) {
        //пример без дерева для сравнения с примером с деревом
        Expression n1 = new Number(32.0);
        Expression m1 = new Number(16.0);
        Expression minus1 = new BinaryOperation(n1, BinaryOperation.MINUS, m1);
        FunctionCall callSqrt1 = new FunctionCall("sqrt", minus1);
        Expression p1 = new Number(2.0);
        BinaryOperation mult1 = new BinaryOperation(p1, BinaryOperation.MUL, callSqrt1);
        FunctionCall callAbs = new FunctionCall("abs", mult1);
        System.out.println("Результат вычисления без дерева: " + callAbs.evaluate(1.0));

        Number n2 = new Number(32.0);
        Number m2 = new Number(16.0);
        BinaryOperation minus2 = new BinaryOperation(n2, BinaryOperation.MINUS, m2);
        FunctionCall callSqrt2 = new FunctionCall("sqrt", minus2);
        Variable p2 = new Variable("var");
        BinaryOperation mult2 = new BinaryOperation(p2, BinaryOperation.MUL, callSqrt2);
        FunctionCall callAbs2 = new FunctionCall("abs", mult2);
        CopySyntaxTree CST = new CopySyntaxTree();
        Expression newExpr = callAbs2.transform(CST);

        System.out.println("Результат вычисления с деревом: " + newExpr.evaluate(2.0));
    }
}

interface Expression {
    double evaluate(double varValue);
    Expression transform(Transformer tr);
}

interface Transformer {
    Expression transformNumber(Number number);
    Expression transformBinaryOperation(BinaryOperation binop);
    Expression transformFunctionCall(FunctionCall fcall);
    Expression transformVariable(Variable var);
}


class Number implements Expression {
    private double value;

    public Number(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    @Override
    public double evaluate(double varValue) {
        return value;
    }

    @Override
    public Expression transform(Transformer tr) {
        return tr.transformNumber(this);
    }
}

class BinaryOperation implements Expression {
    public static final int PLUS = '+';
    public static final int MINUS = '-';
    public static final int DIV = '/';
    public static final int MUL = '*';

    private Expression left;
    private Expression right;
    private int op;

    public BinaryOperation(Expression left, int op, Expression right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public double evaluate(double varValue) {
        double leftVal = left.evaluate(varValue);
        double rightVal = right.evaluate(varValue);

        switch (op) {
            case PLUS:
                return leftVal + rightVal;
            case MINUS:
                return leftVal - rightVal;
            case DIV:
                if (rightVal == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                return leftVal / rightVal;
            case MUL:
                return leftVal * rightVal;
            default:
                throw new IllegalArgumentException("Недопустимая операция: " + (char)op);
        }
    }

    @Override
    public Expression transform(Transformer tr) {
        return tr.transformBinaryOperation(this);
    }

    public Expression left() {
        return left;
    }

    public Expression right() {
        return right;
    }

    public int operation() {
        return op;
    }
}

class FunctionCall implements Expression {
    private String name;
    private Expression arg;

    public FunctionCall(String name, Expression arg) {
        this.name = name;
        this.arg = arg;
    }

    @Override
    public double evaluate(double varValue) {
        double argVal = arg.evaluate(varValue);

        switch (name) {
            case "sqrt":
                if (argVal < 0) {
                    throw new IllegalArgumentException("Нельзя извлечь квадратный корень из отрицательного числа");
                }
                return Math.sqrt(argVal);
            case "abs":
                return Math.abs(argVal);
            default:
                throw new IllegalArgumentException("Неизвестная фукнция: " + name);
        }
    }

    @Override
    public Expression transform(Transformer tr) {
        return tr.transformFunctionCall(this);
    }

    public String name() {
        return name;
    }

    public Expression arg() {
        return arg;
    }
}

class Variable implements Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public double evaluate(double varValue) {
        if (name.equals("var")) {
            return varValue;
        } else {
            throw new IllegalArgumentException("Неизвестная переменная: " + name);
        }
    }

    @Override
    public Expression transform(Transformer tr) {
        return tr.transformVariable(this);
    }
}

class CopySyntaxTree implements Transformer {
    @Override
    public Expression transformNumber(Number number) {
        return new Number(number.value());
    }

    @Override
    public Expression transformBinaryOperation(BinaryOperation binop) {
        return new BinaryOperation(binop.left().transform(this), binop.operation(), binop.right().transform(this));
    }

    @Override
    public Expression transformFunctionCall(FunctionCall fcall) {
        return new FunctionCall(fcall.name(), fcall.arg().transform(this));
    }

    @Override
    public Expression transformVariable(Variable var) {
        return new Variable(var.name());
    }
}
