package com.leon.filepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

import com.leon.filepicker.R;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.consts.ExtraConsts;
import com.leon.lfilepickerlibrary.utils.StringUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "FilePickerLeon";
    private EditText mEtStartPath;
    private EditText mEtFileType;
    private CheckBox mCbIsChooseFile;
    private CheckBox mCbIsMultiMode;
    private CheckBox mCbIsGraterThanStandard;
    private EditText mEtStandardFileSize;
    private EditText mEtMaxNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        mEtStartPath = findViewById(R.id.et_start_path_input);
        mEtFileType = findViewById(R.id.et_file_types_input);
        mCbIsChooseFile = findViewById(R.id.cb_is_choose_file);
        mCbIsMultiMode = findViewById(R.id.cb_is_multi_mode);
        mCbIsGraterThanStandard = findViewById(R.id.cb_is_grater_than_standard);
        mEtStandardFileSize = findViewById(R.id.et_standard_size_input);
        mEtMaxNum = findViewById(R.id.et_select_max_num);

        mEtStartPath.setText(Environment.getExternalStorageDirectory() + "/");
        mEtStartPath.setSelection(mEtStartPath.getText().length());
        mEtFileType.setText("");
        mCbIsChooseFile.setChecked(true);
        mCbIsMultiMode.setChecked(true);
        mCbIsGraterThanStandard.setChecked(true);
        mEtStandardFileSize.setText("0");
        mEtMaxNum.setText("5");
    }

    public void openFromActivity(View view) {
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(Consant.REQUESTCODE_FROM_ACTIVITY)
                .withTitle("文件选择")
                .withMultiMode(mCbIsMultiMode.isChecked())
                .withMaxNum(Integer.valueOf(StringUtils.isEmpty(mEtMaxNum.getText()) ? "1" : mEtMaxNum.getText().toString()))
                .withStartPath(mEtStartPath.getText().toString())
                .withNotFoundTips("至少选择一个文件")
                .withShouldLarger(mCbIsGraterThanStandard.isChecked())
                .withStandardFileSize(Long.valueOf(StringUtils.isEmpty(mEtStandardFileSize.getText()) ? "0" : mEtStandardFileSize.getText().toString()))
                .withChooseMode(mCbIsChooseFile.isChecked())
                .withFileFilter(new String[]{mEtFileType.getText().toString()})
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path;
            if (requestCode == Consant.REQUESTCODE_FROM_ACTIVITY) {
                List<String> paths = data.getStringArrayListExtra(ExtraConsts.EXTRA_FILE_PATHS);
                if (mCbIsMultiMode.isSelected()) {
                    StringBuilder temp = new StringBuilder();
                    for (String p : paths) {
                        temp.append(p).append(";");
                    }
                    path = temp.toString();
                } else {
                    path = paths.get(0);
                    Toast.makeText(getApplicationContext(), "选中的路径为" + path, Toast.LENGTH_SHORT).show();
                }
                Log.i("Selected Paths are: ", path);
            }
        }
    }
}
