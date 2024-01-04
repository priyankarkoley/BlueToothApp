package com.example.simpbluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.simpbluetooth.ui.theme.SimpBluetoothTheme

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
//
//    private late init var btManager : BluetoothManager
//    private late init var btAdapter : BluetoothAdapter
//    private late init var reqPermission : ActivityResultLauncher<String>
//    private late init var takeResultLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        val takeResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK) Toast.makeText(
                    applicationContext,
                    "Permission granted & BL turned ON",
                    Toast.LENGTH_SHORT
                ).show()
                else Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        val reqPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//            if (it) {
//                Toast.makeText(applicationContext, "BL unavailable", Toast.LENGTH_SHORT).show()
//            } else {
//                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                takeResultLauncher.launch(intent)
//            }
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            takeResultLauncher.launch(intent)
        }


        setContent {
            SimpBluetoothTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Column (horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "BlueTooth Manager", color = Color.Black)
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(onClick = {
                                Log.v("btn", "ON")
                                reqPermission.launch(Manifest.permission.BLUETOOTH_CONNECT)
                            }) {
                                Text(text = "Turn ON")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                Log.v("btn", "OFF")
                                if (ActivityCompat.checkSelfPermission(
                                        applicationContext,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    Toast.makeText(applicationContext, "Turned OFF", Toast.LENGTH_SHORT).show()
                                    btAdapter.disable()
                                }
                            }) {
                                Text(text = "Turn OFF")
                            }
                        }
                    }
                }
            }
        }
    }
}