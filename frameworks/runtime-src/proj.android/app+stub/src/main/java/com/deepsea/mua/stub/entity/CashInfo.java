package com.deepsea.mua.stub.entity;

import com.google.gson.annotations.SerializedName;

public class CashInfo {

    /**
     * balance : 5000
     * status : 1
     * zfb : 15711245587
     * cash : 50
     * cash_price : 10
     * cash_tax : 0.12
     */

    private double balance;
    private String status;
    private String zfb;
    private int cash;
    private int cash_price;
    private double cash_tax;
    /**
     * balance : 5000
     * redpack_cash : 50
     * redpack_cash_price : 10
     * redpack_num : 0.12
     */


    private int redpack_cash;
    private int redpack_cash_price;
    private double redpack_num;


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getZfb() {
        return zfb;
    }

    public void setZfb(String zfb) {
        this.zfb = zfb;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCash_price() {
        return cash_price;
    }

    public void setCash_price(int cash_price) {
        this.cash_price = cash_price;
    }

    public double getCash_tax() {
        return cash_tax;
    }

    public void setCash_tax(double cash_tax) {
        this.cash_tax = cash_tax;
    }


    public int getRedpack_cash() {
        return redpack_cash;
    }

    public void setRedpack_cash(int redpack_cash) {
        this.redpack_cash = redpack_cash;
    }

    public int getRedpack_cash_price() {
        return redpack_cash_price;
    }

    public void setRedpack_cash_price(int redpack_cash_price) {
        this.redpack_cash_price = redpack_cash_price;
    }

    public double getRedpack_num() {
        return redpack_num;
    }

    public void setRedpack_num(double redpack_num) {
        this.redpack_num = redpack_num;
    }
}
