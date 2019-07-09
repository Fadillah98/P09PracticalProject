package com.myapplicationdev.android.p09practicalproject;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    EditText etCoordinates;
    TextView tvRead;
    Button btnSave, btnRead, btnMap;
    String exFolderLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck_Write = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionCheck_Read = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_Write != PermissionChecker.PERMISSION_GRANTED || permissionCheck_Read != PermissionChecker.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Permission is not granted!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            finish();
        }

        etCoordinates = findViewById(R.id.etCoordinates);
        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);
        tvRead = findViewById(R.id.tvRead);
        btnMap = findViewById(R.id.btnMap);

        exFolderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
        final File exfolder = new File(exFolderLocation);
        if (exfolder.exists() == false){
            boolean result = exfolder.mkdir();
            if(result == true){
                Log.d("File Read/Write", "Folder created");
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    exFolderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                    File targetFile = new File(exFolderLocation, "quiz.txt");
                    FileWriter writer = new FileWriter(targetFile, false);
                    writer.write(etCoordinates.getText().toString());
                    writer.flush();
                    writer.close();
                    Toast.makeText(MainActivity.this, "Write Successful!", Toast.LENGTH_SHORT).show();
                    etCoordinates.setText("");
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                File targetFile = new File(folderLocation, "quiz.txt");

                if (targetFile.exists() == true){
                    String data ="";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null){
                            data += line + "\n";
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    tvRead.setText(data);
                }
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                String[] arrOfCoordinates = tvRead.getText().toString().split(",");
                i.putExtra("lat", Double.parseDouble(arrOfCoordinates[0]));
                i.putExtra("long", Double.parseDouble(arrOfCoordinates[1]));
                startActivity(i);
            }
        });

    }
}
