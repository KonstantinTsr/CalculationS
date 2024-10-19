package com.example.calculation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private StringBuilder fullExpression = new StringBuilder();
    private boolean isEqualPressed = false;
    private Double memoryValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btn0).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn1).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn2).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn3).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn4).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn5).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn6).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn7).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn8).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn9).setOnClickListener(this::onDigitClick);

        findViewById(R.id.btnAdd).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btnSubtract).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btnMultiply).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btnDivide).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btnEqual).setOnClickListener(this::onEqualClick);
        findViewById(R.id.btnClear).setOnClickListener(this::onClearClick);

    }

    private void onDigitClick(View view) {
        if (isEqualPressed) {
            fullExpression.setLength(0);
            isEqualPressed = false;
        }

        Button button = (Button) view;
        fullExpression.append(button.getText().toString());
        tvResult.setText(fullExpression.toString());
    }

    private void onOperatorClick(View view) {
        if (isEqualPressed) {
            isEqualPressed = false;
        }

        Button button = (Button) view;
        String operator = button.getText().toString();
        fullExpression.append(" ").append(operator).append(" ");
        tvResult.setText(fullExpression.toString());
    }

    private void onEqualClick(View view) {
        try {
            double result = evaluateExpression(fullExpression.toString());
            tvResult.setText(String.valueOf(result));
            fullExpression.setLength(0);
            fullExpression.append(result);
            isEqualPressed = true;

        } catch (Exception e) {
            tvResult.setText("Error");
        }
    }

    private void onClearClick(View view) {
        fullExpression.setLength(0);
        tvResult.setText("0");
        isEqualPressed = false;
    }

    private void onMemoryClick(View view) {
        if (memoryValue == null) {
            if (!fullExpression.toString().isEmpty()) {
                memoryValue = Double.parseDouble(tvResult.getText().toString());
                tvResult.setText("M saved");
            }
        } else {
            fullExpression.append(memoryValue);
            tvResult.setText(fullExpression.toString());
        }
    }

    private double evaluateExpression(String expression) throws Exception {
        String[] tokens = expression.split(" ");
        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                numbers.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    String op = operators.pop();
                    numbers.push(applyOperator(a, b, op));
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            String op = operators.pop();
            numbers.push(applyOperator(a, b, op));
        }

        return numbers.pop();
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return -1;
        }
    }

    private double applyOperator(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            default:
                return 0;
        }
    }
}
