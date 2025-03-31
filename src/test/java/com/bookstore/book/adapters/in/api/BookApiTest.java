package com.bookstore.book.adapters.in.api;

import com.bookstore.book.adapters.in.api.mapper.BookRequestMapper;
import com.bookstore.book.adapters.in.api.request.CreateBookRequest;
import com.bookstore.book.adapters.in.api.request.UpdateBookRequest;
import com.bookstore.book.adapters.in.api.response.BookResponse;
import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.ports.in.BookCrudInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(BookApi.class)
@ExtendWith(SpringExtension.class)
public class BookApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookCrudInputPort bookCrudInputPort;

    @MockitoBean
    private BookRequestMapper bookRequestMapper;

    @DisplayName("Should Create Book")
    @Test
    void should_createBook() throws Exception {

        CreateBookRequest createBookRequest = CreateBookRequest.builder()
                .author("Vinicius")
                .build();

        BookDomain bookDomain = BookDomain.builder()
                .author("Vinicius")
                .build();

        BookResponse bookResponse = BookResponse.builder()
                .author("Vinicius")
                .build();

        Mockito.when(bookRequestMapper.createBookRequestToBookDomain(createBookRequest)).thenReturn(bookDomain);
        Mockito.when(bookCrudInputPort.createBook(bookDomain)).thenReturn(bookDomain);
        Mockito.when(bookRequestMapper.bookDomainToBookResponse(bookDomain)).thenReturn(bookResponse);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/book/v1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createBookRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("Vinicius")));

        Mockito.verify(bookCrudInputPort, Mockito.times(1)).createBook(bookDomain);
        Mockito.verifyNoMoreInteractions(bookCrudInputPort);

        Mockito.verify(bookRequestMapper, Mockito.times(1)).createBookRequestToBookDomain(ArgumentMatchers.any());
        Mockito.verify(bookRequestMapper, Mockito.times(1)).bookDomainToBookResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(bookRequestMapper);

    }

    @DisplayName("Should Update Book")
    @Test
    void should_updateBook() throws Exception {

        Long bookId = 123L;

        UpdateBookRequest updateBookRequest = UpdateBookRequest.builder()
                .id(bookId)
                .author("Vinicius")
                .build();

        BookDomain bookDomain = BookDomain.builder()
                .id(bookId)
                .author("Vinicius")
                .build();

        BookResponse bookResponse = BookResponse.builder()
                .id(bookId)
                .author("Vinicius")
                .build();

        Mockito.when(bookRequestMapper.updateBookRequestToBookDomain(updateBookRequest)).thenReturn(bookDomain);
        Mockito.when(bookCrudInputPort.updateBook(bookId, bookDomain)).thenReturn(bookDomain);
        Mockito.when(bookRequestMapper.bookDomainToBookResponse(bookDomain)).thenReturn(bookResponse);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/book/v1/{bookId}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateBookRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("Vinicius")));

        Mockito.verify(bookCrudInputPort, Mockito.times(1)).updateBook(bookId, bookDomain);
        Mockito.verifyNoMoreInteractions(bookCrudInputPort);

        Mockito.verify(bookRequestMapper, Mockito.times(1)).updateBookRequestToBookDomain(ArgumentMatchers.any());
        Mockito.verify(bookRequestMapper, Mockito.times(1)).bookDomainToBookResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(bookRequestMapper);

    }

    @DisplayName("Should Get Book by Id")
    @Test
    void should_getBookById() throws Exception {

        Long bookId = 123L;

        BookDomain bookDomain = BookDomain.builder()
                .id(bookId)
                .author("Vinicius")
                .build();

        BookResponse bookResponse = BookResponse.builder()
                .id(bookId)
                .author("Vinicius")
                .build();

        Mockito.when(bookCrudInputPort.getBookById(bookId)).thenReturn(bookDomain);
        Mockito.when(bookRequestMapper.bookDomainToBookResponse(bookDomain)).thenReturn(bookResponse);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/book/v1/{bookId}", bookId)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("Vinicius")));

        Mockito.verify(bookCrudInputPort, Mockito.times(1)).getBookById(bookId);
        Mockito.verifyNoMoreInteractions(bookCrudInputPort);

        Mockito.verify(bookRequestMapper, Mockito.times(1)).bookDomainToBookResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(bookRequestMapper);

    }

    @DisplayName("Should Delete Book by Id")
    @Test
    void should_deleteBookById() throws Exception {

        Long bookId = 123L;

        Mockito.doNothing().when(bookCrudInputPort).deleteBook(bookId);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/book/v1/{bookId}", bookId)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(bookCrudInputPort, Mockito.times(1)).deleteBook(bookId);
        Mockito.verifyNoMoreInteractions(bookCrudInputPort);

    }

    @DisplayName("Should Get All Book")
    @Test
    void should_getAllBook() throws Exception {

        Long bookId = 123L;

        BookDomain bookDomain = BookDomain.builder()
                .id(bookId)
                .author("Vinicius")
                .build();

        BookResponse bookResponse = BookResponse.builder()
                .id(bookId)
                .author("Vinicius")
                .build();

        Mockito.when(bookCrudInputPort.getAllBooks()).thenReturn(List.of(bookDomain));
        Mockito.when(bookRequestMapper.bookDomainsToBookResponses(List.of(bookDomain))).thenReturn(List.of(bookResponse));

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/book/v1/all")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author", Matchers.is("Vinicius")));

        Mockito.verify(bookCrudInputPort, Mockito.times(1)).getAllBooks();
        Mockito.verifyNoMoreInteractions(bookCrudInputPort);

        Mockito.verify(bookRequestMapper, Mockito.times(1)).bookDomainsToBookResponses(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(bookRequestMapper);

    }
}
