package ritik.encryptit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.List;


public class MainActivity extends Activity {


    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileHelper helper = new FileHelper(MainActivity.this);
        URIhelper urIhelper = new URIhelper();
        Intent intent = getIntent();
        Log.d("Ritik", "Signature is : " + new TamperCheck().getAppSignature(MainActivity.this));
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (uri != null) {
                try {
                    EncorDec(urIhelper.getFilePath(MainActivity.this, uri), urIhelper.getfilename(urIhelper.getFilePath(MainActivity.this, uri)));
                } catch (Exception e) {

                }

            }
        }
        //TODO handle onpermission
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This always works
                Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                // This works if you defined the intent filter
                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                // Configure initial directory by specifying a String.
                // You could specify a String like "/storage/emulated/0/", but that can
                // dangerous. Always use Android's API calls to get paths to the SD-card or
                // internal memory.
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, 0);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(intent);
            for (Uri uri : files) {
                File file = Utils.getFileForUri(uri);
                EncorDec(file.getPath(), file.getName());
            }
        }
    }


    protected void EncorDec(String path, String File_name) {
        Intent intent;
        Log.d("Ritik", "EncorDec: Path is " + path);

        if (File_name.contains(new Constants().format)) {
            intent = new Intent(MainActivity.this, DecryptActivity.class);
        } else {
            intent = new Intent(MainActivity.this, EncryptActivity.class);
        }
        intent.putExtra("Path", path);
        intent.putExtra("File", File_name);
        startActivity(intent);
        finish();
    }


}
