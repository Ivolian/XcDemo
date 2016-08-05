package com.unicorn.csp.xcdemo.equipment;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.fragment.shared.base.LazyLoadFragment;

import butterknife.BindView;


public class PrincipalFragment extends LazyLoadFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_equipment_principal;
    }

    @BindView(R.id.tv_telephone)
    TextView tvTelephone;


    @Override
    public void initViews() {

        addDialLink("13611804437", "13611804437", tvTelephone);

    }

    private void addDialLink(String linkText, final String telephone, TextView tvTarget) {
        Link link = new Link(linkText)
                .setTextColor(getResources().getColor(R.color.primary))
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Uri uri = Uri.parse("tel:" + telephone);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    }
                });
        LinkBuilder.on(tvTarget)
                .addLink(link)
                .build();
    }
}
