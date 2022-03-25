package com.bkaiquesilva.nnzi.Adt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.bkaiquesilva.nnzi.R;
import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class PicassoClient {

    public  static  void downloadimg2(Context c, String url, CircleImageView img, CircleImageView fake_iv_card_profile) {
        if (url!=null && url.length()>0) {
            Glide.with(c)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.color.grey_20)
                    .into(img);
            img.setVisibility(View.VISIBLE);
            fake_iv_card_profile.setVisibility(View.GONE);
        }
    }
}
