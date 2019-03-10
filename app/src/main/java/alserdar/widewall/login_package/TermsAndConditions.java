package alserdar.widewall.login_package;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;

public class TermsAndConditions extends AppCompatActivity {

    CheckBox checkTerms ;
    Button cancelTerms , okTerms ;
    private TextView termsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        checkTerms = findViewById(R.id.checkTerms);
        termsAndConditions = findViewById(R.id.terms);
        cancelTerms = findViewById(R.id.cancelTerms);
        okTerms = findViewById(R.id.okTerms);

        SpannableString string = new SpannableString("Terms and Conditions");
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        termsAndConditions.setText(string);


        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://widewallonline.wordpress.com"));
                startActivity(intent);

            }
        });

        okTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkTerms.isChecked())
                {
                    SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(TermsAndConditions.this);
                    final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                    editor.putString("TermsAgreed" , "TermsAgreed");
                    editor.apply();
                    Intent i = new Intent(getBaseContext() , ContinueWithGoogle.class);
                    startActivity(i);
                }else
                {
                    new OurToast().myToast(getBaseContext() , getString(R.string.agree_wide_wall_terms_and_conditions));
                }
            }
        });

        cancelTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
