package ritik.encryptit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EncryptActivity extends Activity implements View.OnClickListener {
    EditText filename_et, password_et, v_password_et, burn_time_et;
    TextView filename_tv, password_tv, v_password_tv, burn_time_tv, savable_tv, validtill_tv;
    CheckBox burn_cb, savable_cb;
    Button encrypt_btn;
    FileHelper fileHelper;
    String path;
    String file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        path = getIntent().getExtras().getString("Path");
        file_name = path.substring(path.lastIndexOf("/") + 1);
        fileHelper = new FileHelper(EncryptActivity.this);
        filename_et = (EditText) findViewById(R.id.en_filename_et);
        password_et = (EditText) findViewById(R.id.en_password_et);
        v_password_et = (EditText) findViewById(R.id.en_verify_password_et);
        burn_time_et = (EditText) findViewById(R.id.en_burn_time_et);

        filename_tv = (TextView) findViewById(R.id.en_name_tv);
        password_tv = (TextView) findViewById(R.id.en_password_tv);
        v_password_tv = (TextView) findViewById(R.id.en_verify_password_tv);
        burn_time_tv = (TextView) findViewById(R.id.en_burn_tv);
        savable_tv = (TextView) findViewById(R.id.en_savable_tv);
        validtill_tv = (TextView) findViewById(R.id.en_expire_tv);

        encrypt_btn = (Button) findViewById(R.id.en_encryptit_btn);
        encrypt_btn.setOnClickListener(this);
        burn_cb = (CheckBox) findViewById(R.id.en_burn_cb);
        burn_cb.setOnClickListener(this);
        savable_cb = (CheckBox) findViewById(R.id.en_savable_cb);

        filename_et.setText(file_name);


    }

    @Override
    public void onClick(View view) {
        if (view == burn_cb) {
            if (burn_time_et.getVisibility() == View.INVISIBLE) {
                burn_time_et.setText("");
                burn_time_et.setVisibility(View.VISIBLE);
                burn_cb.setText("Enabled");
            } else {
                burn_time_et.setVisibility(View.INVISIBLE);
                burn_cb.setText("Disabled");
            }
        } else if (view == encrypt_btn) {
            if (filename_et.getText().toString().length() > 0) {
                if (password_et.getText().toString().equals(v_password_et.getText().toString())) {
                    config config = new config();
                    config.setFormat_orignal(filename_et.getText().toString());
                    if (burn_cb.isChecked())
                        config.setBurn_time(Integer.parseInt(burn_time_et.getText().toString()));
                    else
                        config.setBurn_time(0);

                    config.setSavable(savable_cb.isChecked());

                    config.setValid_time(0);

                    encryptit(config, password_et.getText().toString(), path);

                } else {
                    v_password_et.setError("Password don't match");
                }


            } else
                filename_et.setError("FileName can't be empty");


        } else
            Log.d("Ritik", "EncryptActivity : Invalid View");

    }

    public Boolean encryptit(config config, String password, String input_file_path) {
        try {
            fileHelper.gen_config_file(config);


            FileOutputStream fos = this.openFileOutput("file_main", Context.MODE_PRIVATE);
            //todo replace salt
            fileHelper.encryptfile(new FileInputStream(input_file_path), fos, password, "salt");

            FileInputStream[] files = new FileInputStream[2];
            files[0] = this.openFileInput(".config");
            files[1] = this.openFileInput("file_main");
            fos = this.openFileOutput("temp", Context.MODE_PRIVATE);
            fileHelper.zip(files, fos);
            fileHelper.encrypt_final(EncryptActivity.this);
            fileHelper.cleanup(EncryptActivity.this);

            return true;
        } catch (Exception e) {
            Log.e("Ritik", "encryptit: Exception");
            e.printStackTrace();
            return false;
        }
    }
}
