package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip

class GetChipsInteractorImpl : GetChipsInteractor {

    override fun getAll(): List<Chip> {
        //TODO
        return listOf(
            Chip(20, 13),
            Chip(10, 12),
            Chip(5, 11),
            Chip(50, 10),
            Chip(25, 9)
        ).sortedBy { it.number }
    }
}