package com.bkaiquesilva.nnzi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import org.json.JSONException;
import org.json.JSONObject;

public class Pendentesss extends AppCompatActivity {

    private TextView pd_nomase;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private CircleImageView user_pd;
    private boolean mAutori = false;
    private String MKfil;
    private String urlke;
    private String rome;
    private String uide;
    private String usidee;
    private TextView nv_soli;
    private TextView nv_negar;
    private TextView nv_aceitar;
    ProgressDialog dialog;
    private String mu_nme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendentes);

        Bundle extras = getIntent().getExtras();
        rome = extras.getString("y_nome");
        MKfil = extras.getString("y_image");
        uide = extras.getString("y_uide");
        urlke = extras.getString("y_keys");

        pd_nomase = findViewById(R.id.pd_nomase);
        user_pd = findViewById(R.id.user_pd);

        nv_soli = findViewById(R.id.nv_soli);
        nv_negar = findViewById(R.id.nv_negar);
        nv_aceitar = findViewById(R.id.nv_aceitar);
        dialog = new ProgressDialog(Pendentesss.this);
        dialog.setMessage("Processando...");
        dialog.setCancelable(false);
        nv_negar.setEnabled(false);
        nv_aceitar.setEnabled(false);
        mAutori = true;

        Glide.with(Pendentesss.this)
                .load(MKfil)
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .into(user_pd);

        String trimed = rome.trim();
        int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

        if (palavras == 1) {
            pd_nomase.setText(rome + " (pendente)");
            nv_soli.setText(rome + " enviou um\n" +
                    "novo pedido");
        } else {
            StringTokenizer tokenizer = new StringTokenizer(rome, " ");
            String primeiro = tokenizer.nextToken();
            String segundo = tokenizer.nextToken();
            pd_nomase.setText(primeiro + " (pendente)");
            nv_soli.setText(primeiro + " enviou um\n" +
                    "novo pedido");
        }


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            usidee = user.getUid();
        mDatabase.child("Users").child(usidee).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAutori) {
                    mAutori = false;
                    if (dataSnapshot.child("name").exists()) {
                        String iname = dataSnapshot.child("name").getValue().toString();
                            String trimed = iname.trim();
                                int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                                if (palavras == 1) {
                                    mu_nme = iname;
                                    nv_aceitar.setEnabled(true);
                                    nv_negar.setEnabled(true);
                                } else {
                                    StringTokenizer tokenizer = new StringTokenizer(iname, " ");
                                    String primeiro = tokenizer.nextToken();
                                    String segundo = tokenizer.nextToken();
                                    mu_nme = primeiro;
                                    nv_aceitar.setEnabled(true);
                                    nv_negar.setEnabled(true);
                                }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


        nv_negar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nv_aceitar.setEnabled(false);
                nv_negar.setEnabled(false);
                dialog.show();
                    mDatabase.child("Users").child(usidee).child("Contatos").child(urlke).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                            Toast.makeText(Pendentesss.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    });
            }
        });

        nv_aceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nv_aceitar.setEnabled(false);
                nv_negar.setEnabled(false);
                dialog.show();
                    mDatabase.child("Users").child(usidee).child("Contatos").child(urlke).child("nova_msn").setValue("0").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            enviaNotificaee(mu_nme + " aceitou seu pedido");
                        }
                    });
            }
        });
    }



    private void enviaNotificaee(String mensagem) {
        String url = "https://onesignal.com/api/v1/notifications";

                                JSONObject jsonBody;
                                try {
                                    jsonBody = new JSONObject("{"
                                            +   "\"app_id\": \"" + Utils.IDAPP + "\","
                                            +   "\"include_external_user_ids\": [" + uide + "],"
                                            +   "\"channel_for_external_user_ids\": \"push\","
                                            +   "\"data\": {\"foo\": \"bar\"},"
                                            +   "\"contents\": {\"en\": \"" + mensagem + "\"}"
                                            + "}"
                                    );

                                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            dialog.dismiss();
                                            Toast.makeText(Pendentesss.this, "SUCESSO", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                           }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                           }
                                    })
                                    {
                                        @Override
                                        public Map<String, String> getHeaders() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("Authorization", "Basic MzYxYjZiMTItZjQ3ZS00ZjY3LWExN2EtZjUyMTNkMDM1N2Y0");
                                            params.put("Content-type", "application/json");
                                            return params;
                                        }
                                    };
                                    Volley.newRequestQueue(Pendentesss.this).add(jsonRequest);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
    }

    public void ovook(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent loginInten = new Intent(Pendentesss.this, MainActivity.class);
        startActivity(loginInten);
        finish();
        Pendentesss.this.overridePendingTransition(R.anim.volte, R.anim.volte_ii);
    }
}

