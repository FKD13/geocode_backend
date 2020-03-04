package be.ugent.webdevelopment.backend.geocode.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

interface Controller<T, X> {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll() : List<T>

    @GetMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable("id") id: X) : T

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody resource:T) : X

    @PutMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable("id") id: X, @RequestBody resource: T)

    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable(value = "id") id: X)

}