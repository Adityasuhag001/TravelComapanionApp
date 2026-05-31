package com.adityasuhag.travelcomapanionapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // declaring all the UI elements
    Spinner categorySpinner, fromSpinner, toSpinner;
    EditText inputField;
    Button convertButton;
    TextView resultText;

    // creating arrays for the dropdown options
    String[] categories = {"Currency", "Fuel Efficiency & Distance", "Temperature"};
    String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    String[] fuelUnits = {"mpg", "km/L", "Gallon (US)", "Liters", "Nautical Mile", "Kilometers"};
    String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // connecting java variables to the XML elements using their ids
        categorySpinner = findViewById(R.id.categorySpinner);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        inputField = findViewById(R.id.inputField);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        // setting up the category dropdown
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(catAdapter);

        // when user picks a category, update the from/to dropdowns with the right units
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] units;
                // pick the right array based on category
                if (position == 0) {
                    units = currencyUnits;
                } else if (position == 1) {
                    units = fuelUnits;
                } else {
                    units = tempUnits;
                }
                // set both dropdowns to show the units for this category
                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, units);
                fromSpinner.setAdapter(unitAdapter);
                toSpinner.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // when user clicks convert button
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConversion();
            }
        });
    }

    // main conversion method - reads input, does the math, shows result
    private void doConversion() {
        String input = inputField.getText().toString().trim();

        // check if input is empty
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        // try to convert input to a number
        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        String from = fromSpinner.getSelectedItem().toString();
        String to = toSpinner.getSelectedItem().toString();
        int category = categorySpinner.getSelectedItemPosition();

        // if user picked the same unit for both, just return the same value
        if (from.equals(to)) {
            resultText.setText(value + " " + to);
            Toast.makeText(this, "Same unit selected", Toast.LENGTH_SHORT).show();
            return;
        }

        double result = 0;

        // do the conversion based on which category is selected
        if (category == 0) {
            result = convertCurrency(from, to, value);
        } else if (category == 1) {
            // fuel values cant be negative
            if (value < 0) {
                Toast.makeText(this, "Value cannot be negative for this category", Toast.LENGTH_SHORT).show();
                return;
            }
            result = convertFuel(from, to, value);
        } else {
            result = convertTemperature(from, to, value);
        }

        // show the result rounded to 4 decimal places
        resultText.setText(String.format("%.4f %s", result, to));
    }

    // currency conversion - first convert to USD, then convert from USD to target
    private double convertCurrency(String from, String to, double value) {
        // step 1: convert whatever currency to USD
        double inUSD = value;
        if (from.equals("AUD")) inUSD = value / 1.55;
        else if (from.equals("EUR")) inUSD = value / 0.92;
        else if (from.equals("JPY")) inUSD = value / 148.50;
        else if (from.equals("GBP")) inUSD = value / 0.78;

        // step 2: convert USD to the target currency
        double result = inUSD;
        if (to.equals("AUD")) result = inUSD * 1.55;
        else if (to.equals("EUR")) result = inUSD * 0.92;
        else if (to.equals("JPY")) result = inUSD * 148.50;
        else if (to.equals("GBP")) result = inUSD * 0.78;

        return result;
    }

    // fuel and distance conversion
    private double convertFuel(String from, String to, double value) {
        // fuel efficiency: mpg <-> km/L
        if (from.equals("mpg") && to.equals("km/L")) return value * 0.425;
        if (from.equals("km/L") && to.equals("mpg")) return value / 0.425;

        // volume: gallons <-> liters
        if (from.equals("Gallon (US)") && to.equals("Liters")) return value * 3.785;
        if (from.equals("Liters") && to.equals("Gallon (US)")) return value / 3.785;

        // distance: nautical miles <-> kilometers
        if (from.equals("Nautical Mile") && to.equals("Kilometers")) return value * 1.852;
        if (from.equals("Kilometers") && to.equals("Nautical Mile")) return value / 1.852;

        // if someone tries to convert across types like mpg to liters
        Toast.makeText(this, "Cannot convert between different types", Toast.LENGTH_SHORT).show();
        return 0;
    }

    // temperature conversion - convert to celsius first then to target
    private double convertTemperature(String from, String to, double value) {
        // step 1: convert input to celsius
        double celsius = value;
        if (from.equals("Fahrenheit")) celsius = (value - 32) / 1.8;
        if (from.equals("Kelvin")) celsius = value - 273.15;

        // step 2: convert celsius to target
        if (to.equals("Fahrenheit")) return (celsius * 1.8) + 32;
        if (to.equals("Kelvin")) return celsius + 273.15;
        return celsius;
    }
}