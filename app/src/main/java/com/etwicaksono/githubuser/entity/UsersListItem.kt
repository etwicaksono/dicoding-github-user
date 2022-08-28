package com.etwicaksono.githubuser.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersListItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val username: String,

    @field:SerializedName("avatar_url")
    val avatar: String? = null
) : Parcelable
