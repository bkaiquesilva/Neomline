package com.bkaiquesilva.nnzi.Adt;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bkaiquesilva.nnzi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyHolder {

    View mView;
    TextView off_on, user_name;
    CircleImageView perssone;
    CircleImageView fake_iv_card_profile;
    CircleImageView nova_mensagem;
    FirebaseAuth mAuth;
    LinearLayout reeeeee;


    public MyHolder(View itemView) {
        mView = itemView;

        nova_mensagem = itemView.findViewById(R.id.nova_mensagem);
        fake_iv_card_profile = itemView.findViewById(R.id.fake_iv_card_profile);
        perssone = itemView.findViewById(R.id.iv_card_profile);
        off_on = itemView.findViewById(R.id.status);
        user_name = itemView.findViewById(R.id.user_name);
        reeeeee = itemView.findViewById(R.id.reeeeee);

        mAuth = FirebaseAuth.getInstance();
    }
}

