package io.gatling.demostore.api.payloads;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryRequest {

    @Schema(example = "My new category", required = true)
    @NotNull
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
