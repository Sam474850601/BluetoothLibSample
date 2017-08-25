package cn.samblog.bluetoothlib

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import cn.samblog.bluetoothlib.utils.BluetoothUtil
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author Sam
 */
class BluetoothClient : IBluetoothClient
{
    override fun getBondedDevice(): Set<BluetoothDevice> {
        var adapter = BluetoothAdapter.getDefaultAdapter()
        return  adapter.bondedDevices
    }

    override fun cancelScanning() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
    }

    var mOnPairedCallback:OnPairedCallback? = null

    override fun setOnPairedCallback(callback: OnPairedCallback) {
        this.mOnPairedCallback = callback
    }

    override fun pair(device:BluetoothDevice) {

        synchronized(openBloothLock)
        {
            var adapter = BluetoothAdapter.getDefaultAdapter()
            if(!adapter.isEnabled)
            {
                adapter.enable()
                openBloothLock.wait()
            }

            val state = device.getBondState()
            if (state == BluetoothDevice.BOND_NONE) {
                BluetoothUtil.createBond(device);
            }
            else
            {
                if(mOnPairedCallback is OnPairedCallback)
                {
                    mOnPairedCallback!!.onPaired(device)
                }
                else
                {

                }
            }
        }

    }

    var mPin:String? = null
    override fun setPin(pin: String) {
        this.mPin = pin
    }

    var mOnSearchCallback:OnSearchCallback? = null

    override fun setOnSearchCallback(callback: OnSearchCallback) {
        this.mOnSearchCallback = callback
    }

    override fun close() {
        var adapter = BluetoothAdapter.getDefaultAdapter()
        if(adapter.isDiscovering)
        {
            adapter.cancelDiscovery()
        }
        mContext!!.unregisterReceiver(broadcast)
        mContext = null
    }

    override fun connect(device: BluetoothDevice): BluetoothSocket {
        synchronized(openBloothLock)
        {
            var adapter = BluetoothAdapter.getDefaultAdapter()
            if(!adapter.isEnabled)
            {
                adapter.enable()
                openBloothLock.wait()
            }
            


        }
        return null!!
    }


    init {

    }

   private val broadcast =  MyBroadcast()

    private var mContext:Context? = null
    override fun init(context: Context) {
        Log.e("device", "init")
        this.mContext = context
        val intentFilter = IntentFilter()
        intentFilter.priority = 1000
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        context.registerReceiver(broadcast, intentFilter)
    }

    private val target:String?  = null
    inner class MyBroadcast : BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.getAction()
            Log.e("device",action )
            if (BluetoothAdapter.ACTION_STATE_CHANGED == action) run {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                if (BluetoothAdapter.STATE_ON == state) {
                   synchronized(openBloothLock)
                   {
                       openBloothLock.notify()
                   }
                }

            } else if (BluetoothDevice.ACTION_FOUND == action)
            {
                val bluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                mOnSearchCallback!!.onFound(bluetoothDevice,this@BluetoothClient)

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action)
            {
                val bluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                if(mOnPairedCallback is OnPairedCallback)
                {

                    when (bluetoothDevice.bondState) {

                        BluetoothDevice.BOND_BONDED -> {
                            mOnPairedCallback!!.onPaired(bluetoothDevice)
                        }
                        BluetoothDevice.BOND_NONE -> run {
                            mOnPairedCallback!!.onPaireCanceled(bluetoothDevice)
                        }
                        else -> {
                            mOnPairedCallback!!.onPairing(bluetoothDevice)
                        }
                    }
                }


            } else if (BluetoothDevice.ACTION_PAIRING_REQUEST == action) {
                if(mPin is String)
                {
                    val bluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    BluetoothUtil.setPairingConfirmation(bluetoothDevice, true)
                    abortBroadcast()
                    BluetoothUtil.setPin(bluetoothDevice,mPin!! )
                }

            }
        }
    }



    private val searchLock = Object()
    private val openBloothLock = Object()
    private var deviceOfSearching:BluetoothDevice? = null


    override fun scanning() {
        var adapter = BluetoothAdapter.getDefaultAdapter()
        synchronized(openBloothLock)
        {
            if(!adapter.isEnabled)
            {
                adapter.enable()
                openBloothLock.wait()
            }

            if(adapter.isDiscovering)
            {
                adapter.cancelDiscovery()
            }
            adapter.startDiscovery()
        }
    }


    override fun sendData(data: ByteArray): Boolean {
        return false
    }


    override fun setOnClientRecieveDataCallback(callback: OnClientRecieveDataCallback) {

    }



    override fun setUUID(uuid: UUID) {


    }




}