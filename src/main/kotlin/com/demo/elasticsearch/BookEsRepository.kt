package com.demo.elasticsearch

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface BookEsRepository : ElasticsearchRepository<BookES, String> {
  fun findByAuthorName(authorName: String): List<BookES>
  fun findByIsbn(isbn: String): BookES?
}
