package jacksonmeyer.com.archat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacksonmeyer.com.archat.ViewHolders.CustomPrimaryLanguagesAdapter;

public class ChooseLanguageActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.nextButton)
    Button mNextButton;

    private DatabaseReference mUserReference;
    private FirebaseAuth mAuth;
    private String currentUserUid;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    String learningLanguageISO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        mUserReference = FirebaseDatabase.getInstance().getReference("users");

        getCurrentUserUid();

        final String[] spinnerTitles;
        final String[] isoCode;
        String[] spinnerPopulation;
        int[] spinnerImages;

        spinnerTitles = new String[]{"arabic", "dutch", "French", "German", "Haitian Creole", "Hawaiian", "Italian", "Japanese", "Portuguese", "Spanish", "Swahili"};
        isoCode = new String[]{"ar", "nl", "fr", "de", "ht", "haw", "it", "ja", "pt", "es", "sw"};
        spinnerPopulation = new String[]{"10.3M learners", "1.3M learners", "11.2M learners", "1.2M learners", "1.31M learners", "4.2M learners", "899K learners", "455K learners", "4.9M learners", "122.6M learners", "122.2K"};
        spinnerImages = new int[]{
                  R.drawable.arabic
                , R.drawable.dutch
                , R.drawable.france
                , R.drawable.germany
                , R.drawable.haiti
                , R.drawable.hawaii
                , R.drawable.italian
                , R.drawable.japan
                , R.drawable.portuguese
                , R.drawable.spain
                , R.drawable.swahili
        };

        Spinner mSpinner =(Spinner)findViewById(R.id.spinner);
        mNextButton.setOnClickListener(this);

        CustomPrimaryLanguagesAdapter mCustomPrimaryLanguagesAdapter = new CustomPrimaryLanguagesAdapter(ChooseLanguageActivity.this, spinnerTitles, spinnerImages, spinnerPopulation);
        mSpinner.setAdapter(mCustomPrimaryLanguagesAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                learningLanguageISO = isoCode[i];
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }
        });
    }

    public void getCurrentUserUid() {
        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();

        addCurrentUserUidToSharedPreferences(currentUserUid);
    }

    private void addCurrentUserUidToSharedPreferences(String uid) {
        mEditor.putString(Constants.SHARED_PREFERENCES_KEY_LOGGED_IN_UID, uid).apply();
    }

    @Override
    public void onClick(View view) {
        if (view == mNextButton) {
            if (learningLanguageISO.length() <= 1) {

            } else {
                mUserReference.child(currentUserUid).child("learningLanguageISO").setValue(learningLanguageISO);
                Intent intent = new Intent(ChooseLanguageActivity.this, MessagesActivity.class);
                startActivity(intent);
            }
        }
    }
}
