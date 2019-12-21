package com.androiddevs.intentchooser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvFilename = findViewById(R.id.tvFilename);
        TextView tvContent = findViewById(R.id.tvContent);

        Intent textIntent = getIntent();
        if(textIntent.getAction() != null && textIntent.getAction().equals(Intent.ACTION_VIEW)) {
            String scheme = textIntent.getScheme();

            if(scheme != null && scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                Uri uri = textIntent.getData();
                String filename = getFilenameFromContentUri(uri);
                String fileContent = readFileContent(uri);

                tvFilename.setText(filename);
                tvContent.setText(fileContent);
            }
        }
    }

    private String readFileContent(Uri uri) {
        ContentResolver resolver = getContentResolver();
        StringBuilder sb = new StringBuilder();
        try {
            InputStream in = resolver.openInputStream(uri);
            int i;
            while((i = in.read()) != -1) {
                char c = (char) i;
                sb.append(c);
            }

        } catch(FileNotFoundException e) {
            Toast.makeText(this,
                    "File couldn't be found", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            Toast.makeText(this,
                    e.toString(), Toast.LENGTH_SHORT).show();
        }
        return sb.toString();
    }

    private String getFilenameFromContentUri(Uri uri) {
        ContentResolver resolver = getContentResolver();
        Cursor returnCursor = resolver.query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String filename = returnCursor.getString(nameIndex);
        returnCursor.close();
        return filename;
    }
    
}
