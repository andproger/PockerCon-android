package by.aa.pockercon.domain.interactors.count

import by.aa.pockercon.domain.gateways.repositories.PersonCountRepository

class UpdatePersonCountInteractorImpl(
        private val personCountRepository: PersonCountRepository
) : UpdatePersonCountInteractor {

    override fun plus() {
        val count = personCountRepository.get()
        if (count < 30) {
            personCountRepository.save(count + 1)
        }
    }

    override fun minus() {
        val count = personCountRepository.get()
        if (count > 1) {
            personCountRepository.save(count - 1)
        }
    }
}