package edu.pacificu.cs493f15_1.paperorplasticapp.kitchenInventory;

import android.os.Bundle;

import edu.pacificu.cs493f15_1.paperorplasticapp.R;
import edu.pacificu.cs493f15_1.paperorplasticapp.popList.PoPListItemsActivity;
import edu.pacificu.cs493f15_1.paperorplasticjava.KitchenInventories;

/**
 * Created by sull0678 on 4/4/2016.
 */
public class KitchenInventoryItemsActivity extends PoPListItemsActivity {

    /********************************************************************************************
     * Function name: onCreate
     * <p/>
     * Description:   Initializes all needed setup for objects in page
     * <p/>
     * Parameters:    savedInstanceState  - a bundle object
     * <p/>
     * Returns:       none
     ******************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        PoPOnCreate(savedInstanceState, new KitchenInventories(), KitchenInventories.KITCHEN_FILE_NAME, false);
    }

}
