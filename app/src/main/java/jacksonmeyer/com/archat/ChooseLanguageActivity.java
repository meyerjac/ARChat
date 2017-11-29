package jacksonmeyer.com.archat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import jacksonmeyer.com.archat.ViewHolders.CustomAdapter;

public class ChooseLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_activity);

        final String[] spinnerTitles;
        String[] spinnerPopulation;
        int[] spinnerImages;

        spinnerTitles = new String[]{"Australia", "Brazil", "China", "France", "Germany", "India", "Ireland", "Italy", "Mexico", "Poland"};
        spinnerPopulation = new String[]{"24.13 Million", "207.7 Million", "1.379 Billion", "66.9 Million", "82.67 Million", "1.324 Billion", "4.773 Million", "60.6 Million", "127.5 Million", "37.95 Million"};
        spinnerImages = new int[]{R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar
                , R.drawable.avatar};

        Spinner mSpinner =(Spinner)findViewById(R.id.spinner);

        CustomAdapter mCustomAdapter = new CustomAdapter(ChooseLanguageActivity.this, spinnerTitles, spinnerImages, spinnerPopulation);
        mSpinner.setAdapter(mCustomAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                //Toast.makeText(ChooseLanguageActivity.this, spinnerTitles[i], Toast.LENGTH_SHORT).show();
                //take selection and save to database
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }

        });
    }
}
