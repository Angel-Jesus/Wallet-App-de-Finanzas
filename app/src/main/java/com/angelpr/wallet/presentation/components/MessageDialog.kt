package com.angelpr.wallet.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angelpr.wallet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDialog(
    onDismissRequest: () -> Unit,
    positiveButton: () -> Unit,
    textPositive: String = "Confirmar",
    textNegative: String = "Cancelar",
    title: String,
    text: String
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally),
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_warning),
                    contentDescription = "Error"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally),
                    text = title,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = text,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                ){
                    TextButton(
                        onClick = onDismissRequest,

                    ) {
                        Text(textNegative)
                    }

                    TextButton(
                        onClick = positiveButton,
                    ) {
                        Text(textPositive)
                    }
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageDialogPreview() {
    MessageDialog(
        onDismissRequest = {},
        positiveButton = {},
        title = "Eliminar tarjeta",
        text = "Si elimina la tarjeta se eliminarán todos los registros de deuda vinculadas a ella. ¿Desea continuar?"
    )

}