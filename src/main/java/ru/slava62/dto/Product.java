package ru.slava62.dto;

//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

//@JsonInclude(JsonInclude.Include.NON_NULL)

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("categoryTitle")
    @Expose
    private String categoryTitle;
}
