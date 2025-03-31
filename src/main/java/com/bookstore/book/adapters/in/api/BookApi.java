package com.bookstore.book.adapters.in.api;

import com.bookstore.book.adapters.in.api.mapper.BookMapper;
import com.bookstore.book.adapters.in.api.request.CreateBookRequest;
import com.bookstore.book.adapters.in.api.request.UpdateBookRequest;
import com.bookstore.book.adapters.in.api.response.BookResponse;
import com.bookstore.book.adapters.in.api.response.ExceptionResponse;
import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;
import com.bookstore.book.application.ports.in.BookCrudInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book/v1")
@Tag(name = "Book API V1", description = "This API exposes resources about Book Domain")
public class BookApi {
    private final BookCrudInputPort bookCrudInputPort;
    private final BookMapper bookMapper;

    public BookApi(
            BookCrudInputPort bookCrudInputPort,
            BookMapper bookMapper
    ) {
        this.bookCrudInputPort = bookCrudInputPort;
        this.bookMapper = bookMapper;
    }

    @Operation(summary = "Create Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book created successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to create book", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody CreateBookRequest createBookRequest) {
        BookDomain bookDomain = bookCrudInputPort.createBook(bookMapper.createBookRequestToBookDomain(createBookRequest));
        return ResponseEntity.ok(bookMapper.bookDomainToBookResponse(bookDomain));
    }

    @Operation(summary = "Update Book by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to update book", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @PutMapping("{bookId}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("bookId") Long bookId, @RequestBody UpdateBookRequest updateBookRequest) throws BookNotFoundException {
        BookDomain bookDomain = bookCrudInputPort.updateBook(bookId, bookMapper.updateBookRequestToBookDomain(updateBookRequest));
        return ResponseEntity.ok(bookMapper.bookDomainToBookResponse(bookDomain));
    }

    @Operation(summary = "Get Book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to return book", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @GetMapping("{bookId}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("bookId") Long bookId) throws BookNotFoundException {
        BookDomain bookDomain = bookCrudInputPort.getBookById(bookId);
        return ResponseEntity.ok(bookMapper.bookDomainToBookResponse(bookDomain));
    }

    @Operation(summary = "Delete Book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to delete book", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("bookId") Long bookId) throws BookNotFoundException {
        bookCrudInputPort.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book list returned successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to return book list", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @GetMapping()
    public ResponseEntity<List<BookResponse>> getBookList() {
        List<BookDomain> bookDomains = bookCrudInputPort.getAllBooks();
        return ResponseEntity.ok(bookMapper.bookDomainsToBookResponses(bookDomains));
    }
}
