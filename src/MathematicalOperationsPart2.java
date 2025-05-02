public class MathematicalOperationsPart2 {
    public static void main(String[] args) {
        Number n32 = new Number(32.0);
        Number n16 = new Number(16.0);
        BinaryOperation minus = new BinaryOperation(n32, BinaryOperation.MINUS, n16);
        FunctionCall callSqrt = new FunctionCall("sqrt", minus);
        Variable var = new Variable("var");

        var.setValue(2.0);

        BinaryOperation mult = new BinaryOperation(var, BinaryOperation.MUL, callSqrt);
        FunctionCall callAbs = new FunctionCall("abs", mult);

        FoldConstants FC = new FoldConstants();
        Expression newExpr = callAbs.transform(FC);
        System.out.println(newExpr.evaluate());
    }
}

class Expression {
    public double evaluate() { throw new UnsupportedOperationException(); }
    public Expression transform(Transformer tr) { throw new UnsupportedOperationException(); }
}

abstract class Transformer {
    public abstract Expression transformNumber(Number number);
    public abstract Expression transformBinaryOperation(BinaryOperation binop);
    public abstract Expression transformFunctionCall(FunctionCall fcall);
    public abstract Expression transformVariable(Variable var);
}

class Number extends Expression {
    private double value;

    public Number(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public double evaluate() {
        return value;
    }

    public Expression transform(Transformer tr) {
        return tr.transformNumber(this);
    }
}

class BinaryOperation extends Expression {
    public static final int PLUS = '+';
    public static final int MINUS = '-';
    public static final int DIV = '/';
    public static final int MUL = '*';

    private final Expression left;
    private final Expression right;
    private final int operation;

    public BinaryOperation(Expression left, int operation, Expression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public double evaluate() {
        switch (operation) {
            case PLUS: return left.evaluate() + right.evaluate();
            case MINUS: return left.evaluate() - right.evaluate();
            case MUL: return left.evaluate() * right.evaluate();
            case DIV: return left.evaluate() / right.evaluate();
            default: throw new UnsupportedOperationException("Неизвестный оператор");
        }
    }

    public Expression transform(Transformer tr) {
        return tr.transformBinaryOperation(this);
    }

    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }
    public int getOperation() { return operation; }
}

class FunctionCall extends Expression {
    private final String name;
    private final Expression arg;

    public FunctionCall(String name, Expression arg) {
        this.name = name;
        this.arg = arg;
    }

    public double evaluate() {
        switch (name) {
            case "sqrt":
                return Math.sqrt(arg.evaluate());
            case "abs":
                return Math.abs(arg.evaluate());
            default:
                throw new UnsupportedOperationException("Неизвестная функция " + name);
        }
    }

    public Expression transform(Transformer tr) {
        return tr.transformFunctionCall(this);
    }

    public String getName() {
        return name;
    }

    public Expression getArg() {
        return arg;
    }
}


class Variable extends Expression {
    private final String name;
    private double value;

    public Variable(String name) {
        this.name = name;
        this.value = 0;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double evaluate() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Expression transform(Transformer tr) {
        return tr.transformVariable(this);
    }
}


class FoldConstants extends Transformer {
    public Expression transformNumber(Number number) {
        return number;
    }

    public Expression transformBinaryOperation(BinaryOperation binop) {
        Expression left = binop.getLeft().transform(this);
        Expression right = binop.getRight().transform(this);

        if (left instanceof Number && right instanceof Number) {
            double result = 0;
            switch (binop.getOperation()) {
                case BinaryOperation.PLUS:
                    result = ((Number) left).getValue() + ((Number) right).getValue();
                    break;
                case BinaryOperation.MINUS:
                    result = ((Number) left).getValue() - ((Number) right).getValue();
                    break;
                case BinaryOperation.MUL:
                    result = ((Number) left).getValue() * ((Number) right).getValue();
                    break;

                case BinaryOperation.DIV:
                    result = ((Number) left).getValue() / ((Number) right).getValue();
                    break;
            }
            return new Number(result);
        }
        return new BinaryOperation(left, binop.getOperation(), right);
    }

    public Expression transformFunctionCall(FunctionCall fcall) {
        Expression arg = fcall.getArg().transform(this);
        return new FunctionCall(fcall.getName(), arg);
    }

    public Expression transformVariable(Variable var) {
        return var;
    }
}

