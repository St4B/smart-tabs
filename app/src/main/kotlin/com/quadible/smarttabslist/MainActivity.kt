package com.quadible.smarttabslist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quadible.smarttabslist.ui.theme.SmartTabsListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTabsListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SmartTabsListWithData()
                }
            }
        }
    }
}

@Composable
fun SmartTabsListWithData() {
    SmartTabsList(
        smartTabsContent = generateContent(),
        isTab = { it is TabData.Header },
        smartTab = { tab, _ ->
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = tab.title,
                style = MaterialTheme.typography.h6
            )
        },
        smartItem = {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = it.title,
                style = MaterialTheme.typography.body1
            )
        }
    )
}

private fun generateContent(): List<TabData> = buildList {
    repeat(100) {
        if (it % 15 == 0) {
            add(TabData.Header("Header - $it"))
        } else {
            add(TabData.Item("Item - $it"))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartTabsListTheme {
        SmartTabsListWithData()
    }
}