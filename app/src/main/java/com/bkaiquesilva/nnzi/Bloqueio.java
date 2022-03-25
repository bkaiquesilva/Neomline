package com.bkaiquesilva.nnzi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanks.passcodeview.PasscodeView;

public class Bloqueio extends AppCompatActivity {

    PasscodeView senha_view;
    private String senhaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloqueio);

        Bundle extras = getIntent().getExtras();
        senhaa = extras.getString("cedia");

        senha_view = findViewById(R.id.senha_view);

        senha_view.setPasscodeLength(4)
                .setLocalPasscode(senhaa)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        Toast.makeText(getApplicationContext(), "INCORRETO", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onSuccess(String number) {
                            Intent intent = new Intent(Bloqueio.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
