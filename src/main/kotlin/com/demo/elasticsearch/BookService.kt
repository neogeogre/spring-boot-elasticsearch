package com.demo.elasticsearch

import org.elasticsearch.index.query.QueryBuilders
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class BookService(
    private val bookEsRepository: BookEsRepository,
    private val elasticsearchTemplate: ElasticsearchTemplate
) {
  fun getByIsbn(isbn: String): BookES? {
    return bookEsRepository.findByIsbn(isbn)
  }

  val all: List<BookES>
    get() {
      val books: MutableList<BookES> = ArrayList()
      bookEsRepository.findAll().forEach(Consumer { book: BookES -> books.add(book) })
      return books
    }

  fun findByAuthor(authorName: String): List<BookES> {
    return bookEsRepository.findByAuthorName(authorName)
  }

  fun findByTitleAndAuthor(title: String, author: String): List<BookES> {
    val criteria = QueryBuilders.boolQuery()
    criteria.must().addAll(listOf(QueryBuilders.matchQuery("authorName", author), QueryBuilders.matchQuery("title", title)))
    return elasticsearchTemplate.queryForList(NativeSearchQueryBuilder().withQuery(criteria).build(), BookES::class.java)
  }

  fun create(book: BookES): BookES {
    if (getByIsbn(book.getIsbn()) == null) {
      return bookEsRepository.save(book)
    }
    throw RuntimeException(String.format("The provided ISBN: %s already exists. Use update instead!", book.getIsbn()))
  }

  fun deleteById(id: String) {
    bookEsRepository.deleteById(id)
  }

  fun update(id: String, book: BookES): BookES {
    val oldBook = bookEsRepository.findByIdOrNull(id) ?: throw RuntimeException("There is not book associated with the given id")
    oldBook.setIsbn(book.getIsbn())
    oldBook.setAuthorName(book.getAuthorName())
    oldBook.setPublicationYear(book.getPublicationYear())
    oldBook.setTitle(book.getTitle())
    return bookEsRepository.save(oldBook)
  }

}