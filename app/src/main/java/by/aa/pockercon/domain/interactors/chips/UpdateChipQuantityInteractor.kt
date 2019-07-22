package by.aa.pockercon.domain.interactors.chips

interface UpdateChipQuantityInteractor {
    fun update(chipContract: UpdateChipContract)
}

class UpdateChipContract(
    val id: Int,
    val quantity: Int
)