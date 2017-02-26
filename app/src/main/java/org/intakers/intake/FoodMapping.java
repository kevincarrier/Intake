package org.intakers.intake;

/**
 * Created by Kevin on 2/26/17.
 */

public class FoodMapping {

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("food")
    private String mFood;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("url")
    private String mUrl;

    /**
     * ToDoItem constructor
     */
    public FoodMapping() {

    }

    @Override
    public String toString() {
        return getFood();
    }


    public FoodMapping(String food, String id) {
        this.setFood(food);
        this.setId(id);
    }

    /**
     * Returns the item text
     */
    public String getFood() {
        return mFood;
    }


    public final void setFood(String food) {
        mFood = food;
    }

    /**
     * Returns the item id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        mId = id;
    }

    /**
     * Indicates if the item is marked as completed
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FoodMapping && ((FoodMapping) o).mId == mId;
    }
}
