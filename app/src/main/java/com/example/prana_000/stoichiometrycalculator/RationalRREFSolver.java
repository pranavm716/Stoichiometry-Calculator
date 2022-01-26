package com.example.prana_000.stoichiometrycalculator;

import java.util.Arrays;

public class RationalRREFSolver {
  
  private Rational[][] modifiedMatrix;
  private int species;

  public RationalRREFSolver(int[][] matrix){
    species = matrix[0].length;

    // Set up modified matrix
    setupModifiedMatrix(matrix);
  }

  private void setupModifiedMatrix(int[][] matrix){
    modifiedMatrix = new Rational[matrix.length + 1][matrix[0].length + 1];

    for(int i = 0; i < modifiedMatrix.length; i++){
      for(int j = 0; j < modifiedMatrix[0].length; j++){
        if(i < matrix.length && j < matrix[0].length){
          modifiedMatrix[i][j] = new Rational(matrix[i][j]);
        }else if(i == modifiedMatrix.length - 1){
          if(j == 0 || j == modifiedMatrix[0].length - 1){
            modifiedMatrix[i][j] = new Rational(1);
          }else{
            modifiedMatrix[i][j] = new Rational(0);
          }
        }else{
            modifiedMatrix[i][j] = new Rational(0);
        }
      }
    }
    printModifiedMatrix();
  }

  public int[] solve() throws NoSolutionException {
    Rational[][] rref = RationalGaussianElimination.RREF(modifiedMatrix);
    Rational[] rationalCoef = new Rational[species];

    for(int i = 0; i < rationalCoef.length; i++){
        rationalCoef[i] = rref[i][rref[0].length - 1];
    }


    return convertToIntCoef(rationalCoef);
  }

  private int[] convertToIntCoef(Rational[] rationalCoef) throws NoSolutionException {
      int[] coef = new int[rationalCoef.length];
      int scalar = 1;

      System.out.println(Arrays.toString(rationalCoef));
      for(Rational r : rationalCoef){
          if(r.isFractional()){
            scalar = Math.max(scalar, r.getDenominator());
          }
      }

      for(int i = 0; i < coef.length; i++){
        //   System.out.println(rationalCoef[i].isFractional());
          if(rationalCoef[i].isFractional()){
            coef[i] = Rational.multiply(rationalCoef[i], new Rational(scalar)).getNumerator();
          }else{
              coef[i] = rationalCoef[i].getNumerator() * scalar;
          }
      }

      // No solution exists if all elements are 0, so sum of all elements must be 0
      //ik, it's a jank method
      int sum = 0;
      for(int i : coef){
          sum += i;
      }

      if(sum != 0){
          return coef;
      }else{
          throw new NoSolutionException("This is an impossible reaction!");
      }
  }

  public void printModifiedMatrix() {
    for(Rational[] x : modifiedMatrix){
      for(Rational i : x){
        System.out.print(i + " ");
      }
      System.out.println();
    }
  }
  
  public static void main(String[] args) {
      //impossible reaction: CH4 + O2 = H2CO3
    // int[][] a = {
    //   {0, 0, -1},
    //   {4, 0, -2},
    //   {0, 2, -3}
    // };

    // complex reaction: NaCl + SO2 + H2O + O2 = Na2SO4 + HCl
    // int[][] a = {
    //     {1, 0, 0, 0, -2, 0},
    //     {1, 0, 0, 0, 0, -1},
    //     {0, 1, 0, 0, -1, 0},
    //     {0, 0, 2, 0, 0, -1},
    //     {0, 2, 1, 2, -4, 0}
    //   };

    int[][] a = {
        {3, 0}
    };


  }
}