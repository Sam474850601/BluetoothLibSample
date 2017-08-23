package cn.samblog.bluetoothlib

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import java.util.*

/**
 *
 */
interface IBluetoothServer
{
    fun setUUID(uuid:String)
    fun start()
    fun onStart()
    fun onDestroy()
    fun getAllDevices():Vector<BluetoothDevice>
    fun onDeviceConnected(device:BluetoothDevice)
    fun onDeviceDisconnected(device:BluetoothDevice)
    fun setOnServerRecieveDataCallback(callback:OnClientRecieveDataCallback)
    fun sendData(data:ByteArray,device:BluetoothDevice)
}