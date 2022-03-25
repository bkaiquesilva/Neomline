package com.bkaiquesilva.nnzi;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bkaiquesilva.nnzi.Permissao.EasyPermissionList;
import com.bumptech.glide.Glide;
import com.eyalbira.loadingdots.LoadingDots;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilSobee extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CircleImageView eperfil;
    private EditText enomeeww;
    private EditText eaniver;
    private ImageView sv_nome;
    private ProgressBar progres_1;
    private TextView bloqueadoo, senia;
    private boolean bloqueadooo = true;
    private StorageReference mStorageRef;

    private String perfill;
    private String strDate;
    private String u_uuii;
    private String y_iidenti;
    private boolean uii = false;
    private boolean mAutorize = false;
    private static final int GALLERY_REQUEST = 999;


    private String numeero;
    private String imaage;
    private String imaage2;
    private String enome2;
    private String iuodo;
    private String usidee;
    private String enome;
    private boolean auto3 = false;
    private boolean auto4 = false;
    private boolean auto5 = false;
    private boolean sebi1 = false;
    private boolean sebi2 = false;
    private String meu_identifica;
    private DatabaseReference mOrkutNum;
    private EditText encerrade;
    private String nooomee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_sobee);

        Bundle extras = getIntent().getExtras();
        u_uuii = extras.getString("y_uuii");
        perfill = extras.getString("y_image");
        y_iidenti = extras.getString("y_iidenti");
        nooomee = extras.getString("noojjee");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mOrkutNum = FirebaseDatabase.getInstance().getReference().child("OContact");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        senia = findViewById(R.id.senia);
        sv_nome = findViewById(R.id.sv_nome);
        progres_1 = findViewById(R.id.progres_1);
        bloqueadoo = findViewById(R.id.bloqueadooo);
        encerrade = findViewById(R.id.encerrade);
        eperfil = findViewById(R.id.dth_perfil);
        enomeeww = findViewById(R.id.dth_nome);
        eaniver = findViewById(R.id.dth_aniversario);

        enomeeww.setText(nooomee);
        eaniver.setText(y_iidenti);

        Glide.with(PerfilSobee.this)
                .load(perfill)
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .into(eperfil);


        encerrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final AlertDialog dialo = new AlertDialog.Builder(PerfilSobee.this).create();
        View c = getLayoutInflater().inflate(R.layout.deletew, null);
        dialo.setCanceledOnTouchOutside(false);
        dialo.setCancelable(false);
        final EditText email = c.findViewById(R.id.edt_email);
        final EditText senha = c.findViewById(R.id.edt_senha);
        final TextView cancelar = c.findViewById(R.id.btncanci);
        final TextView deletar = c.findViewById(R.id.btn_dlt);
        final ProgressBar tpro = c.findViewById(R.id.podegi);
        dialo.setView(c);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletar.setEnabled(false);
                cancelar.setEnabled(false);
                tpro.setVisibility(View.VISIBLE);
                deletar.getBackground().setAlpha(128);
                cancelar.getBackground().setAlpha(128);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialo.dismiss();
                    }
                }, 1700);
            }
        });

        deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_email = email.getText().toString().trim();
                String e_senha = senha.getText().toString().trim();
                if (!e_email.isEmpty() & !e_senha.isEmpty()) {
                deletar.setEnabled(false);
                cancelar.setEnabled(false);
                tpro.setVisibility(View.VISIBLE);
                deletar.getBackground().setAlpha(128);
                cancelar.getBackground().setAlpha(128);

                        AuthCredential credential = EmailAuthProvider.getCredential(e_email, e_senha);
                        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialo.dismiss();
                                            Intent loginIntent = new Intent(PerfilSobee.this, LoginActivity.class);
                                            startActivity(loginIntent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                    }
                                });
                            }
                        });
                }
            }
        });

        dialo.show();
            }
        });

        eaniver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (y_iidenti != null) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", y_iidenti);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(PerfilSobee.this, "NÚMERO COPIADO", Toast.LENGTH_SHORT).show();
                }
            }
        });

        eperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PerfilSobee.this, EasyPermissionList.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PerfilSobee.this, "ERRO. PERMISSÃO NEGADA", Toast.LENGTH_SHORT).show();
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

        eperfil.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ImageView img_bu;
                final Dialog dialog = new Dialog(PerfilSobee.this);
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
                return false;
            }
        });

        uii = true;

        bloqueadoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloqueadoo.setEnabled(false);
                if (bloqueadooo){
                    mDatabase.child(u_uuii).child("cadeado").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    senia.setText("----");
                                    bloqueadoo.setText("BLOQUEAR");
                                    bloqueadoo.setEnabled(true);
                                    bloqueadooo = false;
                                }
                            }, 700);
                        }
                    });

                }else {
                    bloqueadoo.setEnabled(true);

                    final AlertDialog dialo;
                    dialo = new AlertDialog.Builder(PerfilSobee.this).create();
                    View c = getLayoutInflater().inflate(R.layout.auver, null);
                    dialo.setCanceledOnTouchOutside(true);
                    dialo.setCancelable(true);
                    dialo.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    final ImageView img_bu = c.findViewById(R.id.btimg);
                    final RelativeLayout relate = c.findViewById(R.id.relate);
                    final EditText edit = c.findViewById(R.id.txtSenha);
                    dialo.setView(c);

                    edit.setVisibility(View.VISIBLE);
                    img_bu.setVisibility(View.GONE);
                    relate.setBackgroundColor(Color.WHITE);

                    edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            boolean handled = false;
                            if (i == EditorInfo.IME_ACTION_DONE) {
                                String tst = edit.getText().toString().trim();
                                if (!TextUtils.isEmpty(tst)) {
                                if (tst.length() == 4) {
                                    Toast.makeText(PerfilSobee.this, "Bloqueado", Toast.LENGTH_SHORT).show();
                                    mDatabase.child(u_uuii).child("cadeado").setValue(tst).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialo.dismiss();
                                            bloqueadoo.setText("DESBLOQUEAR");
                                            bloqueadoo.setEnabled(true);
                                            bloqueadooo = true;
                                            senia.setText(tst);
                                        }
                                    });

                                }else {
                                    Toast.makeText(PerfilSobee.this, "Digite 4 números", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                    Toast.makeText(PerfilSobee.this, "Não pode ficar vazio", Toast.LENGTH_SHORT).show();
                                }
                                handled = true;
                            }
                            return handled;
                        }
                    });


                    dialo.show();
                }
            }
        });

        mDatabase.child(u_uuii).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (uii){
                    uii = false;
                    if (dataSnapshot.child("name").exists()) {
                                if (dataSnapshot.child("cadeado").exists()) {
                                    bloqueadoo.setText("DESBLOQUEAR");
                                    bloqueadoo.setEnabled(true);
                                    bloqueadooo = true;
                                    String refre = dataSnapshot.child("cadeado").getValue().toString();
                                    senia.setText(refre);
                                } else {
                                    bloqueadoo.setText("BLOQUEAR");
                                    bloqueadoo.setEnabled(true);
                                    bloqueadooo = false;
                                    senia.setText("----");
                                }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sv_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nome = enomeeww.getText().toString().trim();
                String trimed = nome.trim();
                int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;
                String tmed = String.valueOf(palavras);

                if (palavras >= 3) {
                    enomeeww.setError("Apenas duas palavras");
                }else {
                    if (!TextUtils.isEmpty(nome)) {
                        progres_1.setVisibility(View.VISIBLE);
                        sv_nome.setVisibility(View.GONE);
                        mDatabase.child(u_uuii).child("name").setValue(nome).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progres_1.setVisibility(View.GONE);
                                sv_nome.setVisibility(View.VISIBLE);
                                Toast.makeText(PerfilSobee.this, "Atualizado", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        enomeeww.setError("Não pode ficar vazio");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent loginIntent = new Intent(PerfilSobee.this, MainActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.volte, R.anim.volte_ii);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri mImageUri = result.getUri();
                Confirmee(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(PerfilSobee.this, "ERRO", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Confirmee(Uri mImageUri) {
        final AlertDialog dialo;
        dialo = new AlertDialog.Builder(this).create();
        View c = getLayoutInflater().inflate(R.layout.perfiii, null);
        dialo.setCanceledOnTouchOutside(false);
        dialo.setCancelable(false);
        CircleImageView cicv = c.findViewById(R.id.us_image);
        TextView canc = c.findViewById(R.id.cancee);
        TextView confiee = c.findViewById(R.id.confiee);
        LoadingDots dots = c.findViewById(R.id.looiee);
        dialo.setView(c);

        cicv.setImageURI(mImageUri);

        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialo.dismiss();
            }
        });

        confiee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canc.setVisibility(View.INVISIBLE);
                confiee.setVisibility(View.INVISIBLE);
                dots.setVisibility(View.VISIBLE);
                eperfil.setImageURI(mImageUri);
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAutorize = true;
                    String uuii = user.getUid();
                    StorageReference filepath = mStorageRef.child("Blog_Images").child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
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
                                if (mAutorize) {
                                    mAutorize = false;
                                    mDatabase.child(uuii).child("image").setValue(downloadUrl.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (perfill.equals("https://firebasestorage.googleapis.com/v0/b/nnzi-230b7.appspot.com/o/padrao%2Fuser%20(1).png?alt=media&token=58aa28e9-b3a6-46d9-9dcc-1487dd859978")) {
                                                perfill = downloadUrl.toString();
                                                dialo.dismiss();
                                                Toast.makeText(PerfilSobee.this, "SUCESSO", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (perfill != null) {
                                                    FirebaseStorage foto_img = FirebaseStorage.getInstance();
                                                    StorageReference igell = foto_img.getReferenceFromUrl(perfill);
                                                    igell.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            perfill = downloadUrl.toString();
                                                            dialo.dismiss();
                                                            Toast.makeText(PerfilSobee.this, "SUCESSO", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(PerfilSobee.this, "ERRO", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        dialo.show();

    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    public void novoCOnter(View view) {
        final AlertDialog dialo = new AlertDialog.Builder(PerfilSobee.this).create();
        View c = getLayoutInflater().inflate(R.layout.addnovomembro, null);
        dialo.setCanceledOnTouchOutside(true);
        dialo.setCancelable(true);
        final ImageView sharchi = c.findViewById(R.id.sarchi);
        final EditText textsech = c.findViewById(R.id.textsech);
        final RelativeLayout progressi = c.findViewById(R.id.progressi);
        final LinearLayout slamizade = c.findViewById(R.id.slamizade);
        final TextView btnconvite = c.findViewById(R.id.btnconvite);
        final ImageView imgSoli = c.findViewById(R.id.imgSoli);
        dialo.setView(c);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("+NN NN NNNNN-NNNN");
        MaskTextWatcher mtv = new MaskTextWatcher(textsech, smf);
        textsech.addTextChangedListener(mtv);


        btnconvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    if (!numeero.isEmpty()) {
                        btnconvite.setClickable(false);
                        sebi1 = true;
                        sebi2 = true;

                        mDatabase.child(usidee).child("Contatos").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (sebi1) {
                                            sebi1 = false;
                                            int size = (int) dataSnapshot.getChildrenCount();
                                            if (size >= 101){
                                                Button back;
                                                TextView ti, to;
                                                ImageView status;
                                                Dialog dialog = new Dialog(PerfilSobee.this);
                                                dialog.setContentView(R.layout.eilert);
                                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                Window window = dialog.getWindow();
                                                window.setGravity(Gravity.CENTER);

                                                back = dialog.findViewById(R.id.back);
                                                ti = dialog.findViewById(R.id.ti);
                                                to = dialog.findViewById(R.id.to);
                                                status = dialog.findViewById(R.id.status);
                                                ti.setText("Oops");
                                                to.setText("Você atingiu o limite,\n" + "remova contato.");
                                                status.setImageResource(R.drawable.sadi);

                                                back.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                        textsech.getText().clear();
                                                    }
                                                });

                                                dialog.setCancelable(false);
                                                window.setLayout(androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT, androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT);
                                                dialog.show();
                                            }else{
                                                final String pushi = mDatabase.push().getKey();
                                                final DatabaseReference newPost = mDatabase.child(usidee).child("Contatos").child(pushi);
                                                final DatabaseReference newPost2 = mDatabase.child(iuodo).child("Contatos").child(pushi);

                                                newPost2.child("IMAGE").setValue(imaage2);
                                                newPost2.child("uid").setValue(usidee);
                                                newPost2.child("keys").setValue(pushi);
                                                newPost2.child("nova_msn").setValue("7656");
                                                newPost2.child("meu_identifica").setValue(numeero);
                                                newPost2.child("identificador").setValue(meu_identifica);
                                                newPost2.child("enome").setValue(enome2);

                                                newPost.child("IMAGE").setValue(imaage);
                                                newPost.child("uid").setValue(iuodo);
                                                newPost.child("keys").setValue(pushi);
                                                newPost.child("nova_msn").setValue("0");
                                                newPost.child("meu_identifica").setValue(meu_identifica);
                                                newPost.child("identificador").setValue(numeero);
                                                newPost.child("enome").setValue(enome).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        btnconvite.setText("ADICIONADO");
                                                        btnconvite.setEnabled(false);
                                                        textsech.getText().clear();
                                                        dialo.dismiss();
                                                        Toast.makeText(PerfilSobee.this, "SUCESSO", Toast.LENGTH_SHORT).show();
                                                        Intent iuio = new Intent(PerfilSobee.this, MainActivity.class);
                                                        startActivity(iuio);
                                                        finish();
                                                        overridePendingTransition(R.anim.volte, R.anim.volte_ii);
                                                    }
                                                });
                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            }
        });

        sharchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String textsec = textsech.getText().toString().trim();
                if (!TextUtils.isEmpty(textsec)) {
                    hideKeyboard();
                    progressi.setVisibility(View.VISIBLE);
                    slamizade.setVisibility(View.GONE);
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        usidee = user.getUid();
                        auto3 = true;
                        auto4 = true;
                        auto5 = true;
                        btnconvite.setEnabled(false);

                        mOrkutNum.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (auto4) {
                                    auto4 = false;
                                    if (!textsec.isEmpty()) {
                                        if (dataSnapshot.child(textsec).exists()) {
                                            iuodo = dataSnapshot.child(textsec).getValue().toString();
                                            mDatabase.child(iuodo).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                                    if (auto3) {
                                                        auto3 = false;
                                                        if (dataSnapshot.child("image").exists()) {
                                                            if (dataSnapshot.child("user_usuario").exists()) {
                                                                if (dataSnapshot.child("name").exists()) {
                                                                    enome = dataSnapshot.child("name").getValue().toString();
                                                                    numeero = dataSnapshot.child("user_usuario").getValue().toString();
                                                                    imaage = dataSnapshot.child("image").getValue().toString();
                                                                    mDatabase.child(usidee).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (auto5) {
                                                                                auto5 = false;
                                                                                if (dataSnapshot.child("user_usuario").exists()) {
                                                                                    meu_identifica = dataSnapshot.child("user_usuario").getValue().toString();
                                                                                    if (dataSnapshot.child("image").exists()) {
                                                                                        imaage2 = dataSnapshot.child("image").getValue().toString();
                                                                                        if (dataSnapshot.child("name").exists()) {
                                                                                            enome2 = dataSnapshot.child("name").getValue().toString();
                                                                                            Picasso.get().load(imaage).into(imgSoli, new com.squareup.picasso.Callback() {
                                                                                                @Override
                                                                                                public void onSuccess() {
                                                                                                    if (meu_identifica != null) {
                                                                                                        if (meu_identifica.equals(numeero)) {
                                                                                                            btnconvite.setText("VOCÊ");
                                                                                                            btnconvite.setEnabled(false);
                                                                                                            progressi.setVisibility(View.GONE);
                                                                                                            slamizade.setVisibility(View.VISIBLE);
                                                                                                        } else {
                                                                                                            btnconvite.setText("ADD CONTATO");
                                                                                                            btnconvite.setEnabled(true);
                                                                                                            btnconvite.setClickable(true);
                                                                                                            progressi.setVisibility(View.GONE);
                                                                                                            slamizade.setVisibility(View.VISIBLE);
                                                                                                        }
                                                                                                    } else {
                                                                                                        progressi.setVisibility(View.GONE);
                                                                                                        slamizade.setVisibility(View.GONE);
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onError(Exception e) {
                                                                                                }
                                                                                            });
                                                                                        }else{
                                                                                            enome2 = "Sem nome";

                                                                                            Picasso.get().load(imaage).into(imgSoli, new com.squareup.picasso.Callback() {
                                                                                                @Override
                                                                                                public void onSuccess() {
                                                                                                    if (meu_identifica != null) {
                                                                                                        if (meu_identifica.equals(numeero)) {
                                                                                                            btnconvite.setText("VOCÊ");
                                                                                                            btnconvite.setEnabled(false);
                                                                                                            progressi.setVisibility(View.GONE);
                                                                                                            slamizade.setVisibility(View.VISIBLE);
                                                                                                        } else {
                                                                                                            btnconvite.setText("ADD CONTATO");
                                                                                                            btnconvite.setEnabled(true);
                                                                                                            btnconvite.setClickable(true);
                                                                                                            progressi.setVisibility(View.GONE);
                                                                                                            slamizade.setVisibility(View.VISIBLE);
                                                                                                        }
                                                                                                    } else {
                                                                                                        progressi.setVisibility(View.GONE);
                                                                                                        slamizade.setVisibility(View.GONE);
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onError(Exception e) {
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }else{
                                                                                        imaage2 = "https://firebasestorage.googleapis.com/v0/b/nnzi-230b7.appspot.com/o/padrao%2Fuser%20(1).png?alt=media&token=58aa28e9-b3a6-46d9-9dcc-1487dd859978";

                                                                                        Picasso.get().load(imaage).into(imgSoli, new com.squareup.picasso.Callback() {
                                                                                            @Override
                                                                                            public void onSuccess() {
                                                                                                if (meu_identifica != null) {
                                                                                                    if (meu_identifica.equals(numeero)) {
                                                                                                        btnconvite.setText("VOCÊ");
                                                                                                        btnconvite.setEnabled(false);
                                                                                                        progressi.setVisibility(View.GONE);
                                                                                                        slamizade.setVisibility(View.VISIBLE);
                                                                                                    } else {
                                                                                                        btnconvite.setText("ADD CONTATO");
                                                                                                        btnconvite.setEnabled(true);
                                                                                                        btnconvite.setClickable(true);
                                                                                                        progressi.setVisibility(View.GONE);
                                                                                                        slamizade.setVisibility(View.VISIBLE);
                                                                                                    }
                                                                                                } else {
                                                                                                    progressi.setVisibility(View.GONE);
                                                                                                    slamizade.setVisibility(View.GONE);
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onError(Exception e) {
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                } else {
                                                                    progressi.setVisibility(View.GONE);
                                                                    Toast.makeText(PerfilSobee.this, "Contato sem identificação", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        progressi.setVisibility(View.GONE);
                                                                        Toast.makeText(PerfilSobee.this, "Contato sem identificação", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }, 700);
                                                            }
                                                        } else {
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    progressi.setVisibility(View.GONE);
                                                                    Toast.makeText(PerfilSobee.this, "Contato irreconhecível", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }, 700);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    progressi.setVisibility(View.GONE);
                                                    slamizade.setVisibility(View.GONE);
                                                }
                                            });
                                        } else {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressi.setVisibility(View.GONE);
                                                    Toast.makeText(PerfilSobee.this, "Contato inexistente", Toast.LENGTH_SHORT).show();
                                                }
                                            }, 700);
                                        }
                                    } else {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressi.setVisibility(View.GONE);
                                                Toast.makeText(PerfilSobee.this, "Digite o identificador", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 700);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });

        dialo.show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void loggoutt(View view) {
        mAuth.signOut();
        Intent loginIntent = new Intent(PerfilSobee.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.volte, R.anim.volte_ii);
    }

    public void privacyyyy(View view) {
        Intent intent = new Intent(PerfilSobee.this, PrivacyPolicyActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}