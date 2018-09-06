package com.spoonart.movieclip.util

import com.orm.SugarRecord
import com.spoonart.movieclip.model.AccountDetail
import com.spoonart.movieclip.model.Auth
import com.spoonart.movieclip.model.movie.Genre
import com.spoonart.movieclip.model.Session

object DBUtil {

    fun saveToken(auth: Auth) {
        if (isExist(Auth::class.java)) {
            SugarRecord.update(auth)
            return
        }

        SugarRecord.save(auth)
    }

    fun saveAccountData(account: AccountDetail) {
        if (isExist(AccountDetail::class.java)) {
            SugarRecord.update(account)
            return
        }
        SugarRecord.save(account)
    }

    fun saveSession(session: Session) {
        if (isExist(Session::class.java)) {
            SugarRecord.update(session)
            return
        }
        SugarRecord.save(session)
    }

    fun saveGenres(genres: List<Genre>) {
        if (isExist(Genre::class.java)) {
            SugarRecord.updateInTx(genres)
            return
        }
        SugarRecord.saveInTx(genres)
    }

    private fun isExist(model: Class<*>): Boolean {
        val isExist = SugarRecord.findAll(model).hasNext()
        if (isExist) {
            debugTotal(model)
        }
        return isExist
    }

    private fun debugTotal(model: Class<*>) {
        val total = SugarRecord.find(model, "").size
        println("total entity: ${model.simpleName} -> $total")
    }

    fun getGenreNameById(genreIds: List<Int>): String {
        if (isExist(Genre::class.java)) {
            val genres = arrayListOf<String>()
            genreIds.forEach {
                val entity = SugarRecord.find(Genre::class.java, "genre_id = ?", it.toString()).first()
                genres.add(entity.name)
            }
            return genreNames(genres)
        }
        return "no genre found"
    }

    private fun genreNames(genres: List<String>): String {
        var collection = ""
        genres.forEach {
            collection += it + ", "
        }
        return collection
    }
}