package com.bkaiquesilva.nnzi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bkaiquesilva.nnzi.Permissao.EasyPermissionInit;
import com.bkaiquesilva.nnzi.Permissao.EasyPermissionList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private EditText loginEmail;
    private EditText loginPass;
    private EditText btnLoginPageLogin;
    private TextView btnLoginPageRegister;
    ProgressDialog dialog;
    private Boolean emailcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmailText);
        loginPass = findViewById(R.id.loginPassText);
        btnLoginPageLogin = findViewById(R.id.btnLoginPageLogin);
        btnLoginPageRegister = findViewById(R.id.btnLoginPageRegister);


        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Processando...");
        dialog.setCancelable(false);

        List<String> permission = new ArrayList<>();
        permission.add(EasyPermissionList.RECORD_AUDIO);
        permission.add(EasyPermissionList.WRITE_EXTERNAL_STORAGE);

        if (permission.size() > 0) {
            new EasyPermissionInit(LoginActivity.this, permission);
        }

        btnLoginPageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = loginEmail.getText().toString();
                String userPass = loginPass.getText().toString();
                if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty((userPass))) {
                    SharedPreferences msharedpref = getSharedPreferences("editos", MODE_PRIVATE);
                    SharedPreferences.Editor editor = msharedpref.edit();
                    editor.putString("emme", userEmail);
                    editor.apply();

                    dialog.show();
                    userAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                verificacao();
                            } else {
                                dialog.dismiss();
                                errrooo();
                            }
                        }
                    });
                } else {
                    Button back;
                    TextView ti, to;
                    ImageView status;
                    Dialog dialog = new Dialog(LoginActivity.this);
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
        });

        btnLoginPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }

    private void verificacao(){
        FirebaseUser user = userAuth.getCurrentUser();
        emailcheck = user.isEmailVerified();
        if (emailcheck){
            sendToMain();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Oops");
            builder.setMessage("Por favor, verifique sua caixa de entrada, enviamos lhe um e-mail com um link para confirmar o cadastro.");
            builder.setCancelable(false);
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog11 = builder.create();
            alertDialog11.setCanceledOnTouchOutside(false);
            alertDialog11.show();
            userAuth.signOut();
        }
    }

    private void errrooo() {
        Toast.makeText(this, "ERRO", Toast.LENGTH_SHORT).show();
    }



    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs2 = this.getSharedPreferences("editos", Context.MODE_PRIVATE);
        if (prefs2.getString("emme", "") != null){
            loginEmail.setText(prefs2.getString("emme", ""));
        }
    }

    public void redRedefinir(View view) {
        Intent mainIntent = new Intent(LoginActivity.this, Recuper_Senha.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
