package com.android.beyikyol2.core.util





class ImageUrl {
    val USER = "USER"
    val IMAGE_BASE_URL="https://beyikyol.com"
    val ADS = "ADS"
    val CAR = "CAR"
    fun getFullUrl(url: String, type: String): String{
        return if(type == ADS){
            "$IMAGE_BASE_URL/car/ads/$url"
        } else if(type==CAR){
            return "$IMAGE_BASE_URL/car/image/$url"
        }else if(type==USER){
            return "$IMAGE_BASE_URL/users/images/$url"
        } else {
            ""
        }
    }

}

