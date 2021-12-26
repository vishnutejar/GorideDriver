package com.tranxit.enterprise.ui.bottomsheetdialog.invoice_flow;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tranxit.enterprise.common.SharedHelper;
import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseBottomSheetDialogFragment;
import com.tranxit.enterprise.data.network.model.Payment;
import com.tranxit.enterprise.data.network.model.RentalHourPackage;
import com.tranxit.enterprise.data.network.model.Request_;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tranxit.enterprise.base.BaseActivity.DATUM;

public class InvoiceDialogFragment extends BaseBottomSheetDialogFragment implements InvoiceDialogIView {

    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.payable_amount)
    TextView payableAmount;
    @BindView(R.id.payment_mode_img)
    ImageView paymentModeImg;
    @BindView(R.id.payment_mode_layout)
    LinearLayout paymentModeLayout;
    @BindView(R.id.btnConfirmPayment)
    Button btnConfirmPayment;
    @BindView(R.id.lblPaymentType)
    TextView lblPaymentType;
    @BindView(R.id.total_distance)
    TextView totalDistance;
    @BindView(R.id.travel_time)
    TextView travelTime;
    @BindView(R.id.fixed)
    TextView fixed;
    @BindView(R.id.distance_fare)
    TextView distanceFare;
    @BindView(R.id.peek_hour_charges)
    TextView peekHourCharges;
    @BindView(R.id.night_fare)
    TextView nightFare;
    @BindView(R.id.tax)
    TextView tax;
    @BindView(R.id.tax2)
    TextView tax2;
    @BindView(R.id.commission)
    TextView commission;
    @BindView(R.id.tds)
    TextView tds;
    @BindView(R.id.provider_earnings)
    TextView providerEarnings;

    @BindView(R.id.layout_normal_flow)
    LinearLayout layout_normal_flow;
    @BindView(R.id.layout_rental_flow)
    LinearLayout layout_rental_flow;
    @BindView(R.id.rental_normal_price)
    TextView rentalNormalPrice;
    @BindView(R.id.layout_outstation_flow)
    LinearLayout layout_outstation_flow;
    @BindView(R.id.outstation_distance_travelled)
    TextView outstationDistanceTravelled;
    @BindView(R.id.outstation_distance_fare)
    TextView outstationDistanceFare;
    @BindView(R.id.outstation_driver_beta)
    TextView outstationDriverBeta;
    @BindView(R.id.start_date)
    TextView startDate;

    @BindView(R.id.outstation_round_single)
    TextView outstationRoundSingle;
    @BindView(R.id.outstation_no_of_days)
    TextView outstationNoOfDays;

    @BindView(R.id.rental_total_distance_km)
    TextView rentalTotalDistance;

    @BindView(R.id.rental_extra_hr_km_price)
    TextView rentalExtraHrKmPrice;

    @BindView(R.id.rental_travel_time)
    TextView rentalTravelTime;

    @BindView(R.id.rental_hours)
    TextView rentalHours;
    @BindView(R.id.end_date)
    TextView endDate;
    InvoiceDialogPresenter<InvoiceDialogFragment> presenter = new InvoiceDialogPresenter<>();
    NumberFormat numberFormat = getNumberFormat();
    private StringBuilder stringBuilder;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invoice_dialog;
    }

    String convertDateFormat(String date) {
        String newDateString = null;
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        try {
            if(date!=null) {
                Date newDate = spf.parse(date);
                newDateString = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(newDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDateString;
    }

    @Override
    public void initView(View view) {
        stringBuilder = new StringBuilder(SharedHelper.getKey(getActivity(),
                SharedHelper.CURRENCY));
        setCancelable(false);
        getDialog().setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            View bottomSheetInternal = d.findViewById(android.support.design.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        getDialog().setCanceledOnTouchOutside(false);
        ButterKnife.bind(this, view);
        presenter.attachView(this);

        Request_ datum = DATUM;
        if (datum != null) {

            bookingId.setText(datum.getBookingId());
            startDate.setText(convertDateFormat(datum.getStartedAt()));
            endDate.setText(convertDateFormat(datum.getFinishedAt()));
            totalDistance.setText(String.valueOf(datum.getDistance() + " km"));
            travelTime.setText(getString(R.string._min, datum.getTravelTime()));

            Payment payment = datum.getPayment();
            if (payment != null) {
                fixed.setText(getFareValue(payment.getFixed()));
                Double total = payment.getTotal() - payment.getTax();
                totalAmount.setText(getFareValue(total));
                peekHourCharges.setText(getFareValue(payment.getPeakPrice()));
                payableAmount.setText(getFareValue(payment.getPayable()));
                distanceFare.setText(getFareValue(payment.getDistance()));
                tax.setText(getFareValue(payment.getTax()));
                tax2.setText(getFareValue(payment.getTax()));
                nightFare.setText(getFareValue(payment.getNightFare()));
                commission.setText(getFareValue(payment.getProviderCommission()));
                providerEarnings.setText(getFareValue(payment.getProviderPay()));
                //rental
                RentalHourPackage rentalHourPackage = datum.getRentalPackage();
                if (rentalHourPackage != null) {
                    rentalHours.setText(String.format("%s (%s Hrs)", getString(R.string.rental_normal_price), rentalHourPackage.getHour()));
                }
                rentalNormalPrice.setText(getFareValue(payment.getMinute()));
                rentalTravelTime.setText(getString(R.string._min, datum.getTravelTime()));
                rentalTotalDistance.setText(String.valueOf(datum.getDistance() + " km"));
                rentalExtraHrKmPrice.setText(getFareValue(payment.getRentalExtraHrPrice() + payment.getRentalExtraKmPrice()));

                //outstation
//                outstationDriverBeta.setText(String.format("(%s Days) %s", payment.getOutstationDays(), getFareValue(payment.getDriverBeta())));
                outstationDriverBeta.setText(String.format(getFareValue(payment.getDriverBeta())));
                outstationDistanceFare.setText(getFareValue(payment.getDistance()));
                outstationDistanceTravelled.setText(String.valueOf(datum.getDistance() + " km"));

                outstationRoundSingle.setText(datum.getDay());
//                outstationNoOfDays.setText(String.format("%s Days", payment.getOutstationDays()));
                String serviceReq = datum.getServiceRequired();
                switch (serviceReq) {
                    case "none":
                        layout_normal_flow.setVisibility(View.VISIBLE);
                        break;
                    case "rental":
                        layout_rental_flow.setVisibility(View.VISIBLE);
                        break;
                    case "outstation":
                        layout_outstation_flow.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

        }

    }


    @OnClick(R.id.btnConfirmPayment)
    public void onViewClicked() {

        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", "COMPLETED");
            map.put("_method", "PATCH");
            showLoading();
            presenter.statusUpdate(map, datum.getId());

        }

    }

    private String getFareValue(Object value) {
        if (SharedHelper.getKey(getActivity(), "currency_code", null) == null) {
            return stringBuilder + numberFormat.format(value).substring(1);
        } else {
            return numberFormat.format(value);
        }
    }

    @Override
    public void onSuccess(Object object) {

        dismissAllowingStateLoss();
        hideLoading();

        Intent intent = new Intent("INTENT_FILTER");
        activity().sendBroadcast(intent);

    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
    }


}
