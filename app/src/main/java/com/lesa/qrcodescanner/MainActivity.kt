package com.lesa.qrcodescanner

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.lesa.qrcodescanner.data.MainDB
import com.lesa.qrcodescanner.data.Product
import com.lesa.qrcodescanner.ui.theme.QRCodeScannerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var mainDB: MainDB

    var counter = 0

    private val scanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (!result.contents.isNullOrEmpty()) {
            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        } else {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val productStateList = mainDB.dao.getAllProducts().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            QRCodeScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.9f)
                        ) {
                            items(productStateList.value) {product ->
                                Text(
                                    text = product.name,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                    )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    mainDB.dao.insertProduct(
                                        Product(
                                            id = null,
                                            name = "Product # ${counter++}",
                                            numberQR = "sdssdd"
                                        )
                                    )
                                }
                                //scan()
                            }
                        ) {
                            Text(text = "Scan")
                        }
                    }
                }
            }
        }
    }

    private fun scan() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        scanLauncher.launch(options)
    }
}
