package be.ugent.webdevelopment.backend.geocode.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

interface Controller<T> {

    @GetMapping
    fun findAll() : List<T>

    @GetMapping(value = ["/{id}"])
    fun findById(@PathVariable("id") id:Long) : T

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody resource:T) : Long

    @PutMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable("id") id: Long, @RequestBody resource: T)

    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable(value = "id") id: Long)

}