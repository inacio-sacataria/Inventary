package com.decode.microtic.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Users(
    var id: String? ="",
    var nome : String?= "",
    var email : String? = "",
    var role : String?  ="",
) : Parcelable
