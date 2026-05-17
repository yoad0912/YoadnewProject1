            package com.example.yoadnewproject1;

            import android.os.Bundle;

            import androidx.activity.EdgeToEdge;
            import androidx.appcompat.app.AppCompatActivity;
            import androidx.core.graphics.Insets;
            import androidx.core.view.ViewCompat;
            import androidx.core.view.WindowInsetsCompat;
            import android.content.SharedPreferences;
            import android.widget.TextView;
            import android.widget.LinearLayout;
            import android.app.AlertDialog;
            import android.widget.Button;
            import androidx.appcompat.widget.Toolbar;
            import android.content.Intent;
            import android.net.Uri;
            import android.widget.Toast;

            public class StatsActivity extends AppCompatActivity {

                TextView tvMatches, tvGoals, tvAssists;
                LinearLayout layoutGamesList;
                SharedPreferences sp;
                Button btnDelete;
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    EdgeToEdge.enable(this);
                    setContentView(R.layout.activity_stats);
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                        return insets;
                    });

                    tvMatches = findViewById(R.id.tvMatches);
                    tvGoals = findViewById(R.id.tvGoals);
                    tvAssists = findViewById(R.id.tvAssists);
                    layoutGamesList = findViewById(R.id.layoutGamesList);
                    btnDelete = findViewById(R.id.btnDelete);

                    sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                    int matches = sp.getInt("matches", 0);
                    int goals = sp.getInt("goals", 0);
                    int assists = sp.getInt("assists", 0);
                    String matchesList = sp.getString("matchesList", "");
                    tvMatches.setText("משחקים: " + matches);
                    tvGoals.setText("שערים: " + goals);
                    tvAssists.setText("בישולים: " + assists);
                    String[] allMatches = matchesList.split("\n");

                    for (String match : allMatches) {
                        if (!match.equals("")) {
                            TextView tv = new TextView(this);
                            tv.setText(match);
                            tv.setTextSize(16);
                            tv.setPadding(0, 10, 0, 10);

                            layoutGamesList.addView(tv);
                        }
                    }
                    btnDelete.setOnClickListener(v -> {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("מחיקת נתונים");
                        builder.setMessage("האם אתה בטוח שברצונך למחוק את כל הנתונים?");

                        builder.setPositiveButton("כן", (dialog, which) -> {

                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();

                            tvMatches.setText("משחקים: 0");
                            tvGoals.setText("שערים: 0");
                            tvAssists.setText("בישולים: 0");

                            layoutGamesList.removeAllViews();

                        });

                        builder.setNegativeButton("לא", null);

                        builder.show();
                    });
                }
                @Override
                public boolean onCreateOptionsMenu(android.view.Menu menu) {
                    getMenuInflater().inflate(R.menu.menu_main, menu);
                    return true;
                }

                @Override
                public boolean onOptionsItemSelected(android.view.MenuItem item) {

                    if (item.getItemId() == R.id.menu_main) {
                        startActivity(new Intent(this, MainActivity.class));
                    }

                    if (item.getItemId() == R.id.menu_stats) {
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