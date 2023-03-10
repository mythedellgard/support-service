package com.training.dto.ticket;

import com.training.dto.category.CategoryDto;
import com.training.dto.comment.InputCommentDto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class InputDraftTicketDto {

    private static final String TEXT_PATTERN = "[0-9a-zA-Z~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|} ]{0,500}";
    private static final String CATEGORY_VALIDATION_MSG = "The 'category' field cannot be empty.";
    private static final String NAME_VALIDATION_MSG = "The ticket's name must contain at least one English character, "
            + "digit or a special symbol and not above 100.";
    private static final String DESCRIPTION_VALIDATION_MSG = "Description consists of English characters, "
            + "digits or a special symbols and not above 500.";
    private static final String DESIRED_RESOLUTION_DATE_VALIDATION_MSG = "Desired date cannot contain past date.";

    private UUID id;

    @NotNull(message = CATEGORY_VALIDATION_MSG)
    private CategoryDto category;

    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = TEXT_PATTERN,
            message = NAME_VALIDATION_MSG)
    private String name;

    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = TEXT_PATTERN,
            message = DESCRIPTION_VALIDATION_MSG)
    private String description;

    private String urgency;

    @FutureOrPresent(message = DESIRED_RESOLUTION_DATE_VALIDATION_MSG)
    private LocalDateTime desiredResolutionDate;

    private InputCommentDto comment;
}
