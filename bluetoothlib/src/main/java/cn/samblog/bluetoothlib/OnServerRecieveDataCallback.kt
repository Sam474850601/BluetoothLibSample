package cn.samblog.bluetoothlib

import android.bluetooth.BluetoothDevice

/**
 *
 */
interface OnServerRecieveDataCallback
{
    fun onRecieved(device: BluetoothDevice, data:ByteArray)
}