package be.ugent.webdevelopment.backend.geocode.services

import org.springframework.stereotype.Service

@Service
interface ServiceInterface<T> {

    fun findAll() : List<T>

    fun findById(id: Long) : T

    fun create(resource: T) : Long

    fun getById(id: Long)

    fun update(resource: T)

    fun deleteById(id: Long)

}