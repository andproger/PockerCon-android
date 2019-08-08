package by.aa.pockercon.domain.interactors.count

import by.aa.pockercon.domain.gateways.repositories.PersonCountRepository
import io.reactivex.Observable

class GetPersonCountInteractorImpl(
    private val personCountRepository: PersonCountRepository
) : GetPersonCountInteractor {

    override fun get(): Observable<Int> {
        return personCountRepository.getWithUpdates()
    }
}