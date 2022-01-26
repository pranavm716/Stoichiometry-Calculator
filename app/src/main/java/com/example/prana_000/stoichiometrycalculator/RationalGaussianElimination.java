package com.example.prana_000.stoichiometrycalculator;

public class RationalGaussianElimination{

    public static Rational[][] REF(Rational[][] matrix){
      Rational[][] copy = matrixCopy(matrix);
      return forwardPass(copy);
    }
  
    public static Rational[][] RREF(Rational[][] matrix) {
      Rational[][] copy = matrixCopy(matrix);
      return backwardPass(forwardPass(copy));
    }
  
    private static Rational[][] forwardPass(Rational[][] matrix){
      final int numRows = matrix.length, numCols = matrix[0].length;
  
      for(int i = 0; i < numRows; i++){
        int nextPivotRow = findNextPivotRow(matrix, i);
        if(nextPivotRow != -1){
          if(nextPivotRow != i){
            swapRows(matrix, nextPivotRow, i);
          }
  
          Rational pivot = matrix[i][0];
          int k = 0;
          while(pivot.isZero() && k < numCols){
            k++;
            pivot = matrix[i][k];
          }
  
          // printMatrix(matrix);
          scaleRow(matrix, i, Rational.divide(new Rational(1), pivot));
          // printMatrix(matrix);
  
  
          for(int j = i + 1; j < numRows; j++){
            addRow(matrix, j, Rational.multiply(new Rational(-1), matrix[j][k]), i);
            // printMatrix(matrix);
          }
        }else{
          break;
        }
      }
  
      return matrix;
    }
  
    // Precondition: matrix is already in row echelon form
    private static Rational[][] backwardPass(Rational[][] matrix){
      final int numRows = matrix.length, numCols = matrix[0].length;
  
      int i = numRows - 1;
      while(true){
        int prevPivotRow = findPrevPivotRow(matrix, i);
        if(prevPivotRow != -1){
  
          i = prevPivotRow;
  
          Rational pivot = matrix[i][0];
          int k = 0;
          while(pivot.isZero() && k < numCols){
            k++;
            pivot = matrix[i][k];
          }
  
          for(int j = i - 1; j >= 0; j--){
            addRow(matrix, j, Rational.multiply(new Rational(-1), matrix[j][k]), i);
            // printMatrix(matrix);
          }
  
          i--;
        }else{
          break;
        }
      }
  
      return matrix;
    }
  
    private static Rational[][] matrixCopy(Rational[][] matrix) {
      Rational[][] copy = new Rational[matrix.length][matrix[0].length];
  
      for(int i = 0; i < matrix.length; i++){
        for(int j = 0; j < matrix[0].length; j++){
          copy[i][j] = matrix[i][j];
        }
      }
  
      return copy;
    }
  
    // Returns the next available row <= currentPivotRow with a valid pivot
    // If no pivot rows are found <= currentPivotRow, -1 is returned
    private static int findPrevPivotRow(Rational[][] matrix, int currentPivotRow){
      for(int i = currentPivotRow; i >= 0; i--){
        for(int j = 0; j < matrix[0].length; j++){
          if(!matrix[i][j].isZero()){
            return i;
          }
        }
      }
  
      return -1;
    }
  
    // Returns the next available row >= currentPivotRow with a valid pivot
    // If no pivot rows are found >= currentPivotRow, -1 is returned
    private static int findNextPivotRow(Rational[][] matrix, int currentPivotRow){
      int possibleNextPivot = -1;
      for(int i = currentPivotRow; i < matrix.length; i++){
        for(int j = 0; j < matrix[0].length; j++){
          if(!matrix[i][j].isZero()){
            if(j == i){
              return i;
            }else{
              possibleNextPivot = i;
            }
          }
        }
      }
  
      return possibleNextPivot;
    }
  
    // ELEMENTARY ROW OPERATIONS
  
    // row1 <-> row2
    private static void swapRows(Rational[][] matrix, int row1, int row2){
      for(int i = 0; i < matrix[0].length; i++){
        Rational temp = matrix[row1][i];
        matrix[row1][i] = matrix[row2][i];
        matrix[row2][i] = temp;
      }
    }
  
    // row -> scalar * row
    private static void scaleRow(Rational[][] matrix, int row, Rational scalar){
      for(int i = 0; i < matrix[0].length; i++){
        // matrix[row][i] *= scalar;
        matrix[row][i] = Rational.multiply(matrix[row][i], scalar);
      }
    }
    
    // destRow -> scalar * addRow + destRow
    private static void addRow(Rational[][] matrix, int destinationRow, Rational scalar, int addRow){
      for(int i = 0; i < matrix[0].length; i++){
        // matrix[destinationRow][i] += (scalar * matrix[addRow][i]);
        matrix[destinationRow][i] = Rational.add(matrix[destinationRow][i], Rational.multiply(scalar, matrix[addRow][i]));
      }
    }
  
    public static void printMatrix(Rational[][] matrix){
      for(Rational[] x : matrix){
        for(Rational i : x){
          System.out.print(i + " ");
        }
        System.out.println();
      }
      System.out.println();
    }
  
    public static void main(String[] args) {
      // double[][] a = {
      //   {4, 0, -1, 0},
      //   {0, 2, -3, 0},
      //   {10, 1, -4, 0},
      //   {1, 0, 0, 1}
      // };
  
      // double[][] a = {
      //   {221, 0, 0, -1, 0},
      //   {444, 0, -2, 0, 0},
      //   {0, 2, -1, -2, 0},
      //   {1, 0, 0, 0, 1}
      // };
      
      // double[][] a = {
      //   {2, -1, 0, 0, 0, 0},
      //   {2, -1, 0, 0, 0, 0},
      //   {2, -1, 0, 0, 0, 0},
      //   {2, -1, 0, 0, 0, 0},
      //   {1, 0, 0, 0, 0, 1},
      // };
  
    //   printMatrix(rref);
    }
  }