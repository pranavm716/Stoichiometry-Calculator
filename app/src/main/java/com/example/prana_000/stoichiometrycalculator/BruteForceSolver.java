package com.example.prana_000.stoichiometrycalculator;


public class BruteForceSolver{

    private int[][] rxn;
    private final int limit = 10000000;
    private int species;

    BruteForceSolver(int[][] rxn){
        this.rxn = rxn;
        species = rxn[0].length;
    }

    public int[] solve(){
        int cap = (int) Math.floor(Math.pow((double)limit, 1.0/species));

        for(int i = 0; i < (int)Math.pow(cap, species); i++){
            int[] coef = convertToBaseNumber(i, cap);
            if(isBalanced(coef)){
                return coef;
            }
        }

        return null;
    }

    public boolean isBalanced(int[] coef){
        int sum = 0;
        for(int[] elem : rxn){
            for(int s = 0; s < elem.length; s++){
                sum += elem[s] * coef[s];
            }

            if(sum != 0) return false;
        }

        return true;
    }

    private char reVal(int num) {
        return (char)(num + 48);
    }

    // This also adds 1 to each digit because coefficients cannot be 0
    private int[] convertToBaseNumber(int inputNum, int base) {
        String s = "";
        while (inputNum > 0) {
            s += reVal(inputNum % base);
            inputNum /= base;
        }

        StringBuilder ix = new StringBuilder();
        ix.append(s);

        String rtn = new String(ix.reverse());
        rtn = equalizeLength(rtn, species);

        int[] coef = new int[rtn.length()];
        for(int i = 0; i < coef.length; i++){
            coef[i] = ((int)(rtn.charAt(i))) - 48 + 1;
        }

        return coef;
    }

    private String equalizeLength(String value, int length){
        String rtn = value;
        if(rtn.length() < length){
            int numZerosToAdd = length - rtn.length();
            for(int i = 0; i < numZerosToAdd; i++){
                rtn = "0" + rtn;
            }
        }

        return rtn;
    }

}
