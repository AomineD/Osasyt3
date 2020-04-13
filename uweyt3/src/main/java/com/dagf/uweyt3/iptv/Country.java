package com.dagf.uweyt3.iptv;

import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("name")
    private String name;

    @SerializedName("alpha2Code")
    private String alpha2Code;

    @SerializedName("capital")
    private String capital;

    @SerializedName("flag")
    private String flag;

    public Country() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlag() {
        //MessageFormat messageFormat = new MessageFormat(ApiClient.COUNTRY_FLAG_URL);
        //return messageFormat.format(alpha2Code);
        return String.format(com.dagf.uweyt3.iptv.ApiClient.COUNTRY_FLAG_URL, alpha2Code);

    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
