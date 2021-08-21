package com.tranxit.enterprise.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPResponse{

	@SerializedName("otp")
	private int otp;
	@SerializedName("estimated_fare")
	private Double estimatedFare;

	@SerializedName("user")
	private User user;

	public void setOtp(int otp){
		this.otp = otp;
	}

	public int getOtp(){
		return otp;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public Double getEstimatedFare() {
		return estimatedFare;
	}

	public void setEstimatedFare(Double estimatedFare) {
		this.estimatedFare = estimatedFare;
	}


	public class User {

		@SerializedName("id")
		@Expose
		private Integer id;
		@SerializedName("first_name")
		@Expose
		private String firstName;
		@SerializedName("last_name")
		@Expose
		private String lastName;
		@SerializedName("payment_mode")
		@Expose
		private String paymentMode;
		@SerializedName("email")
		@Expose
		private Object email;
		@SerializedName("gender")
		@Expose
		private String gender;
		@SerializedName("mobile")
		@Expose
		private String mobile;
		@SerializedName("country_code")
		@Expose
		private String countryCode;
		@SerializedName("picture")
		@Expose
		private Object picture;
		@SerializedName("device_token")
		@Expose
		private String deviceToken;
		@SerializedName("device_id")
		@Expose
		private String deviceId;
		@SerializedName("device_type")
		@Expose
		private String deviceType;
		@SerializedName("login_by")
		@Expose
		private String loginBy;
		@SerializedName("social_unique_id")
		@Expose
		private Object socialUniqueId;
		@SerializedName("latitude")
		@Expose
		private Object latitude;
		@SerializedName("longitude")
		@Expose
		private Object longitude;
		@SerializedName("stripe_cust_id")
		@Expose
		private Object stripeCustId;
		@SerializedName("wallet_balance")
		@Expose
		private Integer walletBalance;
		@SerializedName("rating")
		@Expose
		private String rating;
		@SerializedName("otp")
		@Expose
		private Integer otp;
		@SerializedName("updated_at")
		@Expose
		private String updatedAt;
		@SerializedName("emergency_contact1")
		@Expose
		private String emergencyContact1;
		@SerializedName("emergency_contact2")
		@Expose
		private String emergencyContact2;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getPaymentMode() {
			return paymentMode;
		}

		public void setPaymentMode(String paymentMode) {
			this.paymentMode = paymentMode;
		}

		public Object getEmail() {
			return email;
		}

		public void setEmail(Object email) {
			this.email = email;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}

		public Object getPicture() {
			return picture;
		}

		public void setPicture(Object picture) {
			this.picture = picture;
		}

		public String getDeviceToken() {
			return deviceToken;
		}

		public void setDeviceToken(String deviceToken) {
			this.deviceToken = deviceToken;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}

		public String getLoginBy() {
			return loginBy;
		}

		public void setLoginBy(String loginBy) {
			this.loginBy = loginBy;
		}

		public Object getSocialUniqueId() {
			return socialUniqueId;
		}

		public void setSocialUniqueId(Object socialUniqueId) {
			this.socialUniqueId = socialUniqueId;
		}

		public Object getLatitude() {
			return latitude;
		}

		public void setLatitude(Object latitude) {
			this.latitude = latitude;
		}

		public Object getLongitude() {
			return longitude;
		}

		public void setLongitude(Object longitude) {
			this.longitude = longitude;
		}

		public Object getStripeCustId() {
			return stripeCustId;
		}

		public void setStripeCustId(Object stripeCustId) {
			this.stripeCustId = stripeCustId;
		}

		public Integer getWalletBalance() {
			return walletBalance;
		}

		public void setWalletBalance(Integer walletBalance) {
			this.walletBalance = walletBalance;
		}

		public String getRating() {
			return rating;
		}

		public void setRating(String rating) {
			this.rating = rating;
		}

		public Integer getOtp() {
			return otp;
		}

		public void setOtp(Integer otp) {
			this.otp = otp;
		}

		public String getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(String updatedAt) {
			this.updatedAt = updatedAt;
		}

		public String getEmergencyContact1() {
			return emergencyContact1;
		}

		public void setEmergencyContact1(String emergencyContact1) {
			this.emergencyContact1 = emergencyContact1;
		}

		public String getEmergencyContact2() {
			return emergencyContact2;
		}

		public void setEmergencyContact2(String emergencyContact2) {
			this.emergencyContact2 = emergencyContact2;
		}
	}
}

