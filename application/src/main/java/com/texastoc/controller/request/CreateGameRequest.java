package com.texastoc.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.texastoc.model.season.Quarter;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class CreateGameRequest {
    @NotNull(message = "host id is required")
    private Integer hostId;

    @NotNull(message = "date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "doubleBuyIn is required")
    private Boolean doubleBuyIn;

    @NotNull(message = "transportRequired is required")
    private Boolean transportRequired;

}