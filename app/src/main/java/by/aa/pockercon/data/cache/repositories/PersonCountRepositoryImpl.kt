package by.aa.pockercon.data.cache.repositories

import android.annotation.SuppressLint
import android.content.Context
import by.aa.pockercon.domain.gateways.repositories.PersonCountRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class PersonCountRepositoryImpl(private val context: Context) : PersonCountRepository {

    private val changeSubject = BehaviorSubject.createDefault(true)

    @SuppressLint("ApplySharedPref")
    override fun save(count: Int) {
        sp.edit().putInt(SP_KEY_PERSON_COUNT, count).commit()
        changeSubject.onNext(true)
    }

    override fun get() = sp.getInt(SP_KEY_PERSON_COUNT, 0)

    override fun getWithUpdates(): Observable<Int> {
        return changeSubject.map { get() }
    }

    private val sp
        get() = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    companion object {
        const val SP_NAME = "sp_person_count"
        const val SP_KEY_PERSON_COUNT = "count"
    }
}