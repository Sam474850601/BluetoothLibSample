package cn.samblog.bluetoothlib

import android.bluetooth.BluetoothDevice

/**
 * 配对回调
 * @author Sam
 */
interface  OnPairedCallback
{
    fun onPaired(device: BluetoothDevice)
    fun onPaireCanceled(device: BluetoothDevice)
    fun onPairing(device: BluetoothDevice)
}