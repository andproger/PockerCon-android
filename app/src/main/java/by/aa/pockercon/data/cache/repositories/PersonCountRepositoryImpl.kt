package by.aa.pockercon.data.cache.repositories

import by.aa.pockercon.domain.gateways.repositories.PersonCountRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class PersonCountRepositoryImpl : PersonCountRepository {

    private val changeSubject = BehaviorSubject.createDefault(true)

    override fun get(): Int {
        //TODO
        return Random().nextInt(10) + 1
    }

    override fun getWithUpdates(): Observable<Int> {
        return changeSubject.map { get() }
    }

    override fun save(count: Int) {
        //TODO
        changeSubject.onNext(true)
    }
}