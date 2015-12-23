package xyz.also.clockplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import xyz.also.clockplugin.service.UpdateService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, UpdateService.class));
    }
}
