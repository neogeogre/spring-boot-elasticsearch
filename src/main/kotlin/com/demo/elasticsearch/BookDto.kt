package com.demo.elasticsearch

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class BookDto(

    @NotBlank
    var title: String,

    @NotBlank
    var authorName: String,

    @Positive
    @PublicationYear
    var publicationYear: Int,

    @NotBlank
    var isbn: String,
)
