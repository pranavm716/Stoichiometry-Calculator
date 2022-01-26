package com.example.prana_000.stoichiometrycalculator;

import java.util.ArrayList;

public class Compound {
    private String name;
    private String nameWithCoefficient;
    private String nameWithoutCoefficient;

    private String completeSubscriptsWithCoefficient;
    private String completeSubscriptsWithoutCoefficient;
    private String completeSubscriptsWithCoefficientWithoutParentheses;
    private String completeSubscriptsWithoutCoefficientWithoutParentheses;

    private String nameWithoutParentheses;
    private String nameWithCoefficientWithoutParentheses;
    private String nameWithoutCoefficientWithoutParentheses;

    private int coefficient;
    private double molarMass;
    private ArrayList<Element> individualElements = new ArrayList<>();
    private ArrayList<Integer> elementFrequency = new ArrayList<>();
    public PeriodicTable table = new PeriodicTable();

    public Compound(String name){
        this.name = name;

        if (!Character.isDigit(name.charAt(0))) {
            nameWithCoefficient = "1" + name;
        }else {
            nameWithCoefficient = name;
        }


        setupProperties();
    }

    private void setupProperties() {
        coefficient = findCoefficient();

        nameWithoutCoefficient = findNameWithoutCoefficient();

        completeSubscriptsWithoutCoefficient = findCompleteSubscripts();
        completeSubscriptsWithCoefficient = coefficient + completeSubscriptsWithoutCoefficient;

        if (nameWithCoefficient.contains("(")) {
            nameWithoutParentheses = findNameWithoutParentheses();
        }else {
            nameWithoutParentheses = nameWithoutCoefficient;
        }

        completeSubscriptsWithCoefficientWithoutParentheses = "" + coefficient;
        completeSubscriptsWithCoefficientWithoutParentheses += (nameWithCoefficient.contains("(")) ? nameWithoutParentheses : completeSubscriptsWithoutCoefficient;
        completeSubscriptsWithoutCoefficientWithoutParentheses = completeSubscriptsWithCoefficientWithoutParentheses.substring(("" + coefficient).length());

        nameWithCoefficientWithoutParentheses = coefficient + nameWithoutParentheses;

        findCompoundContents();
        molarMass = findMolarMass();
    }

    public String findCompleteSubscripts() {
        String nameCopy = nameWithoutCoefficient;

        int start = nameCopy.charAt(0) != '(' ? 1 : 2;
        String rtn = "" + nameCopy.substring(0, start);
        for (int i = start; i < nameCopy.length(); i++) {
            char cur = nameCopy.charAt(i);
            if (Character.isUpperCase(cur)) {
                if (!Character.isDigit(nameCopy.charAt(i - 1))) {
                    rtn += "1" + cur;
                }else {
                    rtn += "" + cur;
                }
            }else if (cur == '(' || cur == ')') {
                if (!Character.isDigit(nameCopy.charAt(i - 1))) {
                    rtn += "1" + cur + nameCopy.charAt(i + 1);
                    i++;
                }else {
                    rtn += "" + cur + nameCopy.charAt(i + 1);
                    i++;
                }
            }else {
                rtn += cur;
            }
        }

        if (!Character.isDigit(nameCopy.charAt(nameCopy.length() - 1))) {
            rtn += "1";
        }

        return rtn;
    }

    private int findCoefficient() {
        String coefficient = "";

        int i = 0;
        while (Character.isDigit(nameWithCoefficient.charAt(i))) {
            coefficient += nameWithCoefficient.charAt(i);
            i++;
        }

        return Integer.parseInt(coefficient);
    }

    private String findNameWithoutCoefficient() {
        return nameWithCoefficient.substring(("" + coefficient).length());
    }

    private double findMolarMass() {
        double molarMass = 0;

        for (int i = 0; i < individualElements.size(); i++) {
            molarMass += elementFrequency.get(i) * individualElements.get(i).getMolarMass();
        }

        return molarMass;
    }

    private void findCompoundContents() {
        String nameCopy = completeSubscriptsWithoutCoefficientWithoutParentheses;
        while (nameCopy.length() > 0) {
            int i = 0;
            while (!Character.isDigit(nameCopy.charAt(i))) {
                i++;
            }

            String symbol = nameCopy.substring(0, i);
            Element e = table.getElement(symbol);
            assert e != null;
            individualElements.add(new Element(e.getName(), e.getMolarMass(), symbol, e.getAtomicNumber()));
            nameCopy = nameCopy.substring(i);

            int j = 0;
            while (j < nameCopy.length() && Character.isDigit(nameCopy.charAt(j))) {
                j++;
            }
            elementFrequency.add(Integer.parseInt(nameCopy.substring(0, j)));
            nameCopy = nameCopy.substring(j);
        }
    }

    private String findNameWithoutParentheses() {
        String nameCopy = completeSubscriptsWithoutCoefficient;
        String nameWithoutParentheses = "";

        //If cation has parentheses
        if (nameCopy.charAt(0) == '(') {
            int i = nameCopy.indexOf(")") + 1;
            while (!Character.isUpperCase(nameCopy.charAt(i))) {
                i++;
            }
            return findMultiple(nameCopy.substring(0, i)) + nameCopy.substring(i);
        }else {
            //If anion has parentheses
            int i = 0;
            while (nameCopy.charAt(i) != '(') {
                i++;
            }
            nameWithoutParentheses += nameCopy.substring(0, i);

            return nameWithoutParentheses + findMultiple(nameCopy.substring(i));
        }
    }

    private String findMultiple(String str) {
        String rtn = "";

        int multiple = Integer.parseInt(str.substring(str.indexOf(')') + 1));
        str = str.substring(0, str.indexOf(")"));

        for (int i = 0; i < str.length(); i++) {
            if (!("()").contains("" + str.charAt(i))) {
                if (Character.isDigit(str.charAt(i))) {
                    rtn += ((Integer.parseInt("" + str.charAt(i))) * multiple);
                }else {
                    rtn += str.charAt(i);
                }
            }
        }

        return rtn;
    }

    public ArrayList<Integer> getTotalFrequency() {
        ArrayList<Integer> rtn = new ArrayList<>();

        for (Integer anElementFrequency : elementFrequency) {
            rtn.add(coefficient * anElementFrequency);
        }

        return rtn;
    }

    public ArrayList<Integer> getIndividualFrequency() {
        return new ArrayList<>(elementFrequency);
    }

    public String getNameWithCoefficient() {
        return nameWithCoefficient;
    }

    public double getMolarMass() {
        return molarMass;
    }

    public String getNameWithoutParentheses() {
        return nameWithoutParentheses;
    }

    public ArrayList<Element> getIndividualElements() {
        return individualElements;
    }

    public ArrayList<Integer> getElementFrequency() {
        return elementFrequency;
    }

    public int getCoefficient() {
        return coefficient;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < individualElements.size() - 1; i++) {
            str += elementFrequency.get(i) + " " + individualElements.get(i).getName() + ", ";
        }
        str += elementFrequency.get(individualElements.size() - 1) + " " + individualElements.get(individualElements.size() - 1).getName();

        return String.format("Full name: %s\nName: %s\nName without parentheses: %s\nFull subscript name: %s\nCoefficient: %d\nContents: %s\nMolar Mass: %f",
                nameWithCoefficient, nameWithoutCoefficient, nameWithoutParentheses, completeSubscriptsWithoutCoefficient, coefficient, str, molarMass);
    }

    public String getNameWithoutCoefficient() {
        return nameWithoutCoefficient;
    }

    public String getName() {
        return name;
    }
}
