package com.example.stoichiometrycalculator;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prana_000.stoichiometrycalculator.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final  double AVAGADROS_NUMBER = 6.0221409e23;
    public final double LITERS_GAS_STP = 22.414;
    public final String MOLES = "Moles", GRAMS = "Grams", PARTICLES = "Particles", LITERS_OF_GAS = "Liters";
    public ArrayList<Compound> reactants = new ArrayList<>(), products = new ArrayList<>(), combined = new ArrayList<>();
    public String display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button next = findViewById(R.id.nextButton);
        final Button reset = findViewById(R.id.resetBtn);
        final LinearLayout layout = findViewById(R.id.calcLayout);
        final EditText enterRxn = findViewById(R.id.chemRxn);
        final Spinner firstSpinner = findViewById(R.id.firstCompoundSpinner);
        final Spinner secondSpinner = findViewById(R.id.secondCompoundSpinner);
        final Spinner option1Spinner = findViewById(R.id.firstOptionSpinner);
        final Spinner option2Spinner = findViewById(R.id.secondOptionSpinner);
        final EditText numberEntered = findViewById(R.id.numberEntered);
        final EditText calculatedValue = findViewById(R.id.calculatedValue);
        final Button infoButton = findViewById(R.id.infoButton);
        final Button copyToClipboard = findViewById(R.id.copyToClipBoard);
        final Button clearEquation = findViewById(R.id.clearEqButton);
        final Button calculate = findViewById(R.id.calculateButton);

        layout.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        calculate.setVisibility(View.INVISIBLE);
        infoButton.setVisibility(View.INVISIBLE);

        String[] options = new String[] {GRAMS, MOLES, PARTICLES, LITERS_OF_GAS};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        option1Spinner.setAdapter(adapter);
        option2Spinner.setAdapter(adapter);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setVisibility(View.INVISIBLE);
                enterRxn.setText("");
                next.setVisibility(View.VISIBLE);
                reset.setVisibility(View.INVISIBLE);
                calculate.setVisibility(View.INVISIBLE);
                infoButton.setVisibility(View.INVISIBLE);
                copyToClipboard.setVisibility(View.VISIBLE);
                clearEquation.setVisibility(View.VISIBLE);

                numberEntered.setText("");
                calculatedValue.setText("");
            }
        });

        numberEntered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!numberEntered.getText().toString().equals("")) {
                    if (numberEntered.getText().toString().equals(".")) {
                        numberEntered.setText("0.");
                        numberEntered.setSelection(numberEntered.getText().toString().length());
                    }
                    calculate.performClick();
                }else {
                    calculatedValue.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!numberEntered.getText().toString().equals("")) {
                    calculate.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        option1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!numberEntered.getText().toString().equals("")) {
                    calculate.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        option2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!numberEntered.getText().toString().equals("")) {
                    calculate.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clearEquation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterRxn.setText("");
            }
        });

        copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterRxn.getText().toString().equals("")) {
                    Toast fail = Toast.makeText(getApplicationContext(), "There is no reaction to copy!", Toast.LENGTH_SHORT);
                    fail.show();
                }else {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Reaction", enterRxn.getText().toString());
                    clipboard.setPrimaryClip(clip);

                    Toast success = Toast.makeText(getApplicationContext(), "Reaction copied to clipboard!", Toast.LENGTH_LONG);
                    success.show();
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Information");
                alertDialog.setMessage(display);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstSelected = combined.get(firstSpinner.getSelectedItemPosition()).getNameWithCoefficient();
                String secondSelected = "";
                for (Compound c : combined) {
                    if (c.getNameWithoutCoefficient().equals(secondSpinner.getSelectedItem().toString())) {
                        secondSelected = c.getNameWithCoefficient();
                        break;
                    }
                }

                String firstType = option1Spinner.getSelectedItem().toString();
                String secondType = option2Spinner.getSelectedItem().toString();

                if (firstSelected.equals(secondSelected)) {
                    Toast errorMsg = Toast.makeText(getApplicationContext(), String.format("You cannot find how many %s of %s is needed to yield itself!", firstType.toLowerCase(), firstSelected), Toast.LENGTH_LONG);
                    errorMsg.show();
                } else {
                    calculatedValue.setText("");
                    try {
                        double numEntered = Double.parseDouble(numberEntered.getText().toString());
                        double output = calculate(numEntered, new Compound(firstSelected), new Compound(secondSelected), firstType, secondType);
                        String temp = "" + output;

                        if (!temp.contains("E")) {
                            calculatedValue.setText(String.format("%.5f", output));
                        } else {
                            calculatedValue.setText(String.format("%.5e", output));
                        }
                    } catch (NumberFormatException | NullPointerException e) {
                        Toast errorMsg = Toast.makeText(getApplicationContext(), "Please enter a value for the " + firstType + " of " + firstSelected, Toast.LENGTH_LONG);
                        errorMsg.show();
                    }
                }
            }
        });

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String firstSelected = combined.get(firstSpinner.getSelectedItemPosition()).getNameWithoutCoefficient();
                String secondSelected = secondSpinner.getSelectedItem().toString();


                ArrayList<String> updated = new ArrayList<>();
                for (Compound c : combined) {
                    if(!c.getNameWithoutCoefficient().equals(firstSelected)) {
                        updated.add(c.getNameWithoutCoefficient());
                    }
                }

                listSpinner(secondSpinner, new ArrayList<String>());
                listSpinner(secondSpinner, updated);

                if (!firstSelected.equals(secondSelected)) {
                    for (int j = 0; j < secondSpinner.getAdapter().getCount(); j++) {
                        if (secondSpinner.getItemAtPosition(j).toString().equals(secondSelected)) {
                            secondSpinner.setSelection(j);
                            break;
                        }
                    }
                }

                if (!numberEntered.getText().toString().equals("")) {
                    calculate.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reactants.clear();
                products.clear();
                combined.clear();

                ArrayList<Compound> arrReactants, arrProducts;
                String reaction = enterRxn.getText().toString();

                if (!reaction.contains("=")) {
                    Toast errorMsg = Toast.makeText(getApplicationContext(), "Please enter a valid equation!", Toast.LENGTH_LONG);
                    errorMsg.show();
                } else {
                    try {
                        arrReactants = findCompoundArrays(reaction, true);
                        arrProducts = findCompoundArrays(reaction, false);

                        for (int i = 0; i < arrReactants.size(); i++) {
                            reactants.add(arrReactants.get(i));
                            combined.add(arrReactants.get(i));
                        }

                        for (int i = 0; i < arrProducts.size(); i++) {
                            products.add(arrProducts.get(i));
                            combined.add(arrProducts.get(i));
                        }

                        List<String> arrCombined = new ArrayList<>();
                        for (Compound c : combined) {
                            arrCombined.add(c.getNameWithoutCoefficient());
                        }

                        findInfo();

                        if (reactionIsBalanced(arrReactants, arrProducts)) {
                            listSpinner(firstSpinner, arrCombined);
                            listSpinner(secondSpinner, arrCombined.subList(1, arrCombined.size()));

                            copyToClipboard.setVisibility(View.INVISIBLE);
                            clearEquation.setVisibility(View.INVISIBLE);

                            layout.setVisibility(View.VISIBLE);
                            next.setVisibility(View.INVISIBLE);
                            reset.setVisibility(View.VISIBLE);
                            infoButton.setVisibility(View.VISIBLE);
                        }else {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Warning");
                            alertDialog.setMessage("The equation you have entered is not balanced.\nThis may lead to inaccurate calculations.\nDo you wish to proceed?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            layout.setVisibility(View.VISIBLE);
                                            next.setVisibility(View.INVISIBLE);
                                            reset.setVisibility(View.VISIBLE);
                                            infoButton.setVisibility(View.VISIBLE);
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    } catch (Exception e) {
                        Toast errorMsg = Toast.makeText(getApplicationContext(), "Please enter a valid equation!", Toast.LENGTH_LONG);
                        errorMsg.show();
                    }
                }
            }
        });
    }

    private void findInfo() {
        display = "Reactants Molar Masses: (g/mol)\n\n";
        for (Compound c : reactants) {
            display += String.format("%d %s: \t\t\t%.3f\n", c.getCoefficient(), c.getNameWithoutCoefficient(), c.getMolarMass());
        }

        display += "\nProducts Molar Masses: (g/mol)\n\n";

        for (Compound c : products) {
            display += String.format("%d %s: \t\t\t%.3f\n", c.getCoefficient(), c.getNameWithoutCoefficient(), c.getMolarMass());
        }
//                String display = String.format("%-30s%-30s%-30s\n\n", "Compound", "Coefficient", "Molar Mass (g/mol)");
//                for (Compound c : reactants) {
//                    display += String.format("%-30s%-30d%-30.3f\n", c.getNameWithoutCoefficient(), c.getCoefficient(), c.getMolarMass());
//                }
//
//                display += "\n\n";
//
//                for (Compound c : products) {
//                    display += String.format("%-30s%-30d%-30.3f\n", c.getNameWithoutCoefficient(), c.getCoefficient(), c.getMolarMass());
//                }
    }

    /**
     *
     * @param compoundName The compound name without its coefficient
     * @return Whether the compound pass in is a reactant or not
     */
    private boolean isReactant(String compoundName) {
        for (Compound c : reactants) {
            if (c.getNameWithoutCoefficient().equals(compoundName)) {
                return true;
            }
        }
        return false;
    }

    private void listSpinner(Spinner spinner, List<String> names) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     *
     * @param reaction The reaction
     * @param isReactants Whether the method should return the reactants or the products as an arraylist
     * @return An arraylist containing the reactants or products
     */
    private ArrayList<Compound> findCompoundArrays(String reaction, boolean isReactants) {
        reaction = reaction.trim();
        String oneSideReaction = isReactants ? reaction.substring(0, reaction.indexOf("=")).trim() : reaction.substring(reaction.indexOf("=") + 1).trim();
        ArrayList<Compound> rtn = new ArrayList<>();

        if (oneSideReaction.contains("+")) {
            while (oneSideReaction.length() > 0 && oneSideReaction.contains("+")) {
                String curCompound = oneSideReaction.substring(0, oneSideReaction.indexOf("+")).trim();
                curCompound = curCompound.replaceAll("\\s+", "");
                rtn.add(new Compound(curCompound));
                oneSideReaction = oneSideReaction.substring(oneSideReaction.indexOf("+") + 1).trim();
            }
        }

        oneSideReaction = oneSideReaction.replaceAll("\\s+", "");
        rtn.add(new Compound(oneSideReaction));
        return rtn;
    }

    public double calculate(double numEntered, Compound firstCompoundSelected, Compound secondCompoundSelected, String firstType, String secondType) {
        if(numEntered == 0) return 0;
        double rtn = numEntered;
        switch (firstType) {
            case GRAMS:
                rtn /= firstCompoundSelected.getMolarMass();
                break;
            case PARTICLES:
                rtn /= AVAGADROS_NUMBER;
                break;
            case LITERS_OF_GAS:
                rtn /= LITERS_GAS_STP;
                break;
        }

        rtn *= (double)secondCompoundSelected.getCoefficient() / firstCompoundSelected.getCoefficient();

        switch (secondType) {
            case GRAMS:
                rtn *= secondCompoundSelected.getMolarMass();
                break;
            case PARTICLES:
                rtn *= AVAGADROS_NUMBER;
                break;
            case LITERS_OF_GAS:
                rtn *= LITERS_GAS_STP;
                break;
        }

        return rtn;
    }

    public boolean reactionIsBalanced(ArrayList<Compound> reactants, ArrayList<Compound> products) {
        ArrayList<Element> allElementsReactants = reactants.get(0).getIndividualElements();
        ArrayList<Integer> totalElementFrequencyReactants = reactants.get(0).getTotalFrequency();
        ArrayList<Element> allElementsProducts = products.get(0).getIndividualElements();
        ArrayList<Integer> totalElementFrequencyProducts = products.get(0).getTotalFrequency();


        for (int i = 1; i < reactants.size(); i++) {
            ArrayList<Element> curElem = reactants.get(i).getIndividualElements();
            ArrayList<Integer> curTotalFreq = reactants.get(i).getTotalFrequency();

            for (int j = 0; j < curElem.size(); j++) {
                Element e = curElem.get(j);
                int curFreq = curTotalFreq.get(j);
                if (allElementsReactants.indexOf(e) != -1) {
                    totalElementFrequencyReactants.set(allElementsReactants.indexOf(e), totalElementFrequencyReactants.get(allElementsReactants.indexOf(e)) + curFreq);
                } else {
                    allElementsReactants.add(e);
                    totalElementFrequencyReactants.add(curFreq);
                }
            }
        }

        for (int i = 1; i < products.size(); i++) {
            ArrayList<Element> curElem = products.get(i).getIndividualElements();
            ArrayList<Integer> curTotalFreq = products.get(i).getTotalFrequency();

            for (int j = 0; j < curElem.size(); j++) {
                Element e = curElem.get(j);
                int curFreq = curTotalFreq.get(j);
                if (allElementsProducts.indexOf(e) > -1) {
                    totalElementFrequencyProducts.set(allElementsProducts.indexOf(e), totalElementFrequencyProducts.get(allElementsProducts.indexOf(e)) + curFreq);
                }else {
                    allElementsProducts.add(e);
                    totalElementFrequencyProducts.add(curFreq);
                }
            }
        }

        Collections.sort(allElementsReactants);
        Collections.sort(allElementsProducts);
        Collections.sort(totalElementFrequencyReactants);
        Collections.sort(totalElementFrequencyProducts);

        return allElementsReactants.equals(allElementsProducts) && totalElementFrequencyReactants.equals(totalElementFrequencyProducts);
    }
}
