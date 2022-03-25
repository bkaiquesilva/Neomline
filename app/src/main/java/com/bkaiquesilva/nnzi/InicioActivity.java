package com.bkaiquesilva.nnzi;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InicioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mAutorize2 = false;
    private boolean edewe = false;

    private static final int MEU_DADTA = 209;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_activity);

        firebaseInit();

    }

    private void firebaseInit() {
        mAutorize2 = true;
        edewe = true;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            if (mAutorize2) {
                                mAutorize2 = false;
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user == null) {
                                    Intent loginIntent = new Intent(InicioActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                } else {
                                    String user_id = user.getUid();
                                    mDatabaseUsers.child(user_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (edewe) {
                                                edewe = false;

                                                if (dataSnapshot.child("cadeado").exists()) {
                                                    String nme = dataSnapshot.child("cadeado").getValue().toString();
                                                    Intent intent = new Intent(InicioActivity.this, Bloqueio.class);
                                                    intent.putExtra("cedia", nme);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    finish();
                                                } else {
                                                    Intent intent = new Intent(InicioActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    finish();
                                                }


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });
                                }
                            }
                    }
                }, 2000);

            }
        };
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MEU_DADTA && resultCode == RESULT_OK) {
            firebaseInit();
        }else{
            firebaseInit();
        }
    }

}
