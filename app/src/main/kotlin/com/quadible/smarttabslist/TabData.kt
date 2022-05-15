package com.quadible.smarttabslist

sealed class TabData(open val title: String) {

    data class Header(override val title: String) : TabData(title = title)

    data class Item(override val title: String) : TabData(title = title)
}
