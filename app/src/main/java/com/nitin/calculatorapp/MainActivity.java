package com.nitin.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private final String KEY_FOR_PENDING_OPERATION = "KEY_PENDING_OPERATION";

    private EditText result;
    private EditText newNumber;
    private TextView currentOperation;

    private String pendingOperation = null; // initially no pending operation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        newNumber = findViewById(R.id.newNumber);
        currentOperation = findViewById(R.id.operator);

        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button buttonDot = findViewById(R.id.buttonDot);

        // When ever a button From 0 to 9 is pressed, append that button value to newNumber.
        View.OnClickListener onClickListenerForButton0To9AndDot = new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // v is the reference of the view which has been clicked.
                Button clickedButton = (Button) v;  // finding which button is tapped
                newNumber.append(clickedButton.getText().toString());
            }
        };

        button0.setOnClickListener(onClickListenerForButton0To9AndDot);
        button1.setOnClickListener(onClickListenerForButton0To9AndDot);
        button2.setOnClickListener(onClickListenerForButton0To9AndDot);
        button3.setOnClickListener(onClickListenerForButton0To9AndDot);
        button4.setOnClickListener(onClickListenerForButton0To9AndDot);
        button5.setOnClickListener(onClickListenerForButton0To9AndDot);
        button6.setOnClickListener(onClickListenerForButton0To9AndDot);
        button7.setOnClickListener(onClickListenerForButton0To9AndDot);
        button8.setOnClickListener(onClickListenerForButton0To9AndDot);
        button9.setOnClickListener(onClickListenerForButton0To9AndDot);
        buttonDot.setOnClickListener(onClickListenerForButton0To9AndDot);

        Button buttonPlus = findViewById(R.id.buttonPlus);
        Button buttonMinus = findViewById(R.id.buttonMinus);
        Button buttonMultiply = findViewById(R.id.buttonMultiply);
        Button buttonDivide = findViewById(R.id.buttonDivide);
        Button buttonEqual = findViewById(R.id.buttonEqual);

        View.OnClickListener onClickListenerForOperators = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button operator = (Button) v;
                String answer;
                checkForDouble();// if not a valid double number, clear newNumber
                String newNumberString = newNumber.getText().toString();
                String resultString = result.getText().toString();

                if (resultString.isEmpty())
                    answer = newNumberString;    // this happens only initially
                    // result won't change when previously '=' is pressed or we previously calculated our answer and now newNumber is empty
                else if (pendingOperation.equals("=") || newNumberString.isEmpty())
                    answer = result.getText().toString();
                    // this happens when result is not empty and our newNumber is not empty and pendingOperation not '='
                else answer = calculate(resultString, newNumberString, pendingOperation);
                // update our pending operation
                pendingOperation = operator.getText().toString();
                result.setText(answer);
                // reset our newNumber
                newNumber.setText("");
                // also update our currentOperation if it is not null or '='
                if (pendingOperation != null) {
                    if (pendingOperation.equals("=")) currentOperation.setText("");
                    else currentOperation.setText(pendingOperation);
                }
            }
        };

        buttonMultiply.setOnClickListener(onClickListenerForOperators);
        buttonMinus.setOnClickListener(onClickListenerForOperators);
        buttonPlus.setOnClickListener(onClickListenerForOperators);
        buttonDivide.setOnClickListener(onClickListenerForOperators);
        buttonEqual.setOnClickListener(onClickListenerForOperators);

        Button buttonDelete = findViewById(R.id.buttonDelete);
        Button buttonClear = findViewById(R.id.buttonClear);

        // it will delete only the last character from newNumber
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNumberString = newNumber.getText().toString();
                if (!newNumberString.isEmpty())
                    newNumberString = newNumberString.substring(0, newNumberString.length() - 1);

                newNumber.setText(newNumberString);
            }
        });
        // it will clear all contents just like newly launched app
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingOperation = null;
                newNumber.setText("");
                result.setText("");
                currentOperation.setText("");
            }
        });
        // it will toggle the sign of newNumber
        Button buttonNegative = findViewById(R.id.buttonNegative);
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNumberString = newNumber.getText().toString();
                if (newNumberString.isEmpty() || newNumberString.charAt(0) != '-')
                    newNumberString = '-' + newNumberString;
                else newNumberString = newNumberString.substring(1);
                newNumber.setText(newNumberString);
            }
        });

    }

    private String calculate(String operand1, String operand2, String operator) {
        double val1 = Double.parseDouble(operand1);
        double val2 = Double.parseDouble(operand2);
        double result = 0d;
        switch (operator) {
            case "+":
                result = val1 + val2;
                break;
            case "-":
                result = val1 - val2;
                break;
            case "*":
                result = val1 * val2;
                break;
            case "/":
                if (val2 != 0) result = val1 / val2;
                break;
        }
        return String.valueOf(result);
    }

    private void checkForDouble() {
        try {
            Double.parseDouble(newNumber.getText().toString());
        } catch (NumberFormatException e) {
            newNumber.setText("");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(KEY_FOR_PENDING_OPERATION);
        // although our pendingState is successfully reloaded, but our currentOperation TextView should be updated as well
        if (pendingOperation != null) {
            if (pendingOperation.equals("=")) currentOperation.setText("");
            else currentOperation.setText(pendingOperation);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_FOR_PENDING_OPERATION, pendingOperation);
        super.onSaveInstanceState(outState);
    }
}