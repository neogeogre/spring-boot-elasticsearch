package com.demo.elasticsearch

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class BookServiceTest(
    @Autowired var bookService: BookService,
    @Autowired var template: ElasticsearchTemplate,
) {

  companion object {

    @Container
    val elasticsearchContainer = EsTestContainer()

    @JvmStatic
    @BeforeAll
    fun startContainer() {
      elasticsearchContainer.start()
    }

    @JvmStatic
    @AfterAll
    fun stopContainer() {
      elasticsearchContainer.stop()
    }
  }

  @BeforeEach
  fun createIndex() {
    Assertions.assertTrue(elasticsearchContainer.isRunning)
    if (template.indexExists(BookES::class.java)) {
      template.deleteIndex(BookES::class.java)
      template.createIndex(BookES::class.java)
    }
  }

  @Test
  fun testGetBookByIsbn() {
    bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2018, "978-0345816023")))
    val book = bookService.getByIsbn("978-0345816023")
    assertNotNull(book)
    assertEquals("12 rules for life", book!!.getTitle())
    assertEquals("Jordan Peterson", book.getAuthorName())
    assertEquals(2018, book.getPublicationYear())
    assertEquals("978-0345816023", book.getIsbn())
  }

  @Test
  fun testGetAllBooks() {
    bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2018, "978-0345816023")))
    bookService.create(BookES.fromDto(BookDto("The Cathedral and the Bazaar", "Eric Raymond", 1999, "9780596106386")))
    val books = bookService.all
    assertNotNull(books)
    assertEquals(2, books.size)
  }

  @Test
  fun testFindByAuthor() {
    bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2018, "978-0345816023")))
    bookService.create(BookES.fromDto(BookDto("Maps of Meaning", "Jordan Peterson", 1999, "9781280407253")))
    val books = bookService.findByAuthor("Jordan Peterson")
    assertNotNull(books)
    assertEquals(2, books.size)
  }

  @Test
  fun testFindByTitleAndAuthor() {
    bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2018, "978-0345816023")))
    bookService.create(BookES.fromDto(BookDto("Rules or not rules?", "Jordan Miller", 2010, "978128000000")))
    bookService.create(BookES.fromDto(BookDto("Poor economy", "Jordan Miller", 2006, "9781280789000")))
    bookService.create(BookES.fromDto(BookDto("The Cathedral and the Bazaar", "Eric Raymond", 1999, "9780596106386")))
    val books = bookService.findByTitleAndAuthor("rules", "jordan")
    assertNotNull(books)
    assertEquals(2, books.size)
  }

  @Test
  fun testCreateBook() {
    val createdBook = bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2018, "978-0345816023")))
    assertNotNull(createdBook)
    assertNotNull(createdBook.getId())
    assertEquals("12 rules for life", createdBook.getTitle())
    assertEquals("Jordan Peterson", createdBook.getAuthorName())
    assertEquals(2018, createdBook.getPublicationYear())
    assertEquals("978-0345816023", createdBook.getIsbn())
  }

  @Test
  fun testCreateBookWithDuplicateISBNThrowsException() {
    val createdBook = bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2018, "978-0345816023")))
    assertNotNull(createdBook)
    Assertions.assertThrows(RuntimeException::class.java) { bookService.create(BookES.fromDto(BookDto("Test title", "Test author", 2010, "978-0345816023"))) }
  }

  @Test
  fun testDeleteBookById() {
    val createdBook = bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2018, "978-0345816023")))
    assertNotNull(createdBook)
    assertNotNull(createdBook.getId())
    bookService.deleteById(createdBook.getId())
    val books = bookService.findByAuthor("Jordan Peterson")
    Assertions.assertTrue(books.isEmpty())
  }

  @Test
  fun testUpdateBook() {
    val bookToUpdate = bookService.create(BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2000, "978-0345816023")))
    assertNotNull(bookToUpdate)
    assertNotNull(bookToUpdate.getId())
    bookToUpdate.setPublicationYear(2018)
    val updatedBook = bookService.update(bookToUpdate.getId(), bookToUpdate)
    assertNotNull(updatedBook)
    assertNotNull(updatedBook.getId())
    assertEquals("12 rules for life", updatedBook.getTitle())
    assertEquals("Jordan Peterson", updatedBook.getAuthorName())
    assertEquals(2018, updatedBook.getPublicationYear())
    assertEquals("978-0345816023", updatedBook.getIsbn())
  }

  @Test
  fun testUpdateBookThrowsExceptionIfCannotFindBook() {
    val updatedBook = BookES.fromDto(BookDto("12 rules for life", "Jordan Peterson", 2000, "978-0345816023"))
    Assertions.assertThrows(RuntimeException::class.java) { bookService.update("1A2B3C", updatedBook) }
  }

}