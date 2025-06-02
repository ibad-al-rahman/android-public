package com.ibadalrahman.widgets.prayertimes

import javax.inject.Inject

class HelloWorldWidgetViewModel @Inject constructor() {
    fun getLeftTexts(): List<String> {
        return listOf(
            "Text 1",
            "Text 2",
            "Text 3",
            "Text 4",
            "Text 5"
        )
    }

    fun getRightText(): String {
        return "Hello World!"
    }
}
