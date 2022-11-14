package com.demo.elasticsearch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/books")
class BookController(
    var bookService: BookService
) {

  @get:GetMapping
  @get:ResponseStatus(HttpStatus.OK)
  val allBooks: List<Any>
    get() = bookService.all

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PostMapping
  fun createBook(@Valid @RequestBody book: BookDto): BookES {
    return bookService.create(BookES.fromDto(book))
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = ["/{isbn}"])
  fun getBookByIsbn(@PathVariable isbn: String): BookES {
    return bookService.getByIsbn(isbn) ?: throw RuntimeException("The given isbn is invalid")
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = ["/query"])
  fun getBooksByAuthorAndTitle(@RequestParam(value = "title") title: String, @RequestParam(value = "author") author: String): List<BookES> {
    return bookService.findByTitleAndAuthor(title, author)
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = ["/{id}"])
  private fun updateBook(@PathVariable id: String, @RequestBody book: BookDto): BookES {
    return bookService.update(id, BookES.fromDto(book))
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(value = ["/{id}"])
  fun deleteBook(@PathVariable id: String) {
    bookService.deleteById(id)
  }
}