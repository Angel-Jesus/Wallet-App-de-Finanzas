package com.angelpr.wallet

import android.Manifest.permission
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.angelpr.wallet.presentation.navigation.NavManager
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.WalletTheme
import com.angelpr.wallet.utils.Constants.MY_CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<WalletViewModel>()

    private val permissions = getPermission()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Init Splash Screen
        installSplashScreen().apply {
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 1000L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        enableEdgeToEdge()
        createChannel()
        setContent {
            WalletTheme {

                val showDialog = viewModel.showDialog.collectAsState().value
                val launchSetting = viewModel.launchSetting.collectAsState().value

                val permissionsLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { result ->
                        permissions.forEach {permission ->
                            if(result[permission] == false){
                                if(!shouldShowRequestPermissionRationale(permission)){
                                    viewModel.updateLaunchSetting(true)
                                }
                                viewModel.updateShowDialog(true)
                            }
                        }
                    }
                )

                LaunchedEffect(Unit) {
                    permissions.forEach { permission ->
                        val isGranted = checkSelfPermission(permission) ==
                                PackageManager.PERMISSION_GRANTED
                        if(!isGranted){
                            if(shouldShowRequestPermissionRationale(permission)){
                                viewModel.updateShowDialog(true)
                            }else{
                                permissionsLauncher.launch(permissions)
                            }

                        }
                    }
                }

                MaterialTheme {

                    NavManager(viewModel)

                    if(showDialog){
                        PermissionDialog(
                            onDismiss = { viewModel.updateShowDialog(false) },
                            onConfirm = {
                                viewModel.updateShowDialog(false)
                                if(launchSetting){
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", packageName, null)
                                    ).also {
                                        startActivity(it)
                                    }
                                    viewModel.updateLaunchSetting(false)
                                }else{
                                    permissionsLauncher.launch(permissions)
                                }

                            },
                            title = "Es necesario el acceso a las notificaciones",
                            text = "Esta aplicacion necesita el acceso a notificaciones para poder trabajar con ellas"
                        )
                    }

                }
            }
        }
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            MY_CHANNEL_ID,
            "AlarmChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Alarm Channel"
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PermissionDialog(
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
        title: String,
        text: String
    ) {
        BasicAlertDialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 4.dp)
                ) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = text,
                        color = Color.Black,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = onConfirm,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("OK")
                    }
                }
            }
        }

    }

    private fun getPermission(): Array<String> {
        val permissionsList = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(permission.POST_NOTIFICATIONS)
            permissionsList.add(permission.USE_EXACT_ALARM)
        }

        return permissionsList.toTypedArray()

    }

}


