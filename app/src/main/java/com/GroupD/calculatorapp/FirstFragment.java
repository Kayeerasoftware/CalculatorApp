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
    private String currentInput = "";
    private String operator = "";
    private double firstValue = Double.NaN;
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
        binding.buttonEquals.setOnClickListener(v -> calculate());
    }

    private void appendNumber(String number) {
        if (isNewOp) {
            currentInput = "";
            isNewOp = false;
        }
        
        if (number.equals(".") && currentInput.contains(".")) {
            return;
        }
        
        if (currentInput.equals("0") && !number.equals(".")) {
            currentInput = number;
        } else {
            currentInput += number;
        }
        
        binding.textViewDisplay.setText(currentInput);
    }

    private void setOperator(String op) {
        if (!currentInput.isEmpty()) {
            if (!Double.isNaN(firstValue)) {
                calculate();
            }
            try {
                firstValue = Double.parseDouble(currentInput);
                operator = op;
                isNewOp = true;
            } catch (NumberFormatException e) {
                clear();
            }
        }
    }

    private void applyPercent() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput) / 100;
                currentInput = decimalFormat.format(value);
                binding.textViewDisplay.setText(currentInput);
            } catch (NumberFormatException e) {
                clear();
            }
        }
    }

    private void backspace() {
        if (currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            if (currentInput.isEmpty()) {
                currentInput = "0";
                isNewOp = true;
            }
            binding.textViewDisplay.setText(currentInput);
        }
    }

    private void clear() {
        currentInput = "0";
        firstValue = Double.NaN;
        operator = "";
        isNewOp = true;
        binding.textViewDisplay.setText(currentInput);
    }

    private void calculate() {
        if (!Double.isNaN(firstValue) && !currentInput.isEmpty()) {
            try {
                double secondValue = Double.parseDouble(currentInput);
                double result = 0;

                switch (operator) {
                    case "+": result = firstValue + secondValue; break;
                    case "−": result = firstValue - secondValue; break;
                    case "×": result = firstValue * secondValue; break;
                    case "÷": 
                        if (secondValue != 0) {
                            result = firstValue / secondValue;
                        } else {
                            binding.textViewDisplay.setText("Error");
                            currentInput = "0";
                            firstValue = Double.NaN;
                            isNewOp = true;
                            return;
                        }
                        break;
                }

                currentInput = decimalFormat.format(result);
                binding.textViewDisplay.setText(currentInput);
                firstValue = Double.NaN;
                operator = "";
                isNewOp = true;
            } catch (NumberFormatException e) {
                clear();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
