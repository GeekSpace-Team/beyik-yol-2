package com.android.beyikyol2.component.widget

import com.android.beyikyol2.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int,
    val pos: Int
) {
    companion object{
        fun getData(): List<OnBoardingItems>{
            return listOf(
                OnBoardingItems(R.drawable.one, R.string.one_title, R.string.desc_title,1),
                OnBoardingItems(R.drawable.two, R.string.second_title, R.string.second_desc,2),
                OnBoardingItems(R.drawable.three, R.string.third_title, R.string.third_desc,3)
            )
        }
    }
}