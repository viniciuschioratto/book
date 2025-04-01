package com.bookstore.book.adapters.in.api;

import com.bookstore.book.adapters.in.api.mapper.PurchaseRequestMapper;
import com.bookstore.book.adapters.in.api.response.CalculatePriceResponse;
import com.bookstore.book.adapters.in.api.response.ExceptionResponse;
import com.bookstore.book.adapters.in.api.response.PurchaseResponse;
import com.bookstore.book.application.core.domain.CalculatePriceDomain;
import com.bookstore.book.application.core.domain.PurchaseDomain;
import com.bookstore.book.application.core.exceptions.PurchaseNotFoundException;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.ports.in.PurchaseInputPort;
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
@RequestMapping("/purchase/v1")
@Tag(name = "Purchase API V1", description = "This API exposes resources about Purchase domain")
public class PurchaseApi {
    private final PurchaseInputPort purchaseInputPort;
    private final PurchaseRequestMapper purchaseRequestMapper;

    public PurchaseApi(PurchaseInputPort purchaseInputPort, PurchaseRequestMapper purchaseRequestMapper) {
        this.purchaseInputPort = purchaseInputPort;
        this.purchaseRequestMapper = purchaseRequestMapper;
    }

    @Operation(summary = "Get Purchase by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase returned successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to return Purchase", content = {
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
    @GetMapping("{purchaseId}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable("purchaseId") Long purchaseId) throws PurchaseNotFoundException {
        PurchaseDomain purchaseDomain = purchaseInputPort.getPurchaseById(purchaseId);
        return ResponseEntity.ok(purchaseRequestMapper.fromPurchaseDomainToPurchaseResponse(purchaseDomain));
    }

    @Operation(summary = "Calculate Price about purchase", description = "This API receives a list of book IDs " +
            " and the user email to calculate the price of the purchase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price returned successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CalculatePriceResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to return price", content = {
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
    @GetMapping("/calculate-price")
    public ResponseEntity<CalculatePriceResponse> calculatePurchasePrice(
            @RequestParam("email") String email,
            @RequestParam("books") List<Long> booksId
    ) throws UserNotFoundException {
        CalculatePriceDomain calculatePriceDomain = purchaseInputPort.purchaseCalculatePrice(booksId, email);
        return ResponseEntity.ok(purchaseRequestMapper.fromCalculatePriceDomainToCalculatePriceResponse(calculatePriceDomain));
    }

    @Operation(summary = "Purchase book", description = "This API receives a list of book IDs " +
            " and the user email to perform the purchase action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase done successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to do the purchase", content = {
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
    @PutMapping("/purchase")
    public ResponseEntity<List<PurchaseResponse>> purchaseBooks(
            @RequestParam("email") String email,
            @RequestParam("books") List<Long> booksId
    ) throws UserNotFoundException {
        List<PurchaseDomain> purchaseDomain = purchaseInputPort.purchaseBooks(booksId, email);
        return ResponseEntity.ok(purchaseRequestMapper.fromPurchaseDomainListToPurchaseResponseList(purchaseDomain));
    }
}
