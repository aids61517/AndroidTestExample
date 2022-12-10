package com.aids61517.androidtestexample.series5

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class SingleLiveEvent<T> : LiveData<T>() {
    private class ObserverWrapper<T>(
        val observer: Observer<in T>,
    ) : Observer<T> {

        private var valueUpdated = false

        override fun onChanged(t: T) {
            if (valueUpdated) {
                valueUpdated = false
                observer.onChanged(t)
            }
        }

        fun onValueUpdated() {
            valueUpdated = true
        }
    }

    private val observers = mutableListOf<ObserverWrapper<T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        observers
            .filter { wrapper -> wrapper.observer == observer || wrapper == observer }
            .forEach {
                observers.remove(it)
                super.removeObserver(it)
            }
    }

    @MainThread
    public override fun setValue(t: T) {
        observers.forEach { it.onValueUpdated() }
        super.setValue(t)
    }

    public override fun postValue(value: T) {
        observers.forEach { it.onValueUpdated() }
        super.postValue(value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(): T = super.getValue() as T
}
