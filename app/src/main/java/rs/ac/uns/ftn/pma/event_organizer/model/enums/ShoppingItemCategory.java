package rs.ac.uns.ftn.pma.event_organizer.model.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ShoppingItemCategory implements Serializable {
    FOOD(new CategoryUnit[]{
            CategoryUnit.KG,
            CategoryUnit.PCS
    }),
    DRINK(new CategoryUnit[]{
            CategoryUnit.L,
            CategoryUnit.PCS
    });

    private CategoryUnit[] units;

    ShoppingItemCategory(CategoryUnit[] units) {
        this.units = units;
    }

    public List<CategoryUnit> getUnits() {
        return new ArrayList<>(Arrays.asList(units));
    }
}
