package cn.samblog.bluetoothlib

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import java.util.*

/**
 *
 */
interface IBluetoothClient
{
    fun init(context: Context)
    fun scanning()
    fun cancelScanning()
    fun pair(device:BluetoothDevice)
    fun setOnPairedCallback(callback: OnPairedCallback)
    fun setPin(pin:String)
    fun setOnSearchCallback(callback:OnSearchCallback)
    fun getBondedDevice():Set<BluetoothDevice>
    fun connect(device:BluetoothDevice):BluetoothSocket
    fun setUUID(uuid: UUID)
    fun setOnClientRecieveDataCallback(callback:OnClientRecieveDataCallback)
    fun sendData(data:ByteArray):Boolean
    fun close()
}