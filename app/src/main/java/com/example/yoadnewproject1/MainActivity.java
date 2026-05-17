package com.example.yoadnewproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.net.Uri;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class MainActivity extends AppCompatActivity {


    ImageView imgPlayer;
    ActivityResultLauncher<String> imageLauncher;
    Button btnGoToStats, btnAddMatch, btnChooseImage ;
    EditText etResult, etGoals, etAssists;
    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnGoToStats = findViewById(R.id.btnGoToStats);
        etResult = findViewById(R.id.etResult);
        etGoals = findViewById(R.id.etGoals);
        etAssists = findViewById(R.id.etAssists);
        btnAddMatch = findViewById(R.id.btnAddMatch);
        imgPlayer = findViewById(R.id.imgPlayer);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedImage = sp.getString("playerImage", "");
        if (!savedImage.equals("")) {
            imgPlayer.setImageURI(Uri.parse(savedImage));
        }

        imageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imgPlayer.setImageURI(uri);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("playerImage", uri.toString());
                        editor.commit();
                    }
                }
        );

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLauncher.launch("image/*");
            }
        });
        btnGoToStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        btnAddMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = etResult.getText().toString();
                String goalsStr = etGoals.getText().toString();
                String assistsStr = etAssists.getText().toString();

                if (result.equals("") || goalsStr.equals("") || assistsStr.equals("")) {
                    Toast.makeText(MainActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int goals = Integer.parseInt(goalsStr);
                int assists = Integer.parseInt(assistsStr);

                int totalMatches = sp.getInt("matches", 0);
                int totalGoals = sp.getInt("goals", 0);
                int totalAssists = sp.getInt("assists", 0);
                String matchesList = sp.getString("matchesList", "");

                totalMatches++;
                totalGoals += goals;
                totalAssists += assists;

                matchesList += "Result: " + result +
                        " | Goals: " + goals +
                        " | Assists: " + assists + "\n";

                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("matches", totalMatches);
                editor.putInt("goals", totalGoals);
                editor.putInt("assists", totalAssists);
                editor.putString("matchesList", matchesList);
                editor.commit();

                Toast.makeText(MainActivity.this, "Match saved!", Toast.LENGTH_SHORT).show();

                etResult.setText("");
                etGoals.setText("");
                etAssists.setText("");
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (item.getItemId() == R.id.menu_stats) {
            startActivity(new Intent(this, StatsActivity.class));
        }

        if (item.getItemId() == R.id.menu_main) {
            // כבר כאן
        }

        if (item.getItemId() == R.id.menu_about) {
            Toast.makeText(this, "MyFootballStats - אפליקציה למעקב אחרי משחקי כדורגל", Toast.LENGTH_LONG).show();
        }

        if (item.getItemId() == R.id.menu_results) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    android.net.Uri.parse("https://www.365scores.com"));
            startActivity(browserIntent);
        }

        return true;
    }
}
