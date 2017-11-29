package jacksonmeyer.com.archat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import jacksonmeyer.com.archat.ViewHolders.CustomPrimaryLanguagesAdapter;

public class ChooseLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

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

        CustomPrimaryLanguagesAdapter mCustomPrimaryLanguagesAdapter = new CustomPrimaryLanguagesAdapter(ChooseLanguageActivity.this, spinnerTitles, spinnerImages, spinnerPopulation);
        mSpinner.setAdapter(mCustomPrimaryLanguagesAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                Log.d("main", isoCode[i]);
//                take selection and save to database
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }
        });
    }
}
