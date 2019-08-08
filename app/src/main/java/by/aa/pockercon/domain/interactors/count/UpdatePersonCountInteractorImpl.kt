package by.aa.pockercon.domain.interactors.count

import by.aa.pockercon.domain.gateways.repositories.PersonCountRepository

class UpdatePersonCountInteractorImpl(
    private val personCountRepository: PersonCountRepository
) : UpdatePersonCountInteractor {

    override fun update(personCount: Int) {
        if (personCount in 2..29) {
            personCountRepository.save(personCount)
        }
    }
}