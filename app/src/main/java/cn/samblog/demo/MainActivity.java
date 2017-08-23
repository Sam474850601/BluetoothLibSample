package cn.samblog.demo;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.samblog.bluetoothlib.BluetoothClient;
import cn.samblog.bluetoothlib.IBluetoothClient;
import cn.samblog.bluetoothlib.OnPairedCallback;
import cn.samblog.bluetoothlib.OnSearchCallback;

public class MainActivity extends AppCompatActivity implements OnSearchCallback, OnPairedCallback{

    IBluetoothClient bloothClient = new BluetoothClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        else
        {
            _initClient();
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        bloothClient.init(getApplicationContext());
        bloothClient.scanning();
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                _initClient();
            }
        }
    }

    private void _initClient() {
        bloothClient.init(getApplicationContext());
        bloothClient.setOnSearchCallback(this);
        bloothClient.setOnPairedCallback(this);
        bloothClient.scanning();

    }


    @Override
    protected void onDestroy() {
        bloothClient.close();
        super.onDestroy();
    }


    @Override
    public void onFound(BluetoothDevice device, IBluetoothClient client) {
        Log.e("device", "onfound :"+device.getName());
        if(device.getName().contains("MEIZU"))
        {
            client.cancelScanning();
            client.pair(device);
        }
    }

    @Override
    public void onPaired(BluetoothDevice device) {
        Log.e("device", "onPaired :"+device);
    }

    @Override
    public void onPairing(BluetoothDevice device) {
        Log.e("device", "onPairing :"+device);
    }

    @Override
    public void onPaireCanceled(BluetoothDevice device) {
        Log.e("device", "onCancelPaired :"+device);
    }
}
