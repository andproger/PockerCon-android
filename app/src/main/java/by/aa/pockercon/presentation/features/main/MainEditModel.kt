package by.aa.pockercon.presentation.features.main

class MainEditModel(
    var personCount: CachedInputModel<Int>
) {
    companion object {
        val DEFAULT = MainEditModel(
            personCount = CachedInputModel(0, false)
        )
    }
}

class CachedInputModel<T>(
    private var value: T,
    private var updated: Boolean
) {
    fun get(): T = value

    fun input(value: T) {
        this.value = value
        updated = true
    }

    fun complete(value: T) {
        if (this.value == value) {
            updated = false
        }
    }

    fun render(value: T, onChanged: () -> Unit = {}) {
        if (!updated || this.value == value) {
            val equal = value == value
            this.value = value
            updated = false
            if (!equal) {
                onChanged()
            }
        }
    }
}