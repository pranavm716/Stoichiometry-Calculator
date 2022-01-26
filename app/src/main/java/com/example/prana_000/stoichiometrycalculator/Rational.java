package com.example.prana_000.stoichiometrycalculator;

public class Rational{
  private int numerator, denominator;
  private boolean isNegative;

  public Rational(int numerator, int denominator){
    isNegative = numerator * denominator < 0;
    
    this.numerator = Math.abs(numerator);
    this.denominator = Math.abs(denominator);

    // reduce(this);
  }

  public Rational(int value){
    isNegative = value < 0;
    this.numerator = Math.abs(value);
    this.denominator = 1;
  }

  public static Rational multiply(Rational r1, Rational r2){
    Rational product;
    if((r1.isPositive() && r2.isPositive()) || (r1.isNegative() && r2.isNegative())){
      product = new Rational(r1.getNumerator() * r2.getNumerator(), r1.getDenominator() * r2.getDenominator());
    }else{
      product =  new Rational(-1 * r1.getNumerator() * r2.getNumerator(), r1.getDenominator() * r2.getDenominator());
    }

    return reduce(product);
  }

  public static Rational divide(Rational r1, Rational r2){
    Rational quotient;
    if(r2.isPositive()){
      quotient = multiply(r1, new Rational(r2.getDenominator(), r2.getNumerator()));
    }else{
      quotient = multiply(r1, new Rational(-1 * r2.getDenominator(), r2.getNumerator()));
    }

    return reduce(quotient);
  }

  public static Rational add(Rational r1, Rational r2){
    Rational sum;

    int commonDenom = lcm(r1.getDenominator(), r2.getDenominator());
    int factor1 = commonDenom / r1.getDenominator();
    int factor2 = commonDenom / r2.getDenominator();

    int newNum1 = r1.getNumerator() * factor1;
    int newNum2 = r2.getNumerator() * factor2;

    if(r1.isPositive() && r2.isPositive()){
      sum = new Rational(newNum1 + newNum2, commonDenom);
    }else if(r1.isNegative() && r2.isPositive()){
      sum = new Rational(-1 * newNum1 + newNum2, commonDenom);
    }else if(r2.isNegative() && r1.isPositive()){
      sum = new Rational(newNum1 - newNum2, commonDenom);
    }else{
      sum = new Rational(-1 * (newNum1 + newNum2), commonDenom);
    }

    return reduce(sum);
  }

  public static Rational reduce(Rational r){
    int factor = gcf(r.getNumerator(), r.getDenominator());
    Rational reduced;

    if(r.isPositive()){
      reduced = new Rational(r.getNumerator() / factor, r.getDenominator() / factor);
    }else{
      reduced = new Rational(-1 * r.getNumerator() / factor, r.getDenominator() / factor);
    }

    return reduced;
  }

  private static int gcf(int a, int b){
    if (b == 0){
        return a;
    }else{
        return (gcf(b, a % b));
    }
  }

  private static int lcm(int a, int b){
    return (a * b) / gcf(a, b);
  }

  public int getNumerator(){
    return numerator;
  }

  public int getDenominator(){
    return denominator;
  }

  public boolean isNegative() {
    return isNegative;
  }

  public boolean isPositive(){
    return !isNegative;
  }

  public boolean isZero(){
    return numerator == 0;
  }

  public boolean isFractional(){
    return denominator != 1;
  }

  @Override
  public String toString(){
    if(numerator == 0){
      return "0";
    }else if(denominator == 1){
      return "" + numerator;
    }else{
      String value = numerator + "/" + denominator;
      return isNegative ? "-" + value : value;
    }
  }

  public static void main(String[] args) {
    Rational r1 = new Rational(-2, 4);
    Rational r2 = new Rational(2, -5);

    System.out.println(add(r1, r2));
    // System.out.println(reduce(r1));
  }
}