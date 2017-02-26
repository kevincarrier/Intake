package org.intakers.intake.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chrx on 2/25/17.
 */

public class Post {

    //Make sure the serialized name is the same as it appears in the JSON

    @SerializedName("name")
    private String permalinkURL;

    @SerializedName("weight")
    private String thumbnailURL;

    @SerializedName("measure")
    private String titleURL;
}
