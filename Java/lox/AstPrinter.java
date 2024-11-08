package lox;

class AstPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    // New method to convert an expression to RPN
    String toRPN(Expr expr) {
        return expr.accept(new RPNVisitor());
    }

    // Visitor class for generating RPN notation
    private class RPNVisitor implements Expr.Visitor<String> {
        @Override
        public String visitBinaryExpr(Expr.Binary expr) {
            // In RPN, the left and right operands come before the operator
            return expr.left.accept(this) + " " + expr.right.accept(this) + " " + expr.operator.lexeme;
        }

        @Override
        public String visitGroupingExpr(Expr.Grouping expr) {
            // For grouping, just recursively visit the inner expression
            return expr.expression.accept(this);
        }

        @Override
        public String visitLiteralExpr(Expr.Literal expr) {
            // Literals are added as-is
            if (expr.value == null) return "nil";
            return expr.value.toString();
        }

        @Override
        public String visitUnaryExpr(Expr.Unary expr) {
            // For unary, visit the operand first, then add the operator
            return expr.right.accept(this) + " " + expr.operator.lexeme;
        }
    }

    // Existing methods for standard parenthesized notation
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    // Main method to test both notations
    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67))
        );

        AstPrinter printer = new AstPrinter();
        System.out.println("Standard Notation: " + printer.print(expression));
        System.out.println("RPN Notation: " + printer.toRPN(expression));
    }
}
