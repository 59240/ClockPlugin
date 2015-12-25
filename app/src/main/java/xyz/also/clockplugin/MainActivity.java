package xyz.also.clockplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import xyz.also.clockplugin.service.UpdateService;

public class MainActivity extends Activity {

    private SharedPreferences share;
    private Editor editor;
    private RadioButton[] transparentChoose;
    private RadioButton[] textArray;
    private RadioButton[] bgArray;
    private View[] textView;
    private View[] bgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startService(new Intent(MainActivity.this, UpdateService.class));
    }

    private void init() {

        share = getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = share.edit();
        ColorCheckedChange checkedListener = new ColorCheckedChange();
        ClickListener clickListener = new ClickListener();

        RadioGroup transparentGroup = (RadioGroup) findViewById(R.id.transparentGroup);
        transparentGroup.setOnCheckedChangeListener(checkedListener);
        RadioGroup textGroup = (RadioGroup) findViewById(R.id.textGroup);
        textGroup.setOnCheckedChangeListener(checkedListener);
        RadioGroup bgGroup = (RadioGroup) findViewById(R.id.bgGroup);
        bgGroup.setOnCheckedChangeListener(checkedListener);

        transparentChoose = new RadioButton[]{(RadioButton) findViewById(R.id.bgTransparent),
                (RadioButton) findViewById(R.id.bgTranslucent),
                (RadioButton) findViewById(R.id.bgOpaque)};
        textArray = new RadioButton[]{(RadioButton) findViewById(R.id.textButton_1),
                (RadioButton) findViewById(R.id.textButton_2),
                (RadioButton) findViewById(R.id.textButton_3),
                (RadioButton) findViewById(R.id.textButton_4),
                (RadioButton) findViewById(R.id.textButton_5),
                (RadioButton) findViewById(R.id.textButton_6)};
        bgArray = new RadioButton[]{(RadioButton) findViewById(R.id.bgButton_1),
                (RadioButton) findViewById(R.id.bgButton_2),
                (RadioButton) findViewById(R.id.bgButton_3),
                (RadioButton) findViewById(R.id.bgButton_4),
                (RadioButton) findViewById(R.id.bgButton_5),
                (RadioButton) findViewById(R.id.bgButton_6)};
        textView = new View[]{findViewById(R.id.textColor_1),
                findViewById(R.id.textColor_2),
                findViewById(R.id.textColor_3),
                findViewById(R.id.textColor_4),
                findViewById(R.id.textColor_5),
                findViewById(R.id.textColor_6)};
        bgView = new View[]{findViewById(R.id.bgColor_1),
                findViewById(R.id.bgColor_2),
                findViewById(R.id.bgColor_3),
                findViewById(R.id.bgColor_4),
                findViewById(R.id.bgColor_5),
                findViewById(R.id.bgColor_6)};

        for (View view : textView) {
            view.setOnClickListener(clickListener);
        }
        for (View view : bgView) {
            view.setOnClickListener(clickListener);
        }

        transparentChoose[share.getInt("transparentChoose", 0)].setChecked(true);
        textArray[share.getInt("textColorChoose", 0)].setChecked(true);
        bgArray[share.getInt("bgColorChoose", 2)].setChecked(true);
    }

    private class ColorCheckedChange implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.textButton_1:
                    editor.putInt("textColorChoose", 0);
                    break;
                case R.id.textButton_2:
                    editor.putInt("textColorChoose", 1);
                    break;
                case R.id.textButton_3:
                    editor.putInt("textColorChoose", 2);
                    break;
                case R.id.textButton_4:
                    editor.putInt("textColorChoose", 3);
                    break;
                case R.id.textButton_5:
                    editor.putInt("textColorChoose", 4);
                    break;
                case R.id.textButton_6:
                    editor.putInt("textColorChoose", 5);
                    break;
                case R.id.bgButton_1:
                    editor.putInt("bgColorChoose", 0);
                    break;
                case R.id.bgButton_2:
                    editor.putInt("bgColorChoose", 1);
                    break;
                case R.id.bgButton_3:
                    editor.putInt("bgColorChoose", 2);
                    break;
                case R.id.bgButton_4:
                    editor.putInt("bgColorChoose", 3);
                    break;
                case R.id.bgButton_5:
                    editor.putInt("bgColorChoose", 4);
                    break;
                case R.id.bgButton_6:
                    editor.putInt("bgColorChoose", 5);
                    break;
                case R.id.bgTransparent:
                    editor.putInt("transparentChoose", 0);
                    break;
                case R.id.bgTranslucent:
                    editor.putInt("transparentChoose", 1);
                    break;
                case R.id.bgOpaque:
                    editor.putInt("transparentChoose", 2);
                    break;
            }
            editor.commit();
        }
    }
    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            for (int i = 0; i < 6; i++) {
                if (v.equals(textView[i])) {
                    textArray[i].setChecked(true);
                    return;
                }
            }
            for (int i = 0; i < 6; i++) {
                if (v.equals(bgView[i])) {
                    bgArray[i].setChecked(true);
                    return;
                }
            }
        }
    }
}
