package be.ugent.webdevelopment.backend.geocode.services

import org.springframework.stereotype.Service

@Service
interface ServiceInterface<T, X> {

    fun findAll() : List<T>

    fun findById(id: X) : T

    fun create(resource: T) : X

    fun update(id: X, resource: T) : Int

    fun deleteById(id: X): Int

}