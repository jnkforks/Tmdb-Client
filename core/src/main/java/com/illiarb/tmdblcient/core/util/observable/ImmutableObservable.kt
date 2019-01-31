package com.illiarb.tmdblcient.core.util.observable

import com.illiarb.tmdblcient.core.util.Cloneable

/**
 * @author ilya-rb on 20.01.19.
 */
class ImmutableObservable<T : Cloneable<T>>(
    initial: T,
    private val observable: Observable<T>
) : Observable<T> by observable {

    private var currentValue: T = initial

    override fun accept(value: T) {
        if (currentValue === value && currentValue.hashCode() != value.hashCode()) {
            throw IllegalStateException("State is immutable, you need to provide a new object")
        }

        currentValue = value

        observable.accept(value)
    }

    fun value(): T = currentValue.clone()
}