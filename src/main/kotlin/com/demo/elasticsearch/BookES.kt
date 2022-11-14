package com.demo.elasticsearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import java.util.*


@Document(indexName = "books", type = "book")
class BookES {

  @Id
  private val id: String = UUID.randomUUID().toString()

  private var title: String = ""

  private var publicationYear = 0

  private var authorName: String = ""

  private var isbn: String = ""

  fun getId(): String {
    return id
  }

  fun getTitle(): String {
    return title
  }

  fun getPublicationYear(): Int {
    return publicationYear
  }

  fun getAuthorName(): String {
    return authorName
  }

  fun getIsbn(): String {
    return isbn
  }

  fun setTitle(title: String) {
    this.title = title
  }

  fun setPublicationYear(publicationYear: Int) {
    this.publicationYear = publicationYear
  }

  fun setAuthorName(authorName: String) {
    this.authorName = authorName
  }

  fun setIsbn(isbn: String) {
    this.isbn = isbn
  }

  companion object {

    fun fromDto(bookDto: BookDto): BookES {
      val bookES = BookES()
      bookES.setTitle(bookDto.title)
      bookES.setPublicationYear(bookDto.publicationYear)
      bookES.setAuthorName(bookDto.authorName)
      bookES.setIsbn(bookDto.isbn)
      return bookES
    }

  }

}