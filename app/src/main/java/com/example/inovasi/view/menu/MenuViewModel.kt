package com.example.inovasi.view.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {

    enum class Page {
        HOME,
        TASK,
        GALLERY,
        ACCOUNT,
    }

    private val _page = MutableStateFlow(Page.HOME)

    val page : StateFlow<Page> = _page

    fun setPage(page : Page) {
        _page.value = page
    }
}