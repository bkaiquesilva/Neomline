package com.bkaiquesilva.nnzi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText btnRegisterPageRegister;
    private EditText editTextRegisterName;
    private EditText editTextRegisterEmail1;
    private EditText editTextRegisterPassword;
    private boolean autori = false;
    private boolean cogt = true;

    private DatabaseReference mDatabase, mcontato;
    ProgressDialog dialog;
    private LinearLayout abb, abb2, abb3;
    private EditText logine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();
        mcontato = FirebaseDatabase.getInstance().getReference().child("OContact");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        abb = findViewById(R.id.abb);
        abb2 = findViewById(R.id.abb2);
        abb3 = findViewById(R.id.abb3);
        logine = findViewById(R.id.logine);

        btnRegisterPageRegister = findViewById(R.id.btnRegisterPageRegister);
        editTextRegisterName = findViewById(R.id.editTextRegisterName);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        editTextRegisterEmail1 = findViewById(R.id.editTextRegisterEmail1);

        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("Processando...");
        dialog.setCancelable(false);

        logine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnRegisterPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(cogt) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.volte, R.anim.volte_ii);
            finish();
        }else{
            Button back;
            TextView ti, to;
            ImageView status;
            Dialog dialog = new Dialog(RegisterActivity.this);
            dialog.setContentView(R.layout.eilert);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);

            back = dialog.findViewById(R.id.back);
            ti = dialog.findViewById(R.id.ti);
            to = dialog.findViewById(R.id.to);
            status = dialog.findViewById(R.id.status);
            ti.setText("Oops");
            to.setText("O cadastro ainda\n" + "nestá imcompleto.");
            status.setImageResource(R.drawable.confused);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setCancelable(false);
            window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.show();
        }
    }

    private void startRegister() {
            final String emaill = editTextRegisterEmail1.getText().toString();
            final String stringName = editTextRegisterName.getText().toString();
            final String stringPassword = editTextRegisterPassword.getText().toString();

            String trimed = stringName.trim();
            int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;
            String tmed = String.valueOf(palavras);

            if (palavras >= 3){
                Button back;
                TextView ti, to;
                ImageView status;
                Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.eilert);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);

                back = dialog.findViewById(R.id.back);
                ti = dialog.findViewById(R.id.ti);
                to = dialog.findViewById(R.id.to);
                status = dialog.findViewById(R.id.status);
                ti.setText("Oops");
                to.setText("O nome é limitado\n" + "a duas palavras.");
                status.setImageResource(R.drawable.sadi);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setCancelable(false);
                window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.show();

                }else {

                if (!TextUtils.isEmpty(stringName) &&
                        !TextUtils.isEmpty(emaill) &&
                        !TextUtils.isEmpty(stringPassword)) {
                    dialog.show();
                    autori = true;

                    mAuth.createUserWithEmailAndPassword(emaill, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                detalhe(stringName, emaill);
                            } else {
                                dialog.dismiss();
                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthWeakPasswordException weakPassword) {
                                    Toast.makeText(RegisterActivity.this, "SENHA FRACA", Toast.LENGTH_SHORT).show();
                                }catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                    Toast.makeText(RegisterActivity.this, "E-MAIL INVÁLIDO", Toast.LENGTH_SHORT).show();
                                }catch (FirebaseAuthUserCollisionException existEmail) {
                                    Toast.makeText(RegisterActivity.this, "E-MAIL JÁ EXISTE", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(RegisterActivity.this, "ERRO", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                }else {
                    errroo();
                }
            }
    }

    private void detalhe(String name, String stringEmail) {
            final String op1 = Gerador(2);
            final String op2 = Gerador2(4);
            final String op3 = Gerador3(4);
            String gsss = "+55 " + op1 + " 9" + op2 + "-" + op3;
            mcontato.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (autori) {
                        autori = false;
                        if (!dataSnapshot.child(gsss).exists()) {
                            String user_id = mAuth.getCurrentUser().getUid();
                            mcontato.child(gsss).setValue(user_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference current_user_db = mDatabase.child(user_id);
                                    current_user_db.child("name").setValue(name);
                                    current_user_db.child("image").setValue("https://www.suaimagempadrao.com/imagem.jpg/");
                                    current_user_db.child("user_usuario").setValue(gsss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            SharedPreferences msharedpref = getSharedPreferences("editos", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = msharedpref.edit();
                                            editor.putString("emme", stringEmail);
                                            editor.apply();

                                            cogt = false;
                                            abb.setVisibility(View.GONE);
                                            abb2.setVisibility(View.VISIBLE);
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        } else {
                            autori = true;
                            detalhe(name, stringEmail);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }

    private void enviarverificar() {
        dialog.setMessage("Enviando e-mail......");
        dialog.show();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        cogt = true;
                        abb2.setVisibility(View.GONE);
                        abb3.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(RegisterActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }
            });
        }
    }

    private void errroo() {
        Button back;
        TextView ti, to;
        ImageView status;
        Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setContentView(R.layout.eilert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        back = dialog.findViewById(R.id.back);
        ti = dialog.findViewById(R.id.ti);
        to = dialog.findViewById(R.id.to);
        status = dialog.findViewById(R.id.status);
        ti.setText("Oops");
        to.setText("Preencha todos\n" + "os campos.");
        status.setImageResource(R.drawable.sadi);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public String Gerador(int lenght){
        char[] chars = "0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < lenght; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public String Gerador2(int lenght){
        char[] chars = "0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < lenght; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public String Gerador3(int lenght){
        char[] chars = "0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < lenght; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void confirmar(View view) {
        enviarverificar();
    }
}


