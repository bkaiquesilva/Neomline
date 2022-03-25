package com.bkaiquesilva.nnzi;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilContato extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CircleImageView eperfil;
    private TextView enome;
    private TextView eaniver;
    private String perfill;
    private String iuide;
    private ProgressDialog mProgress;
    private boolean uii = false;
    private String urlke;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_contato);

        Bundle extras = getIntent().getExtras();
        iuide = extras.getString("euide");
        perfill = extras.getString("y_image");
        urlke = extras.getString("y_keys");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        eperfil = findViewById(R.id.dth_perfil);
        enome = findViewById(R.id.dth_nome);
        eaniver = findViewById(R.id.dth_aniversario);
        mProgress = new ProgressDialog(this);


        Glide.with(PerfilContato.this)
                .load(perfill)
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .into(eperfil);


        eperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img_bu;
                final Dialog dialog = new Dialog(PerfilContato.this);
                dialog.setContentView(R.layout.auver);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                img_bu = dialog.findViewById(R.id.btimg);


                img_bu.setBackgroundResource(0);
                Picasso.get().load(perfill).into(img_bu);

                img_bu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.setCancelable(true);
                window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });

        eaniver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dee = eaniver.getText().toString().trim();
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", dee);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(PerfilContato.this, "NÚMERO COPIADO", Toast.LENGTH_SHORT).show();
            }
        });

        uii = true;

        mDatabase.child("Users").child(iuide).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (uii){
                    uii = false;
                    if (dataSnapshot.child("name").exists()) {
                        if (dataSnapshot.child("user_usuario").exists()) {
                               eaniver.setText(dataSnapshot.child("user_usuario").getValue().toString());
                                enome.setText(dataSnapshot.child("name").getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent loginInten = new Intent(PerfilContato.this, MainActivity.class);
        startActivity(loginInten);
        finish();
        PerfilContato.this.overridePendingTransition(R.anim.volte, R.anim.volte_ii);
    }

    public void detekei(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilContato.this);
        builder.setTitle("REMOSÃO");
        builder.setMessage("Você quer mesmo remover este contato?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    dialogInterface.dismiss();
                    mProgress.setMessage("Iniciando remosão.....");
                    mProgress.show();
                    String usidee = user.getUid();

                    mDatabase.child("Users").child(usidee).child("Contatos").child(urlke).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mProgress.dismiss();
                            Toast.makeText(PerfilContato.this, "REMOVIDO", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    });
                }else{
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog11 = builder.create();
        alertDialog11.setCanceledOnTouchOutside(false);
        alertDialog11.show();
    }
}