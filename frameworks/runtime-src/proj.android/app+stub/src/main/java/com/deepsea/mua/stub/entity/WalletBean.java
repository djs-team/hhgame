package com.deepsea.mua.stub.entity;

import java.math.BigDecimal;

/**
 * Created by JUN on 2019/5/5
 */
public class WalletBean {
    /**
     * diamond : 450
     * coin : 1450
     */

    private double diamond;
    private long coin;
    private long blue_coin;
    private double check;
    private double list_today_price;
    private double list_yes_price;
    private double list_week_price;
    private double list_month_price;
    private String scale;
    private int  type;
    private int  exchange_switch;
    private int  is_cash;
    private double redpacket_coin;
    private String red_list_today_price;
    private String red_list_yes_price;
    private String red_list_week_price;
    private String red_list_month_price;

    public double getRedpacket_coin() {
        return redpacket_coin;
    }

    public void setRedpacket_coin(double redpacket_coin) {
        this.redpacket_coin = redpacket_coin;
    }

    public String getRed_list_today_price() {
        return red_list_today_price;
    }

    public void setRed_list_today_price(String red_list_today_price) {
        this.red_list_today_price = red_list_today_price;
    }

    public String getRed_list_yes_price() {
        return red_list_yes_price;
    }

    public void setRed_list_yes_price(String red_list_yes_price) {
        this.red_list_yes_price = red_list_yes_price;
    }

    public String getRed_list_week_price() {
        return red_list_week_price;
    }

    public void setRed_list_week_price(String red_list_week_price) {
        this.red_list_week_price = red_list_week_price;
    }

    public String getRed_list_month_price() {
        return red_list_month_price;
    }

    public void setRed_list_month_price(String red_list_month_price) {
        this.red_list_month_price = red_list_month_price;
    }

    public long getBlue_coin() {
        return blue_coin;
    }

    public void setBlue_coin(long blue_coin) {
        this.blue_coin = blue_coin;
    }

    public int getExchange_switch() {
        return exchange_switch;
    }

    public void setExchange_switch(int exchange_switch) {
        this.exchange_switch = exchange_switch;
    }

    public int getIs_cash() {
        return is_cash;
    }

    public void setIs_cash(int is_cash) {
        this.is_cash = is_cash;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDiamond() {
        return diamond;
    }

    public void setDiamond(double diamond) {
        this.diamond = diamond;
    }

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        this.coin = coin;
    }

    public double getCheck() {
        return check;
    }

    public void setCheck(double check) {
        this.check = check;
    }

    public double getList_today_price() {
        return list_today_price;
    }

    public void setList_today_price(double list_today_price) {
        this.list_today_price = list_today_price;
    }

    public double getList_yes_price() {
        return list_yes_price;
    }

    public void setList_yes_price(double list_yes_price) {
        this.list_yes_price = list_yes_price;
    }

    public double getList_week_price() {
        return list_week_price;
    }

    public void setList_week_price(double list_week_price) {
        this.list_week_price = list_week_price;
    }

    public double getList_month_price() {
        return list_month_price;
    }

    public void setList_month_price(double list_month_price) {
        this.list_month_price = list_month_price;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
