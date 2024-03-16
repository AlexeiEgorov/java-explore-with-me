package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
public class CompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @Pattern(regexp = "^(?!\\s*$).+", message = "Field must not consist only of whitespace characters",
            groups = Marker.Update.class)
    @NotBlank(groups = Marker.Create.class)
    @Size(max = 50, groups = {Marker.Create.class, Marker.Update.class})
    private String title;
}
