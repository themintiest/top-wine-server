package vn.topwines.products.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProductQuantityMessage {
    private String type;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long productId;
        private Integer quantity;
    }
}
