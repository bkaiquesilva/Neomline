package com.bkaiquesilva.nnzi;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class CarregarImagem {

    public  static  void downloadimg2(Context c, String url, ImageView img) {
        if (url!=null && url.length()>0) {
            Glide.with(c)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.color.grey_20)
                    .into(img);
        }
    }
}
