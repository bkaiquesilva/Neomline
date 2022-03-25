package com.bkaiquesilva.nnzi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bkaiquesilva.nnzi.Adt.Blog;
import com.bkaiquesilva.nnzi.Adt.CustomAdapter;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mAutorize2 = false;
    private CustomAdapter productsAdapter;
    private ArrayList<Blog> productsLists = new ArrayList<>();
    private Context mContext;
    private ListView listView;
    private String identi;
    private String nooomee;
    private String peffoto;
    private boolean yuba = false;
    private TextView me_txt;
    private EditText editText_searchBar;
    private String uuii;
    SharedPreferences sPreferences = null;
    ProgressDialog dialoggg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.rv_ex);
        listView.setVisibility(View.GONE);

        mContext = MainActivity.this;
        me_txt = findViewById(R.id.me_txt);
        editText_searchBar = findViewById(R.id.editText_searchBar);
        dialoggg = new ProgressDialog(MainActivity.this);
        dialoggg.setMessage("Processando...");
        dialoggg.setCancelable(false);

        productsAdapter = new CustomAdapter(mContext, productsLists);
        listView.setAdapter(productsAdapter);

        sPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);

        editText_searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable si) {
                String s = String.valueOf(si);
                String userinput = s.toLowerCase();
                List<Blog> newList = new ArrayList();
                for (Blog name : productsLists) {
                    if (name.getEnome().toLowerCase().contains(userinput)) {
                        newList.add(name);
                    }
                }

                productsAdapter.updateList(newList);
                listView.setAdapter(productsAdapter);
            }
        });

        firebaseInit();

    }

    private void firebaseInit() {
        mAutorize2 = true;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAutorize2) {
                    mAutorize2 = false;
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        overridePendingTransition(R.anim.volte, R.anim.volte_ii);
                        finish();
                    }else{
                        boolean emailcheck = user.isEmailVerified();
                        if (emailcheck) {
                            uuii = user.getUid();
                            getUserDetails(uuii);
                        }else{
                            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            overridePendingTransition(R.anim.volte, R.anim.volte_ii);
                            finish();
                            mAuth.signOut();
                        }
                    }
                }
            }
        };
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
    }

    private void getUserDetails(String uiio) {
                yuba = true;
                mDatabaseUsers.child(uiio).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (yuba) {
                            yuba = false;
                            OneSignal.setExternalUserId(uiio);
                            if (dataSnapshot.child("user_usuario").exists()) {
                                identi = dataSnapshot.child("user_usuario").getValue().toString();
                                if (dataSnapshot.child("name").exists()) {
                                    nooomee = dataSnapshot.child("name").getValue().toString();
                                    if (dataSnapshot.child("image").exists()) {
                                        peffoto = dataSnapshot.child("image").getValue().toString();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        mDatabaseUsers.child(uuii).child("Contatos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Blog products = postSnapshot.getValue(Blog.class);
                        productsLists.add(products);
                        Collections.sort(productsLists, new Comparator<Blog>() {
                            @Override
                            public int compare(Blog o1, Blog o2) {
                                return o1.getEnome().compareTo(o2.getEnome());
                            }
                        });
                        productsAdapter.notifyDataSetChanged();
                        me_txt.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }else{
                        listView.setVisibility(View.GONE);
                        me_txt.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void configurarr(View view) {
        if (!TextUtils.isEmpty(uuii) &&
                !TextUtils.isEmpty(peffoto) &&
                !TextUtils.isEmpty(identi) &&
        !TextUtils.isEmpty(nooomee)) {
            Intent loginIntent = new Intent(MainActivity.this, PerfilSobee.class);
            loginIntent.putExtra("y_uuii", uuii);
            loginIntent.putExtra("y_image", peffoto);
            loginIntent.putExtra("y_iidenti", identi);
            loginIntent.putExtra("noojjee", nooomee);
            startActivity(loginIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
}
