package com.tranxit.enterprise.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request_ {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("current_provider_id")
    @Expose
    private Integer currentProviderId;
    @SerializedName("service_type_id")
    @Expose
    private Integer serviceTypeId;
    @SerializedName("rental_hours")
    @Expose
    private Object rentalHours;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cancelled_by")
    @Expose
    private String cancelledBy;
    @SerializedName("cancel_reason")
    @Expose
    private Object cancelReason;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("paid")
    @Expose
    private Integer paid;
    @SerializedName("is_track")
    @Expose
    private String isTrack;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("travel_time")
    @Expose
    private Object travelTime;
    @SerializedName("s_address")
    @Expose
    private String sAddress;
    @SerializedName("s_latitude")
    @Expose
    private Double sLatitude;
    @SerializedName("s_longitude")
    @Expose
    private Double sLongitude;
    @SerializedName("d_address")
    @Expose
    private String dAddress;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("d_latitude")
    @Expose
    private Double dLatitude;
    @SerializedName("track_distance")
    @Expose
    private Double trackDistance;
    @SerializedName("track_latitude")
    @Expose
    private Double trackLatitude;
    @SerializedName("track_longitude")
    @Expose
    private Double trackLongitude;
    @SerializedName("d_longitude")
    @Expose
    private Double dLongitude;
    @SerializedName("assigned_at")
    @Expose
    private String assignedAt;
    @SerializedName("schedule_at")
    @Expose
    private Object scheduleAt;
    @SerializedName("started_at")
    @Expose
    private String startedAt;
    @SerializedName("finished_at")
    @Expose
    private String finishedAt;
    @SerializedName("user_rated")
    @Expose
    private Integer userRated;
    @SerializedName("provider_rated")
    @Expose
    private Integer providerRated;
    @SerializedName("use_wallet")
    @Expose
    private Integer useWallet;
    @SerializedName("surge")
    @Expose
    private Integer surge;
    @SerializedName("route_key")
    @Expose
    private String routeKey;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private UserGetResponse user;
    @SerializedName("payment")
    @Expose
    private Payment payment;
    @SerializedName("service_required")
    @Expose
    private String serviceRequired;

    @SerializedName("service_type")
    @Expose
    private ServiceType servicetype;
    @SerializedName("rental_package")
    @Expose
    private RentalHourPackage rentalPackage;
    @SerializedName("day")
    @Expose
    private String day;


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public RentalHourPackage getRentalPackage() {
        return rentalPackage;
    }

    public void setRentalPackage(RentalHourPackage rentalPackage) {
        this.rentalPackage = rentalPackage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getCurrentProviderId() {
        return currentProviderId;
    }

    public void setCurrentProviderId(Integer currentProviderId) {
        this.currentProviderId = currentProviderId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Object getRentalHours() {
        return rentalHours;
    }

    public void setRentalHours(Object rentalHours) {
        this.rentalHours = rentalHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public String getIsTrack() {
        return isTrack;
    }

    public void setIsTrack(String isTrack) {
        this.isTrack = isTrack;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Object getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Object travelTime) {
        this.travelTime = travelTime;
    }

    public String getSAddress() {
        return sAddress;
    }

    public void setSAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public Double getSLatitude() {
        return sLatitude;
    }

    public void setSLatitude(Double sLatitude) {
        this.sLatitude = sLatitude;
    }

    public Double getSLongitude() {
        return sLongitude;
    }

    public void setSLongitude(Double sLongitude) {
        this.sLongitude = sLongitude;
    }

    public String getDAddress() {
        return dAddress;
    }

    public void setDAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Double getDLatitude() {
        return dLatitude;
    }

    public void setDLatitude(Double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public Double getTrackDistance() {
        return trackDistance;
    }

    public void setTrackDistance(Double trackDistance) {
        this.trackDistance = trackDistance;
    }

    public Double getTrackLatitude() {
        return trackLatitude;
    }

    public void setTrackLatitude(Double trackLatitude) {
        this.trackLatitude = trackLatitude;
    }

    public Double getTrackLongitude() {
        return trackLongitude;
    }

    public void setTrackLongitude(Double trackLongitude) {
        this.trackLongitude = trackLongitude;
    }

    public Double getDLongitude() {
        return dLongitude;
    }

    public void setDLongitude(Double dLongitude) {
        this.dLongitude = dLongitude;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Object getScheduleAt() {
        return scheduleAt;
    }

    public void setScheduleAt(Object scheduleAt) {
        this.scheduleAt = scheduleAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Integer getUserRated() {
        return userRated;
    }

    public void setUserRated(Integer userRated) {
        this.userRated = userRated;
    }

    public Integer getProviderRated() {
        return providerRated;
    }

    public void setProviderRated(Integer providerRated) {
        this.providerRated = providerRated;
    }

    public Integer getUseWallet() {
        return useWallet;
    }

    public void setUseWallet(Integer useWallet) {
        this.useWallet = useWallet;
    }

    public Integer getSurge() {
        return surge;
    }

    public void setSurge(Integer surge) {
        this.surge = surge;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserGetResponse getUser() {
        return user;
    }

    public void setUser(UserGetResponse user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public ServiceType getServicetype() {
        return servicetype;
    }

    public void setServicetype(ServiceType servicetype) {
        this.servicetype = servicetype;
    }

    public String getServiceRequired() {
        if (serviceRequired == null) {
            return "";
        } else {
            return serviceRequired;
        }
    }

    public void setServiceRequired(String serviceRequired) {
        this.serviceRequired = serviceRequired;
    }
}
