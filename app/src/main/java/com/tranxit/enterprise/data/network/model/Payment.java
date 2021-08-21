package com.tranxit.enterprise.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("request_id")
    @Expose
    private Integer requestId;
    @SerializedName("promocode_id")
    @Expose
    private Object promocodeId;
    @SerializedName("payment_id")
    @Expose
    private Object paymentId;
    @SerializedName("payment_mode")
    @Expose
    private Object paymentMode;
    @SerializedName("fixed")
    @Expose
    private Double fixed;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("eta_discount")
    @Expose
    private Double etaDiscount;
    @SerializedName("commision")
    @Expose
    private Double commision;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("wallet")
    @Expose
    private Double wallet;
    @SerializedName("surge")
    @Expose
    private Double surge;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("payable")
    @Expose
    private Double payable;
    @SerializedName("waiting_fare")
    @Expose
    private Double waitingFare;
    @SerializedName("tip")
    @Expose
    private Double tip;
    @SerializedName("provider_commission")
    @Expose
    private Double providerCommission;
    @SerializedName("provider_pay")
    @Expose
    private Double providerPay;
    @SerializedName("night_fare")
    @Expose
    private Double nightFare;
    @SerializedName("peak_price")
    @Expose
    private Double peakPrice;
    @SerializedName("driver_beta")
    @Expose
    private Double driverBeta;
    @SerializedName("rental_extra_hr_price")
    @Expose
    private double rentalExtraHrPrice;
    @SerializedName("rental_extra_km_price")
    @Expose
    private double rentalExtraKmPrice;
    @SerializedName("outstation_days")
    @Expose
    private String outstationDays;
    @SerializedName("minute")
    @Expose
    private Double minute;

    public double getRentalExtraHrPrice() {
        return rentalExtraHrPrice;
    }

    public void setRentalExtraHrPrice(double rentalExtraHrPrice) {
        this.rentalExtraHrPrice = rentalExtraHrPrice;
    }

    public double getRentalExtraKmPrice() {
        return rentalExtraKmPrice;
    }

    public void setRentalExtraKmPrice(double rentalExtraKmPrice) {
        this.rentalExtraKmPrice = rentalExtraKmPrice;
    }

    public String getOutstationDays() {
        return outstationDays;
    }

    public void setOutstationDays(String outstationDays) {
        this.outstationDays = outstationDays;
    }

    public Double getMinute() {
        return minute;
    }

    public void setMinute(Double minute) {
        this.minute = minute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Object getPromocodeId() {
        return promocodeId;
    }

    public void setPromocodeId(Object promocodeId) {
        this.promocodeId = promocodeId;
    }

    public Object getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Object paymentId) {
        this.paymentId = paymentId;
    }

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Double getFixed() {
        return fixed;
    }

    public void setFixed(Double fixed) {
        this.fixed = fixed;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getEtaDiscount() {
        return etaDiscount;
    }

    public void setEtaDiscount(Double etaDiscount) {
        this.etaDiscount = etaDiscount;
    }

    public Double getCommision() {
        return commision;
    }

    public void setCommision(Double commision) {
        this.commision = commision;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    public Double getSurge() {
        return surge;
    }

    public void setSurge(Double surge) {
        this.surge = surge;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPayable() {
        return payable;
    }

    public void setPayable(Double payable) {
        this.payable = payable;
    }

    public Double getWaitingFare() {
        return waitingFare;
    }

    public void setWaitingFare(Double waitingFare) {
        this.waitingFare = waitingFare;
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public Double getProviderCommission() {
        return providerCommission;
    }

    public void setProviderCommission(Double providerCommission) {
        this.providerCommission = providerCommission;
    }

    public Double getProviderPay() {
        return providerPay;
    }

    public void setProviderPay(Double providerPay) {
        this.providerPay = providerPay;
    }

    public Double getNightFare() {
        return nightFare;
    }

    public void setNightFare(Double nightFare) {
        this.nightFare = nightFare;
    }


    public Double getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(Double peakPrice) {
        this.peakPrice = peakPrice;
    }

    public Double getDriverBeta() {
        return driverBeta;
    }

    public void setDriverBeta(Double driverBeta) {
        this.driverBeta = driverBeta;
    }
}
