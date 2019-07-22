package by.aa.pockercon.domain.interactors.chips

interface AddChipInteractor {
    fun add(newChip: NewChip)
}

class NewChip(
    val number: Int,
    val quantity: Int
)