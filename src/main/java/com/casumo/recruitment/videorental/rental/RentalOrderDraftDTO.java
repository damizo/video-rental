package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalOrderDraftDTO {
    private List<RentFilmEntryDTO> films = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
