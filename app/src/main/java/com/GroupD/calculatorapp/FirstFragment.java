package com.GroupD.calculatorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.GroupD.calculatorapp.databinding.FragmentFirstBinding;

import java.text.DecimalFormat;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private String formula = "";
    private String currentNumber = "";
    private double result = 0;
    private String lastOperator = "";
    private boolean isNewOp = true;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##########");

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Number buttons
        binding.button0.setOnClickListener(v -> appendNumber("0"));
        binding.button1.setOnClickListener(v -> appendNumber("1"));
        binding.button2.setOnClickListener(v -> appendNumber("2"));
        binding.button3.setOnClickListener(v -> appendNumber("3"));
        binding.button4.setOnClickListener(v -> appendNumber("4"));
        binding.button5.setOnClickListener(v -> appendNumber("5"));
        binding.button6.setOnClickListener(v -> appendNumber("6"));
        binding.button7.setOnClickListener(v -> appendNumber("7"));
        binding.button8.setOnClickListener(v -> appendNumber("8"));
        binding.button9.setOnClickListener(v -> appendNumber("9"));
        binding.buttonDot.setOnClickListener(v -> appendNumber("."));

        // Operator buttons
        binding.buttonPlus.setOnClickListener(v -> setOperator("+"));
        binding.buttonMinus.setOnClickListener(v -> setOperator("−"));
        binding.buttonMultiply.setOnClickListener(v -> setOperator("×"));
        binding.buttonDivide.setOnClickListener(v -> setOperator("÷"));
        binding.buttonPercent.setOnClickListener(v -> applyPercent());

        // Special buttons
        binding.buttonClear.setOnClickListener(v -> clear());
        binding.buttonBackspace.setOnClickListener(v -> backspace());
        binding.buttonEquals.setOnClickListener(v -> showFinalResult());
    }

    private void appendNumber(String number) {
        if (isNewOp && !number.equals(".")) {
            currentNumber = number;
            isNewOp = false;
        } else if (number.equals(".") && currentNumber.contains(".")) {
            return;
        } else {
            currentNumber += number;
        }
        
        updateFormulaDisplay();
        calculateLive();
    }

    private void setOperator(String op) {
        if (!currentNumber.isEmpty() || !formula.isEmpty()) {
            if (currentNumber.isEmpty() && !formula.isEmpty()) {
                // Change the last operator if no number was entered
                formula = formula.substring(0, formula.length() - 3) + " " + op + " ";
            } else {
                formula += currentNumber + " " + op + " ";
                currentNumber = "";
            }
            lastOperator = op;
            isNewOp = true;
            updateFormulaDisplay();
        }
    }

    private void applyPercent() {
        if (!currentNumber.isEmpty()) {
            try {
                double value = Double.parseDouble(currentNumber) / 100;
                currentNumber = decimalFormat.format(value);
                updateFormulaDisplay();
                calculateLive();
            } catch (NumberFormatException ignored) {}
        }
    }

    private void backspace() {
        if (!currentNumber.isEmpty()) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
        } else if (formula.length() > 3) {
            // Remove operator and trailing space
            formula = formula.substring(0, formula.length() - 3);
            // Try to pull back the last number from formula? 
            // Simple implementation: just clear and let user re-type
        }
        updateFormulaDisplay();
        calculateLive();
    }

    private void clear() {
        formula = "";
        currentNumber = "0";
        result = 0;
        lastOperator = "";
        isNewOp = true;
        binding.textViewFormula.setText("0");
        binding.textViewResult.setText("");
    }

    private void updateFormulaDisplay() {
        String fullDisplay = formula + currentNumber;
        if (fullDisplay.isEmpty()) fullDisplay = "0";
        binding.textViewFormula.setText(fullDisplay);
    }

    private void calculateLive() {
        if (formula.isEmpty()) {
            binding.textViewResult.setText("");
            return;
        }

        try {
            // Very simple expression evaluator for live result
            String expression = formula + currentNumber;
            double liveResult = evaluateSimpleExpression(expression);
            binding.textViewResult.setText(decimalFormat.format(liveResult));
        } catch (Exception e) {
            binding.textViewResult.setText("");
        }
    }

    private void showFinalResult() {
        if (!formula.isEmpty() || !currentNumber.isEmpty()) {
            try {
                String expression = formula + currentNumber;
                double finalResult = evaluateSimpleExpression(expression);
                currentNumber = decimalFormat.format(finalResult);
                formula = "";
                isNewOp = true;
                binding.textViewFormula.setText(currentNumber);
                binding.textViewResult.setText("");
            } catch (Exception e) {
                binding.textViewFormula.setText("Error");
                binding.textViewResult.setText("");
            }
        }
    }

    private double evaluateSimpleExpression(String expr) {
        // This is a basic sequential evaluator (doesn't strictly follow BODMAS, just left-to-right)
        // for more complex logic, a library like exp4j could be used.
        String[] parts = expr.trim().split("\\s+");
        if (parts.length == 0) return 0;
        
        double res = Double.parseDouble(parts[0].replace("−", "-"));
        
        for (int i = 1; i < parts.length; i += 2) {
            if (i + 1 >= parts.length) break;
            String op = parts[i];
            double nextVal = Double.parseDouble(parts[i+1].replace("−", "-"));
            
            switch (op) {
                case "+": res += nextVal; break;
                case "−": res -= nextVal; break;
                case "×": res *= nextVal; break;
                case "÷": 
                    if (nextVal != 0) res /= nextVal;
                    else return Double.NaN;
                    break;
            }
        }
        return res;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
