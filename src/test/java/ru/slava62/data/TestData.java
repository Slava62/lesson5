package ru.slava62.data;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;
import ru.slava62.enums.CategoryType;

import static ru.slava62.enums.CategoryType.*;

public class TestData {
    public static Stream<Arguments> productDataProvider() {
        return Stream.of(
                Arguments.of("Apples",90,FOOD.getTitle()),
                Arguments.of("Grapes",120,FOOD.getTitle()),
                Arguments.of("Microwave Oven",5380,ELECTRONIC.getTitle()),
                Arguments.of("Mushrooms",350,FOOD.getTitle())
               // Arguments.of("Table",3460,FURNITURE.getTitle())
        );}
    public static Stream<Arguments> categoryDataProvider() {
        return Stream.of(
                Arguments.of("test_category_1"),
                Arguments.of("test_category_2"),
                Arguments.of("test_category_3"),
                Arguments.of("test_category_4")
        );}
}
