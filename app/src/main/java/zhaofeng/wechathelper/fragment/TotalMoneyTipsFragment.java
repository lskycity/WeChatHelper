package zhaofeng.wechathelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import zhaofeng.wechathelper.R;

/**
 * Created by kevinbest on 16/1/31.
 */
public class TotalMoneyTipsFragment extends DialogFragment {

    public static final String TOTAL_MONEY_AMOUNT = "total_money";
    private float total_money=0.0f;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            total_money = bundle.getFloat(TOTAL_MONEY_AMOUNT);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.total_money_tip,container,false);
        TextView tip = (TextView)view.findViewById(R.id.tips);
        tip.setText("WeChatHelper has collected " + String.format("%.2f",total_money) + " 元 for you.");
        return view;
    }
}
