package com.gempukku.swccgo.packagedProduct;

import com.gempukku.swccgo.game.CardCollection;
import com.gempukku.swccgo.game.SwccgCardBlueprintLibrary;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Defines an Enhanced Jabba's Palace pack (Master Luke).
 */
public class EnhancedJabbasPalacePack_MasterLuke extends BasePackagedCardProduct {
    private List<String> _premiumCards = new ArrayList<String>();

    /**
     * Creates an Enhanced Cloud City pack (Master Luke).
     * @param library the blueprint library
     */
    public EnhancedJabbasPalacePack_MasterLuke(SwccgCardBlueprintLibrary library) {
        super(library);
        _premiumCards.add("110_2");
        _premiumCards.add("110_7");
        _premiumCards.add("110_8");

        filterNonExistingCards(_premiumCards);
    }

    /**
     * Gets the name of the product.
     * @return the name of the product.
     */
    @Override
    public String getProductName() {
        return ProductName.ENHANCED_JABBAS_PALACE_PACK_MASTER_LUKE;
    }

    /**
     * Gets the price of the product.
     * @return the price of the product
     */
    @Override
    public float getProductPrice() {
        return ProductPrice.ENHANCED_JABBAS_PALACE_PACK;
    }

    /**
     * Opens the packaged card product.
     * @return the card collection items contained in the packaged card product.
     */
    @Override
    public List<CardCollection.Item> openPackage() {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        addCards(result, _premiumCards, false);
        addProducts(result, ProductName.JABBAS_PALACE_BOOSTER_PACK, 4);
        return result;
    }
}
