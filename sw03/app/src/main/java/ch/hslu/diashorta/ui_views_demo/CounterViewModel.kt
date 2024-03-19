package ch.hslu.diashorta.ui_views_demo

import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    private var counter = 0

    fun increment() {
        counter++
    }

    fun get(): Int {
        return counter
    }

    fun getButtonName(): String {
        return "ViewModel: $counter++"
    }
}