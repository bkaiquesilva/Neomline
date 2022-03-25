package com.bkaiquesilva.nnzi;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bkaiquesilva.nnzi.Permissao.EasyPermissionList;
import com.bumptech.glide.Glide;
import com.eyalbira.loadingdots.LoadingDots;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import com.bkaiquesilva.nnzi.Playaudio.VoicePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {

    private TextView nomase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private RecyclerView blog_list;
    private static final int GALLERY_REQUEST = 999;
    private FirebaseAuth mAuth;
    private EditText new_message;
    private ImageView sendy, audy;
    LinearLayoutManager layoutManager;
    private boolean mAutori = false;
    private String MKfil;
    private String urlke;
    private String isin;
    private String rome;
    private String uide;
    private String usidee;
    private String meu_identifico;
    private String meu_identifica___seu_identifica;
    private boolean ichee1 = false;
    private boolean ichee2 = false;
    private boolean zoomm2 = false;
    private boolean eifiiu = false;
    private int onli = 3;
    private int botii = 0;
    private LoadingDots looeee;
    private String eideee;
    private String bi_nome;
    GravarAudio gravarAudio;
    Context mcon;
    private boolean mAutori6789 = false;
    private int avisooo = 0;
    private int npostagem = 5;
    private boolean baixaaa = false;
    private boolean deletaa = false;
    private SwipeRefreshLayout activity_main;
    private RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();
        rome = extras.getString("y_nome");
        MKfil = extras.getString("y_image");
        uide = extras.getString("y_uide");
        urlke = extras.getString("y_keys");
        isin = extras.getString("y_identifica");
        meu_identifico = extras.getString("y_meu_identifica");

        nomase = findViewById(R.id.nomase);
        looeee = findViewById(R.id.looeee);
        blog_list = findViewById(R.id.recycler_view);
        new_message = findViewById(R.id.new_message);
        activity_main = findViewById(R.id.bybby_main);
        sendy = findViewById(R.id.sendy);
        audy = findViewById(R.id.audy);
        sendy.setEnabled(false);
        audy.setEnabled(false);
        eifiiu = true;
        progressBar = findViewById(R.id.progress);

        mcon = ChatActivity.this;

        String trimed = rome.trim();
        int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

        if (palavras == 1){
            nomase.setText(rome + " (offline)");
        }else {
            StringTokenizer tokenizer = new StringTokenizer(rome, " ");
            String primeiro = tokenizer.nextToken();
            String segundo = tokenizer.nextToken();
            nomase.setText(primeiro + " (offline)");
        }

        zoomm2 = true;

        activity_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAuth.removeAuthStateListener(mAuthListener);
                int abve = npostagem + 5;
                npostagem = abve;
                chequeusuario();
                mAuth.addAuthStateListener(mAuthListener);
            }
        });


        audy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ChatActivity.this, EasyPermissionList.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChatActivity.this, "ERRO. PERMISSÃO NEGADA", Toast.LENGTH_SHORT).show();
                } else {
                    String[] minetipos = {"image/jpeg", "image/png"};
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, minetipos);
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

        new_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence,int start, int before, int count) {
                if (count == 1) {
                    sendy.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this, R.drawable.ic_send));
                    botii = 1;
                } else if (count == 0) {
                    sendy.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this, R.drawable.ic_round_mic_24));
                    botii = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase.keepSynced(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        blog_list.setHasFixedSize(true);
        blog_list.setLayoutManager(layoutManager);

        mDatabase.child("Users").child(uide).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (zoomm2) {
                    zoomm2 = false;
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        usidee = user.getUid();
                        if (dataSnapshot.child("image").exists()) {
                            String imaage = dataSnapshot.child("image").getValue().toString();
                            if (imaage.equals(MKfil)) {
                            } else {
                                mDatabase.child("Users").child(usidee).child("Contatos").child(urlke).child("IMAGE").setValue(imaage);
                            }
                        }else{
                            MKfil = "https://firebasestorage.googleapis.com/v0/b/nnzi-230b7.appspot.com/o/padrao%2Fuser%20(1).png?alt=media&token=58aa28e9-b3a6-46d9-9dcc-1487dd859978";
                        }

                        if (dataSnapshot.child("name").exists()) {
                            String iname = dataSnapshot.child("name").getValue().toString();
                            if (iname.equals(rome)) {
                            } else {
                                rome = iname;
                                mDatabase.child("Users").child(usidee).child("Contatos").child(urlke).child("enome").setValue(iname);
                                if (onli == 1){
                                    String trimed = iname.trim();
                                    int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                                    if (palavras == 1){
                                        nomase.setText(iname + " (offline)");
                                    }else {
                                        StringTokenizer tokenizer = new StringTokenizer(iname, " ");
                                        String primeiro = tokenizer.nextToken();
                                        String segundo = tokenizer.nextToken();
                                        nomase.setText(primeiro + " (offline)");
                                    }
                                }else if (onli == 2){
                                    String trimed = iname.trim();
                                    int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                                    if (palavras == 1){
                                        nomase.setText(iname + " (online)");
                                    }else {
                                        StringTokenizer tokenizer = new StringTokenizer(iname, " ");
                                        String primeiro = tokenizer.nextToken();
                                        String segundo = tokenizer.nextToken();
                                        nomase.setText(primeiro + " (online)");
                                    }
                                }else if (onli == 3){
                                    String trimed = iname.trim();
                                    int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                                    if (palavras == 1){
                                        nomase.setText(iname + " (offline)");
                                    }else {
                                        StringTokenizer tokenizer = new StringTokenizer(iname, " ");
                                        String primeiro = tokenizer.nextToken();
                                        String segundo = tokenizer.nextToken();
                                        nomase.setText(primeiro + " (offline)");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        new_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND){
                    envioa();
                    handled = true;
                }
                return handled;
            }
        });

        EditText new_message_fk = findViewById(R.id.new_message_fk);
        sendy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (botii == 1) {

                }else if(botii == 0){
                    if (ContextCompat.checkSelfPermission(ChatActivity.this, EasyPermissionList.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ChatActivity.this, "ERRO. PERMISSÃO NEGADA", Toast.LENGTH_SHORT).show();
                    } else {
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        new_message.setVisibility(View.GONE);
                        new_message_fk.setVisibility(View.VISIBLE);
                        audy.setEnabled(false);
                        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
                        gravarAudio = new GravarAudio(outputFile, mcon, meu_identifica___seu_identifica, uide, urlke);
                        gravarAudio.start();

                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        new_message.setVisibility(View.VISIBLE);
                        new_message_fk.setVisibility(View.GONE);
                        audy.setEnabled(true);
                        gravarAudio.stop();
                        mAutori6789 = true;
                        Uri uri = FileProvider.getUriForFile(ChatActivity.this,
                                "com.bkaiquesilva.nnzi.provider", new File(gravarAudio.getAudioFilePath()));
                        StorageReference filepath = mStorageRef.child("Chat_Audios").child(uri.getLastPathSegment());
                        UploadTask uploadTask = filepath.putFile(uri);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return filepath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    if (mAutori6789) {
                                        mAutori6789 = false;
                                        Uri downloadUrl = task.getResult();
                                        String datae = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                                        final DatabaseReference newPost = mDatabase.child("Batepapo").child(meu_identifica___seu_identifica).push();
                                        newPost.child("mident").setValue(uide);
                                        newPost.child("dataa").setValue(datae);
                                        newPost.child("typo").setValue("3");
                                        newPost.child("visto").setValue("0");
                                        newPost.child("audio").setValue(downloadUrl.toString());
                                        newPost.child("imagem").setValue("https://images.jpeg");
                                        newPost.child("menssa").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                MediaPlayer mp = MediaPlayer.create(ChatActivity.this, R.raw.sound);
                                                try {
                                                    mp.prepare();
                                                } catch (IllegalStateException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                mp.start();
                                                gravarAudio.clear();

                                                if (onli == 1){
                                                    if (avisooo == 0) {
                                                        avisooo = 66;

                                                        if (bi_nome != null){
                                                            enviaNotificaee(bi_nome + " enviou uma nova mensagem");
                                                        }
                                                    }
                                                    mDatabase.child("Users").child(uide).child("Contatos").child(urlke).child("nova_msn").setValue("1");
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(ChatActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                    }
                return false;
            }
        });

        sendy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (botii == 1) {
                    envioa();
                }else if(botii == 0){

                }
            }
        });

        chequeusuario();
    }

    private void envioa() {
        final String msn = new_message.getText().toString().trim();
        if (!msn.isEmpty()) {

                MediaPlayer mp = MediaPlayer.create(ChatActivity.this, R.raw.sound);
                try {
                    mp.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();

            new_message.getText().clear();
            sendy.setEnabled(false);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String datae = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                    final DatabaseReference newPost = mDatabase.child("Batepapo").child(meu_identifica___seu_identifica).push();
                    newPost.child("mident").setValue(uide);
                    newPost.child("dataa").setValue(datae);
                    newPost.child("imagem").setValue("https://images.jpeg");
                    newPost.child("audio").setValue("https://audio.mp3");
                    newPost.child("typo").setValue("1");
                    newPost.child("visto").setValue("0");
                    newPost.child("menssa").setValue(msn).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendy.setEnabled(true);
                            if (onli == 1) {
                                if (avisooo == 0) {
                                    avisooo = 66;

                                    if (bi_nome != null){
                                        enviaNotificaee(bi_nome + " enviou uma nova mensagem");
                                    }
                                }
                                mDatabase.child("Users").child(uide).child("Contatos").child(urlke).child("nova_msn").setValue("1");
                            }
                        }
                    });
                } else {
                }

        }else {

        }
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
                                    Volley.newRequestQueue(ChatActivity.this).add(jsonRequest);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
    }


    private void chequeusuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            eideee = user.getUid();
            ichee1 = true;
            ichee2 = true;
            String m1 = isin + meu_identifico;
            String m2 = meu_identifico + isin;
            String meu_1 = m1.replaceAll("[^0-9]", "");
            String meu_2 = m2.replaceAll("[^0-9]", "");


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (ichee1) {
                        ichee1 = false;
                        if (dataSnapshot.child("Batepapo").exists()) {
                            mDatabase.child("Batepapo").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (ichee2) {
                                        ichee2 = false;
                                    if (dataSnapshot.child(meu_1).exists()) {
                                        meu_identifica___seu_identifica = meu_1;
                                        sendy.setEnabled(true);
                                        audy.setEnabled(true);
                                        mDatabase.child("Users").child(eideee).child("eOnline").setValue("2");
                                        mDatabase.child("Users").child(eideee).child("Contatos").child(urlke).child("nova_msn").setValue("0");
                                        testar(meu_identifica___seu_identifica, eideee);
                                    } else if (dataSnapshot.child(meu_2).exists()) {
                                        meu_identifica___seu_identifica = meu_2;
                                        sendy.setEnabled(true);
                                        audy.setEnabled(true);
                                        mDatabase.child("Users").child(eideee).child("eOnline").setValue("2");
                                        mDatabase.child("Users").child(eideee).child("Contatos").child(urlke).child("nova_msn").setValue("0");
                                        testar(meu_identifica___seu_identifica, eideee);
                                    } else {
                                        mDatabase.child("Users").child(eideee).child("eOnline").setValue("2");
                                        mDatabase.child("Users").child(eideee).child("Contatos").child(urlke).child("nova_msn").setValue("0");
                                        meu_identifica___seu_identifica = meu_1;
                                            String datae = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                                            final DatabaseReference newPost = mDatabase.child("Batepapo").child(meu_identifica___seu_identifica).push();
                                            newPost.child("mident").setValue("edebelzaakso");
                                            newPost.child("dataa").setValue(datae);
                                        newPost.child("audio").setValue("https://audio.mp3");
                                            newPost.child("imagem").setValue("https://images.jpeg");
                                            newPost.child("typo").setValue("1");
                                        newPost.child("visto").setValue("0");
                                            newPost.child("menssa").setValue("Diga oi para o seu novo contato.").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    sendy.setEnabled(true);
                                                    audy.setEnabled(true);
                                                    testar(meu_identifica___seu_identifica, eideee);
                                                }
                                            });
                                    }
                                }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else {
                            mDatabase.child("Users").child(eideee).child("eOnline").setValue("2");
                            mDatabase.child("Users").child(eideee).child("Contatos").child(urlke).child("nova_msn").setValue("0");
                            meu_identifica___seu_identifica = meu_1;
                            String datae = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                            final DatabaseReference newPost = mDatabase.child("Batepapo").child(meu_identifica___seu_identifica).push();
                            newPost.child("mident").setValue("edebelzaakso");
                            newPost.child("dataa").setValue(datae);
                            newPost.child("imagem").setValue("https://images.jpep");
                            newPost.child("audio").setValue("https://audio.mp3");
                            newPost.child("typo").setValue("1");
                            newPost.child("visto").setValue("0");
                            newPost.child("menssa").setValue("Diga oi para o seu novo contato.").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendy.setEnabled(true);
                                    audy.setEnabled(true);
                                    testar(meu_identifica___seu_identifica, eideee);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("Users").child(eideee).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (eifiiu){
                        eifiiu = false;
                        if (dataSnapshot.child("name").exists()) {
                            String inameui = dataSnapshot.child("name").getValue().toString();
                            String trimed = inameui.trim();
                            int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                            if (palavras == 1) {
                                bi_nome = inameui;
                            } else {
                                StringTokenizer tokenizer = new StringTokenizer(inameui, " ");
                                String primeiro = tokenizer.nextToken();
                                String segundo = tokenizer.nextToken();
                                bi_nome = primeiro;
                            }
                        }
                }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mDatabase.child("Users").child(uide).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("eOnline").exists()) {
                        if (dataSnapshot.child("eOnline").getValue().toString() != null){
                            onli = Integer.parseInt(dataSnapshot.child("eOnline").getValue().toString());
                            if (onli == 1){
                                String trimed = rome.trim();
                                int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                                if (palavras == 1){
                                    nomase.setText(rome + " (offline)");
                                }else {
                                    StringTokenizer tokenizer = new StringTokenizer(rome, " ");
                                    String primeiro = tokenizer.nextToken();
                                    String segundo = tokenizer.nextToken();
                                    nomase.setText(primeiro + " (offline)");
                                }
                            }else if (onli == 2){
                                String trimed = rome.trim();
                                int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                                if (palavras == 1){
                                    nomase.setText(rome + " (online)");
                                }else {
                                    StringTokenizer tokenizer = new StringTokenizer(rome, " ");
                                    String primeiro = tokenizer.nextToken();
                                    String segundo = tokenizer.nextToken();
                                    nomase.setText(primeiro + " (online)");
                                }
                            }
                        }
                    }else {
                        String trimed = rome.trim();
                        int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

                        if (palavras == 1){
                            nomase.setText(rome + " (offline)");
                        }else {
                            StringTokenizer tokenizer = new StringTokenizer(rome, " ");
                            String primeiro = tokenizer.nextToken();
                            String segundo = tokenizer.nextToken();
                            nomase.setText(primeiro + " (offline)");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void testar(final String user_ide, String eideee) {
        DatabaseReference chatRebi = mDatabase.child("Batepapo").child(user_ide);
        Query recenteMensagem = chatRebi.limitToLast(npostagem);

        final FirebaseRecyclerAdapter<NMsn, SinViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NMsn, SinViewHolder>(
                NMsn.class,
                R.layout.msn_row,
                SinViewHolder.class,
                recenteMensagem) {
            @Override
            protected void populateViewHolder(final SinViewHolder friViewHolder, NMsn friend, final int i) {
                final String post_key = getRef(i).getKey();

                friViewHolder.setTextoo(friend.getMenssa());
                friViewHolder.setUide(friend.getMident());
                friViewHolder.setDatai(friend.getDataa());
                friViewHolder.setTyypo(friend.getTypo());
                friViewHolder.setAudioo(friend.getAudio());
                friViewHolder.setImaagem(getApplicationContext(),friend.getImagem());
                friViewHolder.setVistoo(friend.getVisto());


                friViewHolder.ePerfil.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        RadioButton baixaa, dletarr;
                        TextView cancell, confirmarr, qdfazer;

                        final Dialog dialog = new Dialog(ChatActivity.this);
                        dialog.setContentView(R.layout.doue);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        baixaa = dialog.findViewById(R.id.baixarr);
                        dletarr = dialog.findViewById(R.id.deletarr);
                        cancell = dialog.findViewById(R.id.btncanci);
                        confirmarr = dialog.findViewById(R.id.btn_confirmar);
                        qdfazer = dialog.findViewById(R.id.qdfazer);

                            dletarr.setEnabled(false);
                            if (!friViewHolder.uidei.equals("edebelzaakso")) {
                                if (!friViewHolder.uidei.equals(eideee)) {
                                    dletarr.setEnabled(true);
                                }
                            }

                       cancell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                            baixaa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b){
                                        baixaaa = true;
                                        dletarr.setChecked(false);
                                        confirmarr.setAlpha(1f);
                                    }else{
                                        baixaaa = false;
                                    }
                                }
                            });

                            dletarr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b){
                                        deletaa = true;
                                        baixaa.setChecked(false);
                                        confirmarr.setAlpha(1f);
                                    }else{
                                        deletaa = false;
                                    }
                                }
                            });

                            confirmarr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (baixaaa){
                                        dialog.dismiss();
                                        baixaaa = false;
                                        downloadfile(MKfil);
                                    }else if (deletaa){
                                        deletaa = false;
                                        dialog.dismiss();
                                        if (friViewHolder.typoo == 1) {
                                            mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else if (friViewHolder.typoo == 2) {
                                            mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                    StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.fotos);
                                                    igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }else if (friViewHolder.typoo == 3) {
                                            mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                    StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.maudio);
                                                    igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        dialog.setCancelable(true);
                        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                        return false;
                    }
                });

                friViewHolder.ePerfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            ImageView img_bu;
                            final Dialog dialog = new Dialog(ChatActivity.this);
                            dialog.setContentView(R.layout.auver);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER);
                            img_bu = dialog.findViewById(R.id.btimg);


                            img_bu.setBackgroundResource(0);
                            Picasso.get().load(MKfil).placeholder(R.color.colorPrimary).into(img_bu);

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

                friViewHolder.setms.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!friViewHolder.uidei.equals("edebelzaakso")) {
                            if (!friViewHolder.uidei.equals(eideee)) {
                                RadioButton baixaa, dletarr;
                                TextView cancell, confirmarr, qdfazer;

                                final Dialog dialog = new Dialog(ChatActivity.this);
                                dialog.setContentView(R.layout.doue);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                Window window = dialog.getWindow();
                                window.setGravity(Gravity.CENTER);
                                baixaa = dialog.findViewById(R.id.baixarr);
                                dletarr = dialog.findViewById(R.id.deletarr);
                                cancell = dialog.findViewById(R.id.btncanci);
                                confirmarr = dialog.findViewById(R.id.btn_confirmar);
                                qdfazer = dialog.findViewById(R.id.qdfazer);

                                baixaa.setEnabled(false);

                                cancell.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                dletarr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b){
                                            deletaa = true;
                                            confirmarr.setAlpha(1f);
                                        }else{
                                            deletaa = false;
                                        }
                                    }
                                });

                                confirmarr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (deletaa) {
                                            deletaa = false;
                                            dialog.dismiss();
                                            if (friViewHolder.typoo == 1) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else if (friViewHolder.typoo == 2) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                        StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.fotos);
                                                        igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }else if (friViewHolder.typoo == 3) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                        StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.maudio);
                                                        igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                                dialog.setCancelable(true);
                                window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                                dialog.show();
                            }
                        }
                        return false;
                    }
                });

                friViewHolder.voicePlayerView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!friViewHolder.uidei.equals("edebelzaakso")) {
                            if (!friViewHolder.uidei.equals(eideee)) {
                        RadioButton baixaa, dletarr;
                        TextView cancell, confirmarr, qdfazer;

                        final Dialog dialog = new Dialog(ChatActivity.this);
                        dialog.setContentView(R.layout.doue);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        baixaa = dialog.findViewById(R.id.baixarr);
                        dletarr = dialog.findViewById(R.id.deletarr);
                        cancell = dialog.findViewById(R.id.btncanci);
                        confirmarr = dialog.findViewById(R.id.btn_confirmar);
                                qdfazer = dialog.findViewById(R.id.qdfazer);

                        baixaa.setEnabled(false);

                        cancell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                                dletarr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b){
                                            deletaa = true;
                                            confirmarr.setAlpha(1f);
                                        }else{
                                            deletaa = false;
                                        }
                                    }
                                });

                                confirmarr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (deletaa) {
                                            deletaa = false;
                                            dialog.dismiss();
                                            if (friViewHolder.typoo == 1) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else if (friViewHolder.typoo == 2) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                        StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.fotos);
                                                        igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }else if (friViewHolder.typoo == 3) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                        StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.maudio);
                                                        igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                        dialog.setCancelable(true);
                        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                }
            }
                        return false;
                    }
                });

                friViewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!friViewHolder.uidei.equals("edebelzaakso")) {
                            if (!friViewHolder.uidei.equals(eideee)) {
                        RadioButton baixaa, dletarr;
                        TextView cancell, confirmarr, qdfazer;

                        final Dialog dialog = new Dialog(ChatActivity.this);
                        dialog.setContentView(R.layout.doue);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        baixaa = dialog.findViewById(R.id.baixarr);
                        dletarr = dialog.findViewById(R.id.deletarr);
                        cancell = dialog.findViewById(R.id.btncanci);
                        confirmarr = dialog.findViewById(R.id.btn_confirmar);
                                qdfazer = dialog.findViewById(R.id.qdfazer);

                        baixaa.setEnabled(false);

                        cancell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                                dletarr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b){
                                            deletaa = true;
                                            confirmarr.setAlpha(1f);
                                        }else{
                                            deletaa = false;
                                        }
                                    }
                                });

                                confirmarr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (deletaa) {
                                            deletaa = false;
                                            dialog.dismiss();
                                            if (friViewHolder.typoo == 1) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else if (friViewHolder.typoo == 2) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                        StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.fotos);
                                                        igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }else if (friViewHolder.typoo == 3) {
                                                mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                        StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.maudio);
                                                        igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                        dialog.setCancelable(true);
                        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                }
            }
                        return false;
                    }
                });

                friViewHolder.idcard.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        RadioButton baixaa, dletarr;
                        TextView cancell, confirmarr, qdfazer;

                        final Dialog dialog = new Dialog(ChatActivity.this);
                        dialog.setContentView(R.layout.doue);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        baixaa = dialog.findViewById(R.id.baixarr);
                        dletarr = dialog.findViewById(R.id.deletarr);
                        cancell = dialog.findViewById(R.id.btncanci);
                        confirmarr = dialog.findViewById(R.id.btn_confirmar);
                        qdfazer = dialog.findViewById(R.id.qdfazer);

                        dletarr.setEnabled(false);
                        if (!friViewHolder.uidei.equals("edebelzaakso")) {
                            if (!friViewHolder.uidei.equals(eideee)) {
                                dletarr.setEnabled(true);
                            }
                        }

                        baixaa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b){
                                    confirmarr.setAlpha(1f);
                                    baixaaa = true;
                                    dletarr.setChecked(false);
                                }else{
                                    baixaaa = false;
                                    }
                            }
                        });

                        dletarr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b){
                                    confirmarr.setAlpha(1f);
                                    baixaa.setChecked(false);
                                    deletaa = true;
                                }else{
                                    deletaa = false;
                                }
                            }
                        });

                        cancell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        confirmarr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (baixaaa){
                                    baixaaa = false;
                                    dialog.dismiss();
                                    downloadfile(friViewHolder.fotos);
                                }else if (deletaa){
                                    deletaa = false;
                                    dialog.dismiss();
                                    if (friViewHolder.typoo == 1) {
                                        mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else if (friViewHolder.typoo == 2) {
                                        mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.fotos);
                                                igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }else if (friViewHolder.typoo == 3) {
                                        mDatabase.child("Batepapo").child(user_ide).child(post_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                StorageReference igell = foto_img.getReferenceFromUrl(friViewHolder.maudio);
                                                igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ChatActivity.this, "EXCLUÍDO", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }
                        });

                        dialog.setCancelable(true);
                        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                        return false;
                    }
                });

                friViewHolder.idcard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageView img_bu;
                            final Dialog dialog = new Dialog(ChatActivity.this);
                            dialog.setContentView(R.layout.auver);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER);
                            img_bu = dialog.findViewById(R.id.btimg);

                        img_bu.setBackgroundResource(0);
                        Picasso.get().load(friViewHolder.fotos).placeholder(R.color.colorPrimary).into(img_bu);

                            img_bu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                        dialog.setCancelable(true);
                        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    }
                });

                Drawable unw = AppCompatResources.getDrawable(ChatActivity.this, R.drawable.shape_imessage);
                Drawable mdrX = DrawableCompat.wrap(unw);
                DrawableCompat.setTint(mdrX, 0xffA363DA);

                if ("edebelzaakso".equals(friViewHolder.uidei)) {
                    activity_main.setRefreshing(false);
                    friViewHolder.tempto.setVisibility(View.GONE);
                    friViewHolder.idcard.setVisibility(View.GONE);
                    friViewHolder.setms.setVisibility(View.GONE);
                    friViewHolder.ePerfil.setVisibility(View.GONE);
                    friViewHolder.voicePlayerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }else {
                    activity_main.setRefreshing(false);
                    if (uide.equals(friViewHolder.uidei)) {
                        progressBar.setVisibility(View.GONE);

                        if (friViewHolder.typoo == 1) {
                            if (friViewHolder.vistooo.equals("0")){
                                friViewHolder.tempto.setText(friViewHolder.vidataa + " - Não lida");
                            }else if (friViewHolder.vistooo.equals("1")){
                                friViewHolder.tempto.setText(friViewHolder.vidataa + " - Lida");
                            }
                            friViewHolder.tempto.setVisibility(View.VISIBLE);
                            friViewHolder.voicePlayerView.setVisibility(View.GONE);
                            friViewHolder.idcard.setVisibility(View.GONE);
                            friViewHolder.setms.setVisibility(View.VISIBLE);
                            friViewHolder.ePerfil.setVisibility(View.GONE);
                            friViewHolder.setms.setTextColor(Color.parseColor("#ffffff"));
                            friViewHolder.tempto.setTextColor(Color.parseColor("#ffffff"));
                            friViewHolder.elayout.setBackground(mdrX);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            params.setMargins(80, 0, 0, 0);
                            friViewHolder.elayouti.setLayoutParams(params);

                        } else if (friViewHolder.typoo == 2) {
                            friViewHolder.tempto.setVisibility(View.GONE);
                            friViewHolder.voicePlayerView.setVisibility(View.GONE);
                            friViewHolder.setms.setVisibility(View.GONE);
                            friViewHolder.idcard.setVisibility(View.VISIBLE);
                            friViewHolder.ePerfil.setVisibility(View.GONE);
                            friViewHolder.elayout.setBackground(mdrX);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            params.setMargins(60, 0, 0, 0);
                            friViewHolder.elayouti.setLayoutParams(params);

                        } else if (friViewHolder.typoo == 3) {
                            friViewHolder.tempto.setVisibility(View.GONE);
                            friViewHolder.voicePlayerView.setVisibility(View.VISIBLE);
                            friViewHolder.voicePlayerView.setPlayPaueseBackgroundShape(R.color.colorPrimary, 100);
                            friViewHolder.voicePlayerView.setProgressTimeColor("#ffffff");
                            friViewHolder.voicePlayerView.setSeekBarStyle(R.color.branco, R.color.tranparente);
                            friViewHolder.voicePlayerView.setPlayPauseShape(ChatActivity.this, R.color.branco);
                            friViewHolder.idcard.setVisibility(View.GONE);
                            friViewHolder.setms.setVisibility(View.GONE);
                            friViewHolder.ePerfil.setVisibility(View.GONE);
                            friViewHolder.elayout.setBackground(mdrX);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            params.setMargins(100, 0, 0, 0);
                            friViewHolder.elayouti.setLayoutParams(params);


                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        if (friViewHolder.typoo == 1) {
                            if (friViewHolder.vistooo.equals("0")){
                                friViewHolder.tempto.setText(friViewHolder.vidataa + " - Não lida");
                                friViewHolder.mDatabaseVisto.child("Batepapo").child(user_ide).child(post_key).child("visto").setValue("1");
                            }else if (friViewHolder.vistooo.equals("1")){
                                friViewHolder.tempto.setText(friViewHolder.vidataa + " - Lida");
                            }
                            friViewHolder.tempto.setVisibility(View.VISIBLE);
                            friViewHolder.voicePlayerView.setVisibility(View.GONE);
                            friViewHolder.idcard.setVisibility(View.GONE);
                            friViewHolder.setms.setVisibility(View.VISIBLE);
                            friViewHolder.setms.setTextColor(Color.parseColor("#A363DA"));
                            friViewHolder.tempto.setTextColor(Color.parseColor("#A363DA"));
                            friViewHolder.elayout.setBackground(ContextCompat.getDrawable(ChatActivity.this, R.drawable.shape_omessage));
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            params.setMargins(0, 0, 80, 0);
                            friViewHolder.elayouti.setLayoutParams(params);


                        } else if (friViewHolder.typoo == 2) {
                            friViewHolder.tempto.setVisibility(View.GONE);
                            friViewHolder.voicePlayerView.setVisibility(View.GONE);
                            friViewHolder.setms.setVisibility(View.GONE);
                            friViewHolder.idcard.setVisibility(View.VISIBLE);
                            friViewHolder.elayout.setBackground(ContextCompat.getDrawable(ChatActivity.this, R.drawable.shape_omessage));
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            params.setMargins(0, 0, 60, 0);
                            friViewHolder.elayouti.setLayoutParams(params);

                        }else if (friViewHolder.typoo == 3) {
                            friViewHolder.tempto.setVisibility(View.GONE);
                            friViewHolder.voicePlayerView.setVisibility(View.VISIBLE);
                            friViewHolder.voicePlayerView.setPlayPaueseBackgroundShape(R.color.crbelor, 100);
                            friViewHolder.voicePlayerView.setProgressTimeColor("#BA673AB7");
                            friViewHolder.voicePlayerView.setSeekBarStyle(R.color.colorPrimary, R.color.tranparente);
                            friViewHolder.voicePlayerView.setPlayPauseShape(ChatActivity.this, R.color.colorPrimary);
                            friViewHolder.idcard.setVisibility(View.GONE);
                            friViewHolder.setms.setVisibility(View.GONE);
                            friViewHolder.elayout.setBackground(ContextCompat.getDrawable(ChatActivity.this, R.drawable.shape_omessage));
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            params.setMargins(0, 0, 80, 0);
                            friViewHolder.elayouti.setLayoutParams(params);


                        }
                        friViewHolder.ePerfil.setVisibility(View.VISIBLE);
                        Glide.with(ChatActivity.this)
                                .load(MKfil)
                                .centerCrop()
                                .placeholder(R.color.colorPrimary)
                                .into(friViewHolder.ePerfil);
                    }
                }

            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManager.smoothScrollToPosition(blog_list, null, firebaseRecyclerAdapter.getItemCount());
            }
        });

        blog_list.setAdapter(firebaseRecyclerAdapter);
    }

    public void ovook(View view) {
        onBackPressed();
    }

    public static class SinViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout elayout, elayouti;
        ImageView imgchat, sewrf;
        CircleImageView ePerfil;
        TextView setms;
        TextView tempto;
        String uidei;
        String fotos;
        CardView idcard;
        int typoo = 0;
        View mView;
        String maudio;
        VoicePlayerView voicePlayerView;
        String vistooo;
        String vidataa;
        FirebaseAuth mAuth;
        DatabaseReference mDatabaseVisto;

        public SinViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            voicePlayerView = mView.findViewById(R.id.voicePlayerView);
            ePerfil = mView.findViewById(R.id.ePerfil);
            setms = mView.findViewById(R.id.setmsn);
            tempto = mView.findViewById(R.id.tempto);
            idcard = mView.findViewById(R.id.idcard);
            elayout = mView.findViewById(R.id.elayout);
            imgchat = mView.findViewById(R.id.imgchat);
            elayouti = mView.findViewById(R.id.elayouti);
            sewrf = mView.findViewById(R.id.sewrf);
            tempto.setVisibility(View.GONE);

            mAuth = FirebaseAuth.getInstance();
            mDatabaseVisto = FirebaseDatabase.getInstance().getReference();
        }

        public void setTextoo(String msntext) {
            setms.setText(msntext);
        }

        public void setUide(String mident) {
            uidei = mident;
        }

        public void setDatai(String dataa) {
            vidataa = dataa;
            tempto.setText(dataa);
        }

        public void setTyypo(String typo) {
            if (typo != null) {
                typoo = Integer.parseInt(typo);
            }
        }

        public void setImaagem(final Context applicationContext, final String imagem) {
            if (imagem != null ){
                if (!imagem.equals("https://images.jpeg")) {
            if (!imagem.equals("https://images.jpep")) {
                fotos = imagem;
                sewrf.setBackgroundResource(R.color.colorWhite);
                imgchat.setBackgroundResource(0);
                CarregarImagem.downloadimg2(applicationContext, imagem, imgchat);
            }
            }
        }
        }

        public void setAudioo(String audio) {
            maudio = audio;
            if(audio != null) {
                if(audio.equals("https://audio.mp3")){}else {
                    voicePlayerView.setAudio(audio);
                }
            }
        }

        public void setVistoo(String visto) {
            if (visto != null) {
                vistooo = visto;
            }else{
                vistooo = "0";
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (eideee != null) {
            mDatabase.child("Users").child(eideee).child("eOnline").setValue("1");
        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        Intent loginInten = new Intent(ChatActivity.this, MainActivity.class);
        startActivity(loginInten);
        finish();
        ChatActivity.this.overridePendingTransition(R.anim.volte, R.anim.volte_ii);
    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Uri mImageUri = data.getData();
                mAutori = true;
                looeee.setVisibility(View.VISIBLE);

                StorageReference filepath = mStorageRef.child("Chat_Images").child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
                UploadTask uploadTask = filepath.putFile(mImageUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            if (mAutori) {
                                mAutori = false;
                                looeee.setVisibility(View.GONE);
                                String datae = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                                final DatabaseReference newPost = mDatabase.child("Batepapo").child(meu_identifica___seu_identifica).push();
                                newPost.child("mident").setValue(uide);
                                newPost.child("dataa").setValue(datae);
                                newPost.child("typo").setValue("2");
                                newPost.child("visto").setValue("0");
                                newPost.child("audio").setValue("https://audio.mp3");
                                newPost.child("imagem").setValue(downloadUrl.toString());
                                newPost.child("menssa").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        MediaPlayer mp = MediaPlayer.create(ChatActivity.this, R.raw.sound);
                                        try {
                                            mp.prepare();
                                        } catch (IllegalStateException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        mp.start();

                                        if (onli == 1){
                                            if (avisooo == 0) {
                                                avisooo = 66;

                                                if (bi_nome != null){
                                                    enviaNotificaee(bi_nome + " enviou uma nova mensagem");
                                                }
                                            }
                                            mDatabase.child("Users").child(uide).child("Contatos").child(urlke).child("nova_msn").setValue("1");
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(ChatActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void downloadfile(String urlimg) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(urlimg);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Downloading");
        pd.setMessage("Baixando imagem!");
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();


        final File rootPath = new File(Environment.getExternalStorageDirectory(), "NNZi DL");

        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        String name = new Date().toString() + ".jpg";
        final File localFile = new File(rootPath, name);

        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener <FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                if (localFile.canRead()){
                    ChatActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(localFile)));
                    pd.dismiss();
                }

                Toast.makeText(ChatActivity.this, "SUCESSO", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(ChatActivity.this, "ERRO", Toast.LENGTH_LONG).show();
            }
        });
    }
}

