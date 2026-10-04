package com.demo.event.ui.events

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.demo.event.location.LocationManager
import kotlinx.coroutines.launch


@Composable
fun EventListScreen(
) {


    val context = LocalContext.current

    var userLatitude by remember {
        mutableStateOf<Double?>(null)
    }

    var userLongitude by remember {
        mutableStateOf<Double?>(null)
    }

    val locationManager =
        remember {
            LocationManager(context)
        }

    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||
                        permissions[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (granted) {

                coroutineScope.launch {
                    val location =
                        locationManager
                            .getCurrentLocation()

                    userLatitude =
                        location?.latitude

                    userLongitude =
                        location?.longitude
                }
            }
        }

    LaunchedEffect(Unit) {

        val fineGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {

            val location =
                locationManager
                    .getCurrentLocation()

            userLatitude =
                location?.latitude

            userLongitude =
                location?.longitude

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }




}

