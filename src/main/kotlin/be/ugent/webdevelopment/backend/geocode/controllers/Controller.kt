package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.Wrapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


interface Controller<T : Wrapper>