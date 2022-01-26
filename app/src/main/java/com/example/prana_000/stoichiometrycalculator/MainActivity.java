package com.example.prana_000.stoichiometrycalculator;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    public final  double AVAGADROS_NUMBER = 6.0221409e23;
    public final double LITERS_GAS_STP = 22.414;
    public final String MOLES = "Moles", GRAMS = "Grams", PARTICLES = "Particles", LITERS_OF_GAS = "Liters";
    public ArrayList<Compound> reactants = new ArrayList<>(), products = new ArrayList<>(), combined = new ArrayList<>();
    public String display;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        calculatedValue.setFocusable(EditText.NOT_FOCUSABLE);
        calculatedValue.setFocusableInTouchMode(false);

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

                final ArrayList<Compound> arrReactants, arrProducts;
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

                        int[] coef = new int[combined.size()];
                        for(int i = 0; i < coef.length; i++){
                            coef[i] = combined.get(i).getCoefficient();
                        }

                        final int[][] rxnMatrix = createRxnMatrix(arrReactants, arrProducts);
//                        final BruteForceSolver solution = new BruteForceSolver(rxnMatrix);

                        boolean rxnIsBalanced = isBalanced(rxnMatrix, coef);

                        if (rxnIsBalanced) {
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
                            String notBalancedMessage = "The equation you have entered is not balanced.\n" +
                                    "This may lead to inaccurate calculations.\n" +
                                    "Let the balancing algorithm try to find a solution?";
                            alertDialog.setMessage(notBalancedMessage);
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            RationalRREFSolver solution = new RationalRREFSolver(rxnMatrix);
                                            int[] balancedCoefs;

                                            try{
                                                balancedCoefs = solution.solve();
                                                String balancedEqString = "";
                                                for(int i = 0; i < arrReactants.size() - 1; i++){
                                                    if(balancedCoefs[i] != 1) {
                                                        balancedEqString += "" + balancedCoefs[i] + arrReactants.get(i).getNameWithoutCoefficient() + " + ";
                                                    }else {
                                                        balancedEqString += arrReactants.get(i).getNameWithoutCoefficient() + " + ";
                                                    }
                                                }

                                                if(balancedCoefs[arrReactants.size() - 1] != 1) {
                                                    balancedEqString += "" + balancedCoefs[arrReactants.size() - 1] + arrReactants.get(arrReactants.size() - 1).getNameWithoutCoefficient() + " = ";
                                                }else{
                                                    balancedEqString += arrReactants.get(arrReactants.size() - 1).getNameWithoutCoefficient() + " = ";
                                                }

                                                for(int i = 0; i < arrProducts.size() - 1; i++){
                                                    if(balancedCoefs[i + arrReactants.size()] != 1) {
                                                        balancedEqString += "" + balancedCoefs[i + arrReactants.size()] + arrProducts.get(i).getNameWithoutCoefficient() + " + ";
                                                    }else{
                                                        balancedEqString += arrProducts.get(i).getNameWithoutCoefficient() + " + ";
                                                    }
                                                }
                                                if(balancedCoefs[balancedCoefs.length - 1] != 1) {
                                                    balancedEqString += "" + balancedCoefs[balancedCoefs.length - 1] + arrProducts.get(arrProducts.size() - 1).getNameWithoutCoefficient();
                                                }else{
                                                    balancedEqString += arrProducts.get(arrProducts.size() - 1).getNameWithoutCoefficient();
                                                }

                                                Toast balanceFound = Toast.makeText(getApplicationContext(), "Solution found!", Toast.LENGTH_LONG);
                                                balanceFound.show();

                                                enterRxn.setText(balancedEqString);

                                                next.performClick();
                                            }catch (NoSolutionException e){
                                                Toast noBalanceFound = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                                                noBalanceFound.show();
                                            }
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Proceed without balancing",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            layout.setVisibility(View.VISIBLE);
                                            next.setVisibility(View.INVISIBLE);
                                            reset.setVisibility(View.VISIBLE);
                                            infoButton.setVisibility(View.VISIBLE);
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

    private boolean isBalanced(int[][] rxn, int[] coef) {
        int sum = 0;
        for(int[] elem : rxn){
            for(int s = 0; s < elem.length; s++){
                sum += elem[s] * coef[s];
            }

            if(sum != 0) return false;
        }

        return true;
    }

    private int[][] createRxnMatrix(ArrayList<Compound> reactants, ArrayList<Compound> products) {

//        ArrayList<Element> allElementsReactants = reactants.get(0).getIndividualElements();
//        ArrayList<Integer> totalElementFrequencyReactants = reactants.get(0).getTotalFrequency();
//        ArrayList<Element> allElementsProducts = products.get(0).getIndividualElements();
//        ArrayList<Integer> totalElementFrequencyProducts = products.get(0).getTotalFrequency();

        ArrayList<Element> allElementsReactants = new ArrayList<>(), allElementsProducts = new ArrayList<>();

//        ArrayList<Integer>[] rxnMatrix = new ArrayList[reactants.size() + products.size()];
        int[][] rxnMatrix = new int[PeriodicTable.NUM_ELEMENTS][reactants.size() + products.size()];
//        for(int i = 0; i < rxnMatrix.length; i++){
//            rxnMatrix[i] = new ArrayList(1);
//        }
//        rxnMatrix[0] = totalElementFrequencyReactants;

        for (int i = 0; i < reactants.size(); i++) {
            ArrayList<Element> curElem = reactants.get(i).getIndividualElements();
            ArrayList<Integer> curIndividualFreq = reactants.get(i).getIndividualFrequency();

            for (int j = 0; j < curElem.size(); j++) {
                Element elementToAdd = curElem.get(j);
                int curFreq = curIndividualFreq.get(j);
                if (allElementsReactants.indexOf(elementToAdd) == -1) {
                    allElementsReactants.add(elementToAdd);
                }

                rxnMatrix[allElementsReactants.indexOf(elementToAdd)][i] = curFreq;

            }
        }

        for (int i = 0; i < products.size(); i++) {
            ArrayList<Element> curElem = products.get(i).getIndividualElements();
            ArrayList<Integer> curIndividualFreq = products.get(i).getIndividualFrequency();

            for (int j = 0; j < curElem.size(); j++) {
                Element elementToAdd = curElem.get(j);
                int curFreq = curIndividualFreq.get(j);
                if (allElementsProducts.indexOf(elementToAdd) == -1) {
                    allElementsProducts.add(elementToAdd);
                }

                rxnMatrix[allElementsReactants.indexOf(elementToAdd)][i + reactants.size()] = -1 * curFreq;

            }
        }

//        for (int i = 1; i < products.size(); i++) {
//            ArrayList<Element> curElem = products.get(i).getIndividualElements();
//            ArrayList<Integer> curTotalFreq = products.get(i).getTotalFrequency();
//
//            for (int j = 0; j < curElem.size(); j++) {
//                Element e = curElem.get(j);
//                int curFreq = curTotalFreq.get(j);
//                if (allElementsProducts.indexOf(e) != -1) {
//                    totalElementFrequencyProducts.set(allElementsProducts.indexOf(e), totalElementFrequencyProducts.get(allElementsProducts.indexOf(e)) + curFreq);
//                }else {
//                    allElementsProducts.add(e);
//                    totalElementFrequencyProducts.add(curFreq);
//                }
//            }
//        }

//        int[][] matrix = new int[rxnMatrix.length][rxnMatrix[0].size()];
//        for(int i = 0; i < rxnMatrix.length; i++){
//            for(int j = 0; j < rxnMatrix[0].size(); j++){
//                matrix[i][j] = (int)rxnMatrix[i].get(j);
//            }
//        }
//
//        return transpose(matrix);

        return rxnMatrix;
    }

    private int[][] transpose(int[][] rxn) {
        int columns = rxn[0].length;
        int rows = rxn.length;

        int[][] temp = new int[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                temp[j][i] = rxn[i][j];
            }
        }

        return temp;
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

    public boolean reactionIsBalanced(ArrayList<Compound> rcts, ArrayList<Compound> prods) {
        ArrayList<Compound> reactants = new ArrayList<>(), products = new ArrayList<>();
        for(int i = 0; i < rcts.size(); i++) reactants.add(rcts.get(i));
        for(Compound c : prods) products.add(c);

        ArrayList<Element> allElementsReactants = reactants.get(0).getIndividualElements();
        ArrayList<Integer> totalElementFrequencyReactants = reactants.get(0).getTotalFrequency();
        ArrayList<Element> allElementsProducts = products.get(0).getIndividualElements();
        ArrayList<Integer> totalElementFrequencyProducts = products.get(0).getTotalFrequency();

        for (int i = 1; i < reactants.size(); i++) {
            ArrayList<Element> curElem = reactants.get(i).getIndividualElements();
            ArrayList<Integer> curTotalFreq = reactants.get(i).getTotalFrequency();

            for (int j = 0; j < curElem.size(); j++) {
                Element elementToAdd = new Element(curElem.get(j).getName(), curElem.get(j).getMolarMass(), curElem.get(j).getSymbol(), curElem.get(j).getAtomicNumber());
                int curFreq = curTotalFreq.get(j);
                if (allElementsReactants.indexOf(elementToAdd) != -1) {
                    totalElementFrequencyReactants.set(allElementsReactants.indexOf(elementToAdd), totalElementFrequencyReactants.get(allElementsReactants.indexOf(elementToAdd)) + curFreq);
                } else {
                    allElementsReactants.add(elementToAdd);
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
