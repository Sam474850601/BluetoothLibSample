package cn.samblog.bluetoothlib.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

/**
 * 蓝牙工具类
 */
class BluetoothUtil
{


companion object {
    /**
     * 配对
     */
    @Throws(Exception::class)
    fun createBond(bluetoothDevice: BluetoothDevice): Boolean {
        val createBondMethod = BluetoothDevice::class.java
                .getMethod("createBond")
        return createBondMethod.invoke(bluetoothDevice) as Boolean
    }

    fun clearAllBonds() {
        val adapter = BluetoothAdapter.getDefaultAdapter() ?: return
        val deviceSet = adapter.bondedDevices
        if (null == deviceSet || deviceSet.isEmpty())
            return
        for (device in deviceSet) {
            try {
                removeBond(device)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    /**
     * 设置配对的PIN码
     */
    @Throws(Exception::class)
    fun setPin(btDevice: BluetoothDevice, pin: String): Boolean {
        val removeBondMethod = BluetoothDevice::class.java.getDeclaredMethod("setPin", *arrayOf<Class<*>>(ByteArray::class.java))
        return removeBondMethod.invoke(btDevice, pin.toByteArray()) as Boolean

    }

    @Throws(Exception::class)
    fun removeBond(btDevice: BluetoothDevice): Boolean {
        val removeBondMethod = BluetoothDevice::class.java.getMethod("removeBond")
        val returnValue = removeBondMethod.invoke(btDevice) as Boolean
        return returnValue
    }


    /**
     * 取消用户输入
     */
    @Throws(Exception::class)
    fun cancelPairingUserInput(device: BluetoothDevice): Boolean {
        val createBondMethod = BluetoothDevice::class.java.getMethod("cancelPairingUserInput")
        val returnValue = createBondMethod.invoke(device) as Boolean
        return returnValue
    }

    @Throws(Exception::class)
    fun setPairingConfirmation(device: BluetoothDevice, isConfirm: Boolean) {
        val setPairingConfirmation = BluetoothDevice::class.java.getDeclaredMethod("setPairingConfirmation", Boolean::class.javaPrimitiveType)
        setPairingConfirmation.invoke(device, isConfirm)
    }
}



}