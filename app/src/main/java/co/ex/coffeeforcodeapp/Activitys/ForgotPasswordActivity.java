package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edittext_email_forgot_password;
    CardView card_btn_change_password;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        edittext_email_forgot_password = findViewById(R.id.edittext_email_forgot_password);
        card_btn_change_password = findViewById(R.id.card_btn_change_password);
        card_btn_change_password.setElevation(20);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loadingDialog = new LoadingDialog(this);

        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        card_btn_change_password.setOnClickListener(v -> {
            card_btn_change_password.setElevation(0);
            card_btn_change_password.setEnabled(false);
            loadingDialog.startLoading();
            if(edittext_email_forgot_password.getText() == null || edittext_email_forgot_password.getText().length() == 0){
                edittext_email_forgot_password.setError("Required to fill in the field: EMAIL" + "\n" + "Necessário preencher o campo: EMAIL");
                edittext_email_forgot_password.requestFocus();
                imm.showSoftInput(edittext_email_forgot_password, InputMethodManager.SHOW_IMPLICIT);
                card_btn_change_password.setElevation(20);
            }else if (!Patterns.EMAIL_ADDRESS.matcher(edittext_email_forgot_password.getText()).matches()){
                edittext_email_forgot_password.setError("Fill in your email correctly" + "\n" + "Preencha corretamente seu email");
                edittext_email_forgot_password.requestFocus();
                imm.showSoftInput(edittext_email_forgot_password, InputMethodManager.SHOW_IMPLICIT);
                card_btn_change_password.setElevation(20);
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(edittext_email_forgot_password.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                loadingDialog.dimissDialog();
                                mPrefs.edit().clear().apply();
                                GoToLogin();
                                Toast.makeText(ForgotPasswordActivity.this, R.string.we_have_send_reset_password, Toast.LENGTH_SHORT).show();
                            } else {
                                loadingDialog.dimissDialog();
                                mPrefs.edit().clear().apply();
                                card_btn_change_password.setEnabled(true);
                                Toast.makeText(ForgotPasswordActivity.this, R.string.failed_send_email, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void GoToLogin() {
        Intent goto_login = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(ForgotPasswordActivity.this, goto_login, activityOptionsCompat.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {
        GoToLogin();
    }
}