package com.tranxit.enterprise.ui.bottomsheetdialog.invoice_show;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tranxit.enterprise.common.SharedHelper;
import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseBottomSheetDialogFragment;
import com.tranxit.enterprise.data.network.model.Payment;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tranxit.enterprise.base.BaseActivity.DATUM_history_detail;


public class InvoiceShowDialogFragment extends BaseBottomSheetDialogFragment implements InvoiceShowDialogIView {

    @BindView(R.id.btnClose)
    Button btnClose;
    Unbinder unbinder;
    @BindView(R.id.lblBookingid)
    TextView lblBookingid;
    @BindView(R.id.lblTotal)
    TextView lblTotal;
    @BindView(R.id.customer_payable_amount)
    TextView customerPayableAmount;
    @BindView(R.id.provider_earnings)
    TextView providerEarnings;

    NumberFormat numberFormat = getNumberFormat();
    private StringBuilder stringBuilder;

    public InvoiceShowDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_invoice_show_dialog;
    }

    @Override
    public void initView(View view) {
        stringBuilder = new StringBuilder(SharedHelper.getKey(getActivity(),
                SharedHelper.CURRENCY));
        unbinder = ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        if (DATUM_history_detail != null) {
            lblBookingid.setText(DATUM_history_detail.getBookingId());

            Payment payment = DATUM_history_detail.getPayment();
            if (payment != null) {
                lblTotal.setText(getFareValue(payment.getPayable()));
                customerPayableAmount.setText(getFareValue(payment.getPayable()));
                providerEarnings.setText(getFareValue(payment.getProviderPay()));
            }
        }
    }

    private String getFareValue(Object value) {
        if (SharedHelper.getKey(getActivity(), "currency_code", null) == null) {
            return stringBuilder + numberFormat.format(value).substring(1);
        } else {
            return numberFormat.format(value);
        }
    }

    @OnClick(R.id.btnClose)
    public void onViewClicked() {
        dismissAllowingStateLoss();
    }

    @Override
    public void onError(Throwable e) {

    }
}
