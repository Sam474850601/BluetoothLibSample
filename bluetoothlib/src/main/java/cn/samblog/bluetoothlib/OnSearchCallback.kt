package cn.samblog.bluetoothlib

import android.bluetooth.BluetoothDevice

/**
 * 搜索获取到的蓝牙回调接口
 * @author Sam
 */
interface  OnSearchCallback
{
    fun onFound(device:BluetoothDevice, client: IBluetoothClient)
}