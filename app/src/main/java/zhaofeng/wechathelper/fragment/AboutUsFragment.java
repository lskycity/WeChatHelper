package zhaofeng.wechathelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zhaofeng.wechathelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements View.OnClickListener
{
    private TextView mMailLink;

    public AboutUsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        getActivity().setTitle(R.string.about_us);
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mMailLink = (TextView) view.findViewById(R.id.mail_link);
        mMailLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.mail_link:
                sendMail(getString(R.string.team_mail_address));
                break;
        }
    }

    private void sendMail(String address)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_content));

        Context context = getActivity();

        PackageManager pManager = context.getPackageManager();
        List<ResolveInfo> info = pManager.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if(info!=null)
        {
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.mail_chooser_title)));
        }
    }
}
